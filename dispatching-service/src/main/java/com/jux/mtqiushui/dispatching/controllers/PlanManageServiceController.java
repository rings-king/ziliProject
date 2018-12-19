package com.jux.mtqiushui.dispatching.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.dispatching.model.PlanDetail;
import com.jux.mtqiushui.dispatching.model.PlanManage;
import com.jux.mtqiushui.dispatching.model.SearchCondition;
import com.jux.mtqiushui.dispatching.repository.ResourcesServiceApi;
import com.jux.mtqiushui.dispatching.services.PlanManageService;
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

@RestController
@RequestMapping(value = "v1/planManage")
public class PlanManageServiceController {

    @Autowired
    private PlanManageService planManageService;

    @Autowired
    private ResourcesServiceApi resourcesServiceApi;
    /**
     * 添加计划管理
     * @param param
     * @return
     */
            @RequestMapping(method = RequestMethod.POST)
            public Map<String, Object> addPlanManage(@RequestBody String param) {
                Map<String, Object> map = new HashMap<>();
                // 使用fastjson解析前端传过来的json字符串
                JSONObject jsonObject = JSONObject.parseObject(param);
                // 使用Map集合接收jsonObject
                Map<String, Object> jsonMap = jsonObject;
                // 从map集合中获取addnew对应要添加的计划管理对象
                Object jsonPlanManage = jsonMap.get("addnew");
                // 把JSONObject的物料定义对象转成JavaBean的计划管理对象
                PlanManage planManage = JSONObject.toJavaObject((JSON) jsonPlanManage, PlanManage.class);
                boolean allFieldNull = false;
                System.out.println("********:"+planManage);
                try {
                    allFieldNull = ObjectIsNull.isAllFieldNull(planManage);
                } catch (Exception e) {
            map.put("error", "解析异常");
            return map;
        }

        if (allFieldNull == true) {
            map.put("error", "添加内容不能为空");
            return map;
        }

        try {
            Set<PlanDetail> planDetailsTab = planManage.getPlanDetailsTab();
            if (planDetailsTab != null) {
                for (PlanDetail planDetail : planDetailsTab) {
                    Long materialId = planDetail.getMaterialId();
                    if (materialId != null) {
                        Map<String, String> materialDefineMap = resourcesServiceApi.getMaterialNameAndMaterialModelById(materialId);
                        if (materialDefineMap == null) {
                            map.put("error", "添加失败,该物料不存在");
                            return map;
                        }
                    }
                    Long unitId = planDetail.getUnitId();
                    if (unitId != null) {
                        String unitName = resourcesServiceApi.getMeasurementUnitNameById(unitId);
                        if (unitName == null) {
                            map.put("error", "添加失败,该单位不存在");
                            return map;
                        }
                    }
                }
            }
            planManage.setUpdateTime(Calendar.getInstance().getTime());
            PlanManage result = planManageService.addPlanManage(planManage);
            if (result == null) {
                map.put("error", "添加失败");
                return map;
            }
        } catch (Exception e) {
            System.out.println("xxxxxxxxxxxxxx:"+e.getMessage());
            map.put("error", "添加异常，请重新添加");
            return map;
        }

        map.put("success","true");
        return map;
    }

    /**
     * 获取所有计划管理 默认根据时间降序
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object getAllPlanManage(@PageableDefault(sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Pageable newPageable = new PageRequest(pageable.getPageNumber() < 1 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        Page<PlanManage> allPlanManage = null;
        try {
            allPlanManage = planManageService.getAllPlanManage(newPageable);
        } catch (Exception e) {
            Map<Object, Object> map = new HashMap<>();
            map.put("error","服务器错误");
            return map;
        }

        if (allPlanManage != null) {
            for (PlanManage planManage : allPlanManage) {
                Set<PlanDetail> planDetailsTab = planManage.getPlanDetailsTab();
                if (planDetailsTab != null) {
                    for (PlanDetail planDetail : planDetailsTab) {
                        String materialCode = planDetail.getMaterialCode();
                        if (materialCode != null) {
                            Map<String, String> maps = resourcesServiceApi.getMaterialByMaterialCode(materialCode);
                            if (maps != null) {
                                planDetail.setMaterialName(maps.get("materialName"));
                                planDetail.setMaterialModel(maps.get("materialModel"));
                                planDetail.setUnitName(maps.get("unitName"));
                            }
                        }
                    }
                }
            }
        }
        return allPlanManage;
    }

    /**
     * 根据计划派工id返回对应的计划派工信息
     * @param planManageId
     * @return
     */
    @RequestMapping(value = "/{planManageId}", method = RequestMethod.GET)
    public Object getPlanManageById(@PathVariable("planManageId") Long planManageId) {
        Map<Object, Object> map = new HashMap<>();

        if (planManageId == null) {
            map.put("error", "信息不能为空");
            return map;
        }
        PlanManage planManageById = null;
        try {
            planManageById = planManageService.getPlanManageById(planManageId);
            if (planManageById == null) {
                map.put("error", "没有查到该计划派工的信息");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "服务器异常");
            return map;
        }
        Set<PlanDetail> planDetailsTab = planManageById.getPlanDetailsTab();

        if (planDetailsTab != null) {
            for (PlanDetail planDetail : planDetailsTab) {
                String materialCode = planDetail.getMaterialCode();
                if (materialCode != null) {
                    Map<String, String> maps = resourcesServiceApi.getMaterialByMaterialCode(materialCode);
                    if (maps != null) {
                        planDetail.setMaterialName(maps.get("materialName"));
                        planDetail.setMaterialModel(maps.get("materialModel"));
                        planDetail.setUnitName(maps.get("unitName"));
                    }
                }

//                Long unitId = planDetail.getUnitId();
//                if (unitId != null) {
//                    String unitName = resourcesServiceApi.getMeasurementUnitNameById(unitId);
//                    if (unitName != null) {
//                        planDetail.setUnitName(unitName);
//                    }
//                }
            }
        }

        return planManageById;
    }

