package com.jux.mtqiushui.dispatching.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.dispatching.model.Process;
import com.jux.mtqiushui.dispatching.model.SearchCondition;
import com.jux.mtqiushui.dispatching.repository.ResourcesServiceApi;
import com.jux.mtqiushui.dispatching.services.ProcessService;
import com.jux.mtqiushui.dispatching.util.MyPageable;
import com.jux.mtqiushui.dispatching.util.ObjectIsNull;
import com.jux.mtqiushui.dispatching.util.SearchFarmat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 工艺流程
 */
@RestController
@RequestMapping(value = "v1/process")
public class ProcessServiceController {

    @Autowired
    private ProcessService processService;
    @Autowired
    private ResourcesServiceApi resourcesServiceApi;

    //添加工艺流程 ->附带子表工序
    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> addProcess(@RequestBody String param) {
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的物料定义对象
        Object jsonProcessReturn = jsonMap.get("addnew");
        // 把JSONObject的物料定义对象转成JavaBean的物料定义对象
        Process process = JSONObject.toJavaObject((JSON) jsonProcessReturn, Process.class);

        boolean allFieldNull = false;

        try {
            allFieldNull = ObjectIsNull.isAllFieldNull(process);
        } catch (Exception e) {
            map.put("error", "解析异常");
            return map;
        }

        //判断工艺流程是否为空
        if (allFieldNull == true) {
            map.put("error", "添加内容不能为空");
            return map;
        }
        Map<String, String> result = null;
        try {
            Long materialDefineId = process.getProcessProductionId();
            //获得产品名和规则型号
            result = processService.getMaterialNameAndMaterialModelById(materialDefineId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) {
            map.put("error", "没有查到该产品名");
            return map;
        }
        //判断添加的时候同一产品名不能有相同的版本号
        Boolean materialName = false;
        try {
            materialName = processService.getProcessVersions(process.getProcessVersion(), result.get("materialName"));
            if (materialName == false) {
                map.put("error", "版本号重复");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        try {
            process.setUpdateTime(Calendar.getInstance().getTime());
            Process resultProcess = processService.addprocess(process);
            if (resultProcess == null) {
                map.put("error", "添加失败");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "添加异常 请重新添加");
            return map;
        }
        map.put("success", true);
        return map;
    }

    //查询所有 -->
    @RequestMapping(method = RequestMethod.GET)
    //设置根据id查询 升序排列 分页
    public Object getAllProcess(@PageableDefault(sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Pageable newPageble = new PageRequest(pageable.getPageNumber() < 1 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        Page<Process> processes;
        try {
            processes = processService.getAllProcess(newPageble);
        } catch (Exception e) {
            System.out.println("eee"+e.getMessage());
            Map<Object, Object> map = new HashMap<>();
            map.put("error", "服务器错误");
            return map;
        }
        return processes;
    }

    // /根据id查询所属的工艺流程以及附带的工序
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Object getProcessById(@PathVariable("id") Long processId) {
        Map<String, Object> map = new HashMap<>();
        if (processId == null) {
            map.put("error", "id不能为空");
        }
        Process process = null;
        try {
            process = processService.findProcessById(processId);
        } catch (Exception e) {
            map.put("error", "服务器异常");
            return map;
        }
        if (process == null) {
            map.put("error", "没有查到工艺流程信息");
            return map;
        }
        //根据产品id查询物料 产品名以及对应的规则编号
        Long processProductionId = process.getProcessProductionId();
        if (processProductionId != null) {
            Map<String, String> model = null;
            try {
                model = processService.getMaterialNameAndMaterialModelById(processProductionId);
                if (model != null) {
                    process.setProcessProductionName(model.get("materialName"));
                    process.setMaterialModel(model.get("materialModel"));
                } else {
                    process.setProcessProductionName(null);
                    process.setMaterialModel(null);
                }
            } catch (Exception e) {
                map.put("error", "服务器异常");
                return map;
            }
        }
        return process;
    }

    //根据id删除工艺流程以及下属的所有关联工序
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public Object deleteAllProcess(@PathVariable("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        if (id == null) {
            map.put("error", "服务器错误");
            return map;
        }
        //先删除
        try {
            processService.deleteProcessById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //根据刚才删除的id去查询工艺流程
        Process process = null;
        try {
            process = processService.findProcessById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (process == null) {
            map.put("success", true);
            return map;
        }
        map.put("error", "删除失败");
        return map;
    }

    //根据id修改工艺流程以及所属的工序
    @RequestMapping(method = RequestMethod.PUT)
    public Map<String, Object> modifyProcess(@RequestBody String param) {
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 获取系统当前时间
        Date time = Calendar.getInstance().getTime();
        // 从map集合中获取物料定义的id
        Integer processId = (Integer) jsonMap.get("id");
        //根据id查询对应的工艺流程以及工序信息
        Process process = null;
        try {
            process = processService.findProcessById(processId.longValue());
            if (process == null) {
                map.put("error", "没有找到此工艺信息");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "服务器错误");
        }
        // 从map集合中获取addnew对应要添加工序对象
        Object addJsonProcess = jsonMap.get("addnew");
        // 将json的addnew转为工序对象
        Process addProcess = JSONObject.toJavaObject((JSON) addJsonProcess, Process.class);
        addProcess.setUpdateTime(time);
        System.out.println("添加：" + addProcess);
        // 从map集合中获取update对应要更新的工艺对象
        Object updatejsonProcess = jsonMap.get("update");
        // 把JSONObject的物料定义对象转成JavaBean的工艺对象
        Process updateProcess = JSONObject.toJavaObject((JSON) updatejsonProcess, Process.class);
        updateProcess.setUpdateTime(time);
        System.out.println("修改:" + updateProcess);
        /**
         * 同一产品名版本号不能相同
         */
        //获得要修改的版本号
        String processVersion = updateProcess.getProcessVersion();
        if (processVersion != null) {
            //根据id查询产品名称以及规则型号
            Map<String, String> modelById = resourcesServiceApi.getMaterialNameAndMaterialModelById(updateProcess.getProcessProductionId());
            //判断添加的时候同一产品名不能有相同的版本号
            Boolean materialName = false;
            try {
                //获得版本号  获得产品名
                materialName = processService.getProcessVersions(processVersion, modelById.get("materialName"));
                if (materialName == false) {
                    map.put("error", "版本号重复");
                    return map;
                }
            } catch (Exception e) {
                map.put("error", "服务器错误");
                return map;
            }
        }
        // 从map集合中获取remove对应要删除的工艺对象
        Object deletejsonProcess = jsonMap.get("remove");
        // 把JSONObject的物料定义对象转成JavaBean的工艺对象
        Process deleteProcess = JSONObject.toJavaObject((JSON) deletejsonProcess, Process.class);
        deleteProcess.setUpdateTime(time);
        System.out.println("删除:" + deleteProcess);
        List<Process> processes = new ArrayList<>();
        processes.add(addProcess);
        processes.add(deleteProcess);
        processes.add(updateProcess);

        Boolean restltBoolean = false;
        try {
            restltBoolean = processService.modifyProcess(processes, process);
            if (restltBoolean == true) {
                map.put("success", "true");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "修改失败,服务器错误");
            return map;
        }
        map.put("error", "服务器错误");
        return map;
    }

    /**
     * 工艺流程搜索
     */
    @RequestMapping(value = "/searchProcess", method = RequestMethod.GET)
    public Object searchProcess(String conditionList, @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        Map map = new HashMap();
        if (StringUtils.isEmpty(conditionList)){
            map.put("error","条件为空请重新输入条件");
            return map;
        }
        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);
        List<Process> processList = null;
        if (newSearchConditions.size() != 0) {
            try {
                processList = processService.findProcess(newSearchConditions);
                if (processList.size()!=0) {
                    System.out.println("page:" + pageable.getPageNumber());
                    System.out.println("size:" + pageable.getPageSize());
                    MyPageable myPageable = new MyPageable(pageable.getPageSize(), pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(), processList);
                    return myPageable;
                }else {
                    map.put("error", "没有符合条件的信息");
                }
            } catch (Exception e) {
                map.put("error", "服务器错误");
                return map;
            }
        }
        return  map;
    }
}