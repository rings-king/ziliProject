package com.jux.mtqiushui.dispatching.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.dispatching.common.ServerResponse;
import com.jux.mtqiushui.dispatching.model.*;
import com.jux.mtqiushui.dispatching.model.statistic_analysis.Analysis;
import com.jux.mtqiushui.dispatching.repository.AuthServiceApi;
import com.jux.mtqiushui.dispatching.services.ProcessService;
import com.jux.mtqiushui.dispatching.services.ProductionLineService;
import com.jux.mtqiushui.dispatching.services.TaskTwoService;
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

import java.sql.Timestamp;
import java.util.*;
import java.util.Calendar;

/**
 * 派工单管理
 */
@RestController
@RequestMapping(value = "v1/tasktreeservice")
public class TaskTwoServiceController {

    @Autowired
    private TaskTwoService taskTwoService;
    //注入工艺流程service
    @Autowired
    private ProcessService processService;
    //注入产线定义service
    @Autowired
    private ProductionLineService productionLineService;
    //注入员工档案的api
    @Autowired
    private AuthServiceApi authServiceApi;

    /**
     * 添加派工对象
     * 使用post请求
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> addTaskTreeType(@RequestBody String param) {
        Map<String, Object> map = new HashMap<>();
        //使用jsonoject解析前端json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        //将json对象赋值---map1
        Map<String, Object> map1 = jsonObject;
        //从map中取出要添加产派工类对象
        Object type = map1.get("addnew");
        //将object转为  派工类对象
        TaskTwo taskTwo = JSONObject.toJavaObject((JSON) type, TaskTwo.class);
        //使用解析json工具类解析json字符串
        boolean allFieldNull = false;
        try {
            allFieldNull = ObjectIsNull.isAllFieldNull(taskTwo);
        } catch (Exception e) {
            map.put("error", "解析异常");
            return map;
        }
        //判断派工单对象是否为空
        if (allFieldNull == true) {
            map.put("error", "添加内容不能为空");
            return map;
        }
        // 查找是否存在派工单号-->派工单号不能重复
        TaskTwo taskTwo1 = null;
        try {
            taskTwo1 = taskTwoService.findByTaskTwoId(taskTwo.getDispatchListCode());
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        if (taskTwo1 != null) {
            map.put("error", "派工单号已经存在,请更换");
            return map;
        }
        //根据产品id查询产品名称
        Map<String, String> result = null;
        try {
            //获得产品id
            Long materialId = taskTwo.getMaterialId();
            //获得产品名和规则型号-->>查询物料定义
            result = processService.getMaterialNameAndMaterialModelById(materialId);
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        if (result == null) {
            map.put("error", "没有查到该产品名称");
            return map;
        }
        //根据工艺id查询工艺流程(id)名称以及对应的版本号
        Map<String, String> processNameAndVersion = null;
        try {
            processNameAndVersion = processService.getProcessNameAndVersionById(taskTwo.getProcessId());
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        if (processNameAndVersion == null) {
            map.put("error", "没有查到该工艺流程");
            return map;
        }
        //判断时间是否为空
        if (taskTwo.getPlannedStartTime() == null || taskTwo.getPlannedEndTime() == null) {
            map.put("error", "计划开始或计划结束时间不能为空");
            return map;
        }
        //取出json对象的工位
        Set<DistributionStationTab1> distributionStationTab1 = taskTwo.getDistributionStationTab1();
        //判断要添加的工位是否为空
        if (distributionStationTab1 != null) {
            for (DistributionStationTab1 stationTab1 : distributionStationTab1) {
                //根据工序的id去查询工序名称
                String processNumById = null;
                try {
                    processNumById = processService.getProcessNumById(stationTab1.getProcessNumId());
                    if (processNumById == null) {
                        map.put("error", "该工序不存在");
                        return map;
                    }
                } catch (Exception e) {
                    map.put("error", "服务器错误");
                    return map;
                }

                //根据产线id去查询对应的产线以及所属工位
                try {
                    Map<String, String> productionLineNameAndStationName = productionLineService.getProductionLineNameAndStationName(stationTab1.getProductionLineId(), stationTab1.getStationId());
                    if (productionLineNameAndStationName.get("productionLineName") == null) {
                        map.put("error", "该产线不存在");
                        return map;
                    }
                    if (productionLineNameAndStationName.get("stationName") == null) {
                        map.put("error", "该工位不存在");
                        return map;
                    }
                } catch (Exception e) {
                    map.put("error", "服务器错误");
                    return map;
                }
                //根据员工档案id去查询员工姓名
                String employeeById = authServiceApi.getEmpNameByid(stationTab1.getOperationId());
//                if (employeeById != null) {
//                    stationTab1.setOperationId(stationTab1.getOperationId());
//                }
                if (employeeById == null) {
                    map.put("error", "该员工不存在");
                    return map;
                }
            }
        }
        TaskTwo taskTwo2 = null;
        try {
            taskTwo2 = taskTwoService.addTaskTwo(taskTwo);
            if (taskTwo2 != null) {
                map.put("success", true);
                return map;
            }
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        map.put("error", "添加失败");
        return map;
    }

    /**
     * 查询所有
     */
    @RequestMapping(method = RequestMethod.GET)
    public Page<TaskTwo> findAllTaskTwo(@PageableDefault(sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Pageable newPageable = new PageRequest(pageable.getPageNumber() < 1 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());

            taskTwoService.modifyTaskTwoChildren();

        Page<TaskTwo> page = null;
        try {
            page = taskTwoService.findAllTaskTwo(newPageable);
        } catch (Exception e) {
            System.out.println("*********" + e.getMessage());
        }

        return page;
    }

    /**
     * 根据id查询对应的派工对象以及所属工序
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object findTaskTwoById(@PathVariable("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        if (id == null) {
            map.put("error", "ID为空");
            return map;
        }
        //根据id查询派工对象
        TaskTwo taskTwo = null;
        try {
            taskTwo = taskTwoService.findTaskTwoById(id);
            if (taskTwo == null) {
                map.put("error", "此派工信息不存在");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        return taskTwo;
    }

    /**
     * 根据id级联删除对应的派工对象以及所属的全部分配工位
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteAllTaskTwo(@PathVariable("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        if (id == null) {
            map.put("error", "ID为空");
            return map;
        }
        //根据id级联删除派工
        try {
            taskTwoService.deleteAllTaskTwo(id);
        } catch (Exception e) {
            map.put("error", "删除失败");
            return map;
        }
        map.put("success", true);
        return map;
    }

    /**
     * 修改数据
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Map<String, Object> modifyTaskTwo(@RequestBody String param) {
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 获取系统当前时间
        Date time = Calendar.getInstance().getTime();
        // 从map集合中获取派工的id
        Integer id = (Integer) jsonMap.get("id");
        //根据id查询是否存在此派工
        TaskTwo taskTwoTById = null;
        try {
            taskTwoTById = taskTwoService.findTaskTwoTById(id.longValue());
            if (taskTwoTById == null) {
                map.put("error", "此派工信息不存在");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        //获得要添加的json对象
        Object addnew = jsonMap.get("addnew");
        TaskTwo taskTwoaddnew = JSONObject.toJavaObject((JSON) addnew, TaskTwo.class);
        System.out.println("要添加的" + taskTwoaddnew);
        taskTwoaddnew.setUpdateTime(time);
        //获得要删除的json对象
        Object remove = jsonMap.get("remove");
        TaskTwo taskTworemove = JSONObject.toJavaObject((JSON) remove, TaskTwo.class);
        System.out.println("要删除的" + taskTworemove);
        taskTworemove.setUpdateTime(time);
        Object update = jsonMap.get("update");
        TaskTwo taskTwoupdate = JSONObject.toJavaObject((JSON) update, TaskTwo.class);
        System.out.println("要修改的" + taskTwoupdate);
        taskTwoupdate.setUpdateTime(time);
        List<TaskTwo> list = new ArrayList<>();
        list.add(taskTwoaddnew);
        list.add(taskTworemove);
        list.add(taskTwoupdate);
        Boolean bn = false;
        try {
            bn = taskTwoService.modifyTaskTwo(list, taskTwoTById);
            if (bn == true) {
                map.put("success", true);
                return map;
            }
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        map.put("error", "修改失败");
        return map;
    }

    /**
     * 派工单搜索
     */
    @RequestMapping(value = "/searchTaskTwo", method = RequestMethod.GET)
    public Object searchTaskTwo(String conditionList, @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        Map map = new HashMap();
        if (StringUtils.isEmpty(conditionList)){
            map.put("error","条件为空请重新输入条件");
            return map;
        }
        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);
        List<TaskTwo> taskTwoList = null;
        if (newSearchConditions.size() != 0) {
            try {
                taskTwoList = taskTwoService.findTaskTwo(newSearchConditions);
                if (taskTwoList.size() != 0) {
                    MyPageable myPageable = new MyPageable(pageable.getPageSize(), pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(), taskTwoList);
                    return myPageable;
                } else {
                    map.put("error", "没有符合该条件的信息");
                    return map;
                }
            } catch (Exception e) {
                map.put("error", "服务器错误");
                return map;
            }
        } else {
            map.put("error", "内容不能为空");
        }
        return map;
    }