    /**
     * 根据Id删除对应的计划管理信息
     * @param planManageId
     * @return
     */
    @RequestMapping(value = "/{planManageId}", method = RequestMethod.DELETE)
    public Object deletePlanManageById(@PathVariable("planManageId") Long planManageId) {
        Map<Object, Object> map = new HashMap<>();

        if (planManageId == null) {
            map.put("error", "信息不能为空");
            return map;
        }

        try {
            PlanManage planManageById = planManageService.getPlanManageById(planManageId);
            if (planManageById == null) {
                map.put("error", "删除失败,该计划管理不存在");
                return map;
            }
            planManageService.deletePlanManageById(planManageId);
        } catch (Exception e) {
            map.put("error", "服务器异常");
            return map;
        }

        map.put("success", "true");
        return map;
    }

    /**
     * 修改计划管理
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Map<String, Object> modifyPlanManage(@RequestBody String param) {
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;

        // 获取系统当前时间
        Date time = Calendar.getInstance().getTime();

        // 从map集合中获取计划管理的id
        Integer planManageId = (Integer) jsonMap.get("id");

        // 从map集合中获取addnew对应要添加的计划管理对象
        Object addJsonMaterialDefine = jsonMap.get("addnew");
        // 把JSONObject的物料定义对象转成JavaBean的计划管理对象
        PlanManage addPlanManage = JSONObject.toJavaObject((JSON) addJsonMaterialDefine, PlanManage.class);
        addPlanManage.setUpdateTime(time);
        System.out.println("添加："+addPlanManage);

        // 从map集合中获取update对应要更新的计划管理对象
        Object updateJsonMaterialDefine = jsonMap.get("update");
        // 把JSONObject的物料定义对象转成JavaBean的计划管理对象
        PlanManage updatePlanManage = JSONObject.toJavaObject((JSON) updateJsonMaterialDefine, PlanManage.class);
        updatePlanManage.setUpdateTime(time);
        System.out.println("修改:"+updatePlanManage);

        // 从map集合中获取remove对应要删除的计划管理对象
        Object deleteJsonMaterialDefine = jsonMap.get("remove");
        // 把JSONObject的物料定义对象转成JavaBean的计划管理对象
        PlanManage deletePlanManage = JSONObject.toJavaObject((JSON) deleteJsonMaterialDefine, PlanManage.class);
        deletePlanManage.setUpdateTime(time);
        System.out.println("删除:"+deletePlanManage);

        List<PlanManage> planManageList = new ArrayList<>();
        planManageList.add(addPlanManage);
        planManageList.add(deletePlanManage);
        planManageList.add(updatePlanManage);

        Boolean restltBoolean = false;
        try {
            restltBoolean = planManageService.modifyPlanManage(planManageList, planManageId.longValue());
        } catch (Exception e) {
            map.put("error", "修改失败,服务器错误");
            return map;
        }
        if (restltBoolean == false) {
            map.put("error", "服务器错误");
            return map;
        }
        map.put("success", "true");
        return map;
    }

    /**
     * 多条件查询
     * @param conditionList
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/searchPlanManage", method = RequestMethod.GET)
    public Object searchPlanManage(String conditionList, @PageableDefault(value = 20, direction = Sort.Direction.DESC) Pageable pageable){
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(conditionList)){
            map.put("error","条件为空请重新输入条件");
            return map;
        }
        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);
        List<PlanManage> planManages = null;
        try {
            planManages = planManageService.searchPlanManage(newSearchConditions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("materialType***:"+planManages);
        MyPageable myPageable = new MyPageable(pageable.getPageSize() ,pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(), planManages);
        return myPageable;
    }

    /**
     * 根据计划管理的派工单号查询 该计划管理下的所有计划详情
     * @param planManageId
     * @return
     */
    @RequestMapping(value = "/getPlanDetail/{planManageId}", method = RequestMethod.GET)
    public Object getListPlanDetail(@PathVariable("planManageId") Long planManageId) {
        Set<PlanDetail> planDetailList = null;
        try {
            planDetailList = planManageService.getPlanDetailList(planManageId);
        } catch (Exception e) {
            Map<String, Object> map = new HashMap<>();
            map.put("error", "服务器错误");
            return map;
        }
        return planDetailList;
    }
}
