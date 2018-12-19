package com.jux.mtqiushui.dispatching.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.dispatching.model.DeviceDefine;
import com.jux.mtqiushui.dispatching.model.SearchCondition;
import com.jux.mtqiushui.dispatching.services.DeviceDefineService;
import com.jux.mtqiushui.dispatching.services.ProductionLineService;
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
@RequestMapping(value = "v1/deviceDefine")
public class DeviceDefineServiceController {

    @Autowired
    private DeviceDefineService deviceDefineService;
    @Autowired
    private ProductionLineService productionLineService;

    private List<DeviceDefine> all;

    public List<DeviceDefine> getAll() {
        return all;
    }

    public void setAll(List<DeviceDefine> all) {
        this.all = all;
    }

    /**
     * 添加设备定义
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Map<String, String> addDeviceDefine(@RequestBody String param) {
        Map<String, String> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的设备定义对象
        Object jsondeviceDefineReceive = jsonMap.get("addnew");
        // 把JSONObject的物料定义对象转成JavaBean的设备定义对象
        DeviceDefine deviceDefine = JSONObject.toJavaObject((JSON) jsondeviceDefineReceive, DeviceDefine.class);
        deviceDefine.setUpdateTime(Calendar.getInstance().getTime());
        boolean allFieldNull = false;

        try {
            ObjectIsNull.isAllFieldNull(deviceDefine);
            if (allFieldNull == true) {
                map.put("error", "添加内容不能为空");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "解析异常");
            return map;
        }

        Map<String, String> result = new HashMap<>();
        try {
            result = productionLineService.getProductionLineNameAndStationName(deviceDefine.getProductionLineId(), deviceDefine.getStationId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) {
            map.put("error", "没有查到该所属产线或所属工位");
            return map;
        }

        try {
            DeviceDefine resultDeviceDefine = deviceDefineService.addDeviceDefine(deviceDefine);
            if (resultDeviceDefine == null) {
                map.put("error", "添加失败");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "添加异常 请重新添加");
            return map;
        }
        this.setAll(deviceDefineService.getAllDeviceDefine());
        map.put("success", "true");
        return map;
    }

    /**
     * 修改设备定义
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Map<String, Object> modifyDeviceDefine(@RequestBody String param) {
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 获取系统当前时间
        Date time = Calendar.getInstance().getTime();
        // 从map集合中获取物料定义的id
        Integer deviceDefineId = (Integer) jsonMap.get("id");
        //根据id查询对应的工艺流程以及工序信息
        DeviceDefine deviceDefine = null;
        try {
            deviceDefine = deviceDefineService.getDeviceDefineById(deviceDefineId.longValue());
            if (deviceDefine == null) {
                map.put("error", "没有找到此工艺信息");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "服务器错误");
        }
        // 从map集合中获取addnew对应要添加工序对象
        Object addJsonDeviceDefine = jsonMap.get("addnew");
        // 将json的addnew转为工序对象
        DeviceDefine addDeviceDefine= JSONObject.toJavaObject((JSON) addJsonDeviceDefine, DeviceDefine.class);
        addDeviceDefine.setUpdateTime(time);
        System.out.println("添加：" + addDeviceDefine);

        // 从map集合中获取update对应要更新的工艺对象
        Object updateJsonJsonDeviceDefine = jsonMap.get("update");
        // 把JSONObject的物料定义对象转成JavaBean的工艺对象
        DeviceDefine updateDeviceDefine = JSONObject.toJavaObject((JSON) updateJsonJsonDeviceDefine, DeviceDefine.class);
        updateDeviceDefine.setUpdateTime(time);

        // 从map集合中获取remove对应要删除的工艺对象
        Object deleteJsonProcessReturn = jsonMap.get("remove");
        // 把JSONObject的物料定义对象转成JavaBean的工艺对象
        DeviceDefine deleteDeviceDefine = JSONObject.toJavaObject((JSON) deleteJsonProcessReturn, DeviceDefine.class);
        deleteDeviceDefine.setUpdateTime(time);
        System.out.println("删除:" + deleteDeviceDefine);

        List<DeviceDefine> listDeviceDefine = new ArrayList<>();
        listDeviceDefine.add(addDeviceDefine);
        listDeviceDefine.add(deleteDeviceDefine);
        listDeviceDefine.add(updateDeviceDefine);

        Boolean restltBoolean = false;
        try {
            restltBoolean = deviceDefineService.modifyDeviceDefine(listDeviceDefine, deviceDefineId.longValue());
            if (restltBoolean == true) {
                this.setAll(deviceDefineService.getAllDeviceDefine());
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
     * 获取全部的设备定义信息
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object getAllDeviceDefine(@PageableDefault(sort = "updateTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Pageable newPageable = new PageRequest(pageable.getPageNumber() < 1 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());

        Page<DeviceDefine> allDeviceDefine = null;

        try {
            allDeviceDefine = deviceDefineService.getAllDeviceDefine(newPageable);

        } catch (Exception e) {
            Map<Object, Object> map = new HashMap<>();
            map.put("error","服务器错误");
            return map;
        }

        if (allDeviceDefine != null) {
            for (DeviceDefine deviceDefine : allDeviceDefine) {
                Long productionLineId = deviceDefine.getProductionLineId();
                Long stationId = deviceDefine.getStationId();
                Map<String, String> productionMap = null;
                try {
                    productionMap = productionLineService.getProductionLineNameAndStationName(productionLineId, stationId);
                    if (productionMap != null) {
                        deviceDefine.setProductionLine(productionMap.get("productionLineName"));
                        deviceDefine.setStationName(productionMap.get("stationName"));
                    }
                } catch (Exception e) {

                }
            }
        }
        return allDeviceDefine;
    }

    /**
     * 据计设备定义ID返回对应的设备定义信息
     * @param deviceDefineId
     * @return
     */
    @RequestMapping(value = "/{deviceDefineId}",method = RequestMethod.GET)
    public Object getDeviceDefineById(@PathVariable("deviceDefineId") Long deviceDefineId) {
        Map<Object, Object> map = new HashMap<>();

        if (deviceDefineId == null) {
            map.put("error", "信息不能为空");
            return map;
        }
        DeviceDefine deviceDefineById = null;
        try {
            deviceDefineById = deviceDefineService.getDeviceDefineById(deviceDefineId);
            if (deviceDefineById == null) {
                map.put("error", "没有查到该设备定义的信息");
                return map;
            }
            Long productionLineId = deviceDefineById.getProductionLineId();
            Long stationId = deviceDefineById.getStationId();
            Map<String, String> productionMap = productionLineService.getProductionLineNameAndStationName(productionLineId, stationId);
            if (productionMap != null) {
                deviceDefineById.setProductionLine(productionMap.get("productionLineName"));
                deviceDefineById.setStationName(productionMap.get("stationName"));
            }
        } catch (Exception e) {
            map.put("error", "服务器异常");
            return map;
        }

        return deviceDefineById;
    }