    /**
     * 根据工位编号查询对应的名称,进度,编号
     * 根据工位查(时间段,不按时间段)
     * ~~
     * @return
     */
    @RequestMapping(value = "/findLnr",method = RequestMethod.GET)
    public Object findAllStationNumberById(Long stationId, Timestamp startDate, Timestamp stopDate, @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable){
        List<LocationNum> locationNums=null;
        try {
             locationNums=taskTwoService.getDisTabById(stationId,startDate,stopDate);
             MyPageable<LocationNum> myPageable=new MyPageable<LocationNum>(pageable.getPageSize(), pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(),locationNums);
             if (locationNums.size()!=0){
                return  myPageable;
             }
        } catch (Exception e) {
            throw  new RuntimeException("查询失败");
        }
        return  locationNums;
    }


    /**
     * 根据时间间隔统计各产品产量
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/findStatistic01",method = RequestMethod.GET)
    public ServerResponse getCPBytime(String startTime ,String endTime){

        ServerResponse<List<Analysis>> statistic = taskTwoService.getStatistic(startTime, endTime);

        if (statistic == null){
            return ServerResponse.createByErrorMessage("服务器错误");
        }

        return ServerResponse.createBySuccess("成功",statistic.getData());

    }


    /**
     * 根据时间间隔统计各员工产量
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/findStatistic02",method = RequestMethod.GET)
    public ServerResponse getCPbyDept(String startTime,String endTime){

        ServerResponse<List<Analysis>> statistic = taskTwoService.getstatisticBydept(startTime, endTime);

        if (statistic == null){
            return ServerResponse.createByErrorMessage("服务器错误");
        }

        return ServerResponse.createBySuccess("成功",statistic.getData());
    }

    /**
     * 根据时间间隔统计不同类型的产量对比
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/findStatistic03",method = RequestMethod.GET)
    public ServerResponse getMaterialType(String startTime,String endTime){

        ServerResponse<List<Analysis>> statistic = taskTwoService.getStaticMaterialType(startTime, endTime);

        if (statistic == null){
            return ServerResponse.createByErrorMessage("服务器错误");
        }

        return ServerResponse.createBySuccess("成功",statistic.getData());
    }
    /**
     * 根据时间间隔统计每个类型的产量对比
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "/findStatistic04",method = RequestMethod.GET)
    public ServerResponse getMaterialTypeEvery(String startTime,String endTime){

        ServerResponse<List<AnalysisTwo>> statistic = taskTwoService.getStaticMaterialTypeEvery(startTime, endTime);

        if (statistic == null){
            return ServerResponse.createByErrorMessage("服务器错误");
        }

        return ServerResponse.createBySuccess("成功",statistic.getData());
    }
}