    /**
     * 据计设备定义ID删除对应的设备定义信息
     * @param deviceDefineId
     * @return
     */
    @RequestMapping(value = "/{deviceDefineId}",method = RequestMethod.DELETE)
    public Object deleteDeviceDefineById(@PathVariable("deviceDefineId") Long deviceDefineId) {
        Map<Object, Object> map = new HashMap<>();

        if (deviceDefineId == null) {
            map.put("error", "信息不能为空");
            return map;
        }
        DeviceDefine deviceDefineById = null;
        try {
            deviceDefineById = deviceDefineService.getDeviceDefineById(deviceDefineId);
            if (deviceDefineById == null) {
                map.put("error", "没有查到该设备定义的信息");
                return map;
            }
            deviceDefineService.deleteDeviceDefineById(deviceDefineId);
        } catch (Exception e) {
            map.put("error", "服务器异常");
            return map;
        }
        this.setAll(deviceDefineService.getAllDeviceDefine());
        map.put("success", "true");
        return map;
    }
    /**
     * 根据产线id查询设备是否关联了产线
     */
    public Boolean selectDeviceLineById(@PathVariable("id") Long id){
       List<DeviceDefine> list= deviceDefineService.selectAll();
        for (DeviceDefine deviceDefine : list) {
            if (id.longValue()==deviceDefine.getProductionLineId().longValue()){
                return  false;
            }
        }
        return  true;
    }

    /**
     *
     * @param conditionList
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/searchDeviceDefine", method = RequestMethod.GET)
    public Object searchDeviceDefine(String conditionList, @PageableDefault(value = 20, direction = Sort.Direction.DESC) Pageable pageable) {

        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(conditionList)){
            map.put("error","条件为空请重新输入条件");
            return map;
        }
        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);
        List<DeviceDefine> deviceDefines = null;
        try {
            deviceDefines = deviceDefineService.searchDeviceDefine(newSearchConditions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("materialType***:"+deviceDefines);
        MyPageable myPageable = new MyPageable(pageable.getPageSize() ,pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(), deviceDefines);
        return myPageable;
    }
}
