package com.jux.mtqiushui.resources.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.resources.model.MeasurementUnit;
import com.jux.mtqiushui.resources.model.SearchCondition;
import com.jux.mtqiushui.resources.services.MeasurementUnitService;
import com.jux.mtqiushui.resources.util.MyPageable;
import com.jux.mtqiushui.resources.util.SearchFarmat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "v1/measurementUnit")
public class MeasurementUnitServiceController {

    @Autowired
    private MeasurementUnitService measurementUnitService;

    /**
     * 返回所有计量单位信息 支持分页
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object getAllMeasurementUnit(@PageableDefault(value = 99999, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MeasurementUnit> measurementUnits;
        try {
            measurementUnits = measurementUnitService.getAllMeasurementUnit(pageable);
        } catch (Exception e) {
            Map<Object, Object> map = new HashMap<>();
            map.put("error","服务器错误");
            return map;
        }
        return measurementUnits;
    }

    /**
     * 根据计量单位id查询对应的信息
     * @param measurementUnitId
     * @return
     */
    @RequestMapping(value = "/{measurementUnitId}", method = RequestMethod.GET)
    public Object getMeasurementUnitById(@PathVariable("measurementUnitId") Long measurementUnitId) {
        Map<Object, Object> map = new HashMap<>();
        if (measurementUnitId == null) {
            map.put("error","id不能为空");
            return map;
        }
        MeasurementUnit resultMeasurementUnit = null;
        try {
            resultMeasurementUnit = measurementUnitService.getMeasurementUnitById(measurementUnitId);
            if (resultMeasurementUnit == null) {
                map.put("error", "没有查到该计量单位信息");
                return map;
            }
        } catch (Exception e) {
           map.put("error", "服务器错误");
           return map;
        }

        return resultMeasurementUnit;
    }

    /**
     * 添加计量单位
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> addMeasurementUnit(@RequestBody String param) {
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的计量单位对象
        Object jsonMeasurementUnit = jsonMap.get("addnew");
        // 把JSONObject的计量单位对象转成JavaBean的计量单位对象
        MeasurementUnit measurementUnit = JSONObject.toJavaObject((JSON) jsonMeasurementUnit, MeasurementUnit.class);
        if (measurementUnit == null) {
            map.put("error", "计量单位信息不能为空");
            return map;
        }

        MeasurementUnit resultMeasurementUnit = null;
        try {
            MeasurementUnit measurementUnitByCode = measurementUnitService.getMeasurementUnitByCode(measurementUnit.getMeasureUnitCode());
            if (measurementUnitByCode != null) {
                map.put("error", "该计量单位编码已存在，请重新添加");
                return map;
            }
            measurementUnit.setUpdateTime(Calendar.getInstance().getTime());
            resultMeasurementUnit = measurementUnitService.addMeasurementUnit(measurementUnit);
        } catch (Exception e) {
            map.put("error", "服务器错误，添加计量单位失败");
            return map;
        }

        if (resultMeasurementUnit == null) {
            map.put("error", "添加失败");
            return map;
        }

        map.put("success", "true");
        return map;
    }

    /**
     * 根据id修改计量单位信息
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Map<Object, Object> modifyMeasurementUnitById(@RequestBody String param) {
        Map<Object, Object> map = new HashMap<>();

        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的计量单位对象
        Object jsonMeasurementUnit = jsonMap.get("update");
        // 把JSONObject的计量单位对象转成JavaBean的计量单位对象
        MeasurementUnit measurementUnit = JSONObject.toJavaObject((JSON) jsonMeasurementUnit, MeasurementUnit.class);
         if (measurementUnit == null) {
             map.put("error", "内容不能为空");
             return map;
         }
        MeasurementUnit resultMeasurementUnit = null;
        try {
            resultMeasurementUnit = measurementUnitService.getMeasurementUnitById(measurementUnit.getId());
            if (resultMeasurementUnit == null) {
                map.put("error", "修改失败,该计量单位不存在");
                return map;
            }
            if (measurementUnit.getMeasureUnitName() != null) {
                resultMeasurementUnit.setMeasureUnitName(measurementUnit.getMeasureUnitName());
            }
            resultMeasurementUnit.setUpdateTime(Calendar.getInstance().getTime());
            measurementUnitService.addMeasurementUnit(resultMeasurementUnit);
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        map.put("success",true);
        return map;
    }

    /**
     * 根据id删除对应的计量单位
     * @param measurementUnitId
     * @return
     */
    @RequestMapping(value = "/{measurementUnitId}", method = RequestMethod.DELETE)
    public Map<Object, Object> deleteMeasurementUnitById(@PathVariable("measurementUnitId") Long measurementUnitId) {
        Map<Object, Object> map = new HashMap<>();
        if (measurementUnitId == null) {
            map.put("error","id不能为空");
            return map;
        }

        MeasurementUnit resultMeasurementUnit = null;
        try {
            resultMeasurementUnit = measurementUnitService.getMeasurementUnitById(measurementUnitId);
            if (resultMeasurementUnit == null) {
                map.put("error", "删除失败,该计量单位不存在");
                return map;
            }
            measurementUnitService.deleteMeasurementUnitById(measurementUnitId);

        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }

        map.put("success", "true");
        return map;
    }

    /**
     * 多条件查询
     * @param conditionList
     * @return
     */
    @RequestMapping(value = "/searchMeasurementUnit", method = RequestMethod.GET)
    public Object searchMeasurementUnit(String conditionList, @PageableDefault(direction = Sort.Direction.DESC, value = 20) Pageable pageable) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(conditionList)){
            map.put("error","条件为空请重新输入条件");
            return map;
        }
        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);
        List<MeasurementUnit> measurementUnit = null;
        try {
            measurementUnit = measurementUnitService.getMeasurementUnit(newSearchConditions);
        } catch (Exception e) {
            System.out.println("***********:"+e.getMessage());
            map.put("error", "服务器错误");
            return map;
        }
        System.out.println("measurementUnit***:"+measurementUnit);
        MyPageable myPageable = new MyPageable(pageable.getPageSize() ,pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(),measurementUnit);
        return myPageable;
    }

    /**
     * 根据计量单位id返回该计量单位的名字
     * @param measurementUnitId
     * @return
     */
    @RequestMapping(value = "/getMeasurementUnitNameById/{measurementUnitId}", method = RequestMethod.GET)
    public String getMeasurementUnitNameById(@PathVariable("measurementUnitId") Long measurementUnitId) {
        MeasurementUnit measurementUnitById = null;
        try {
            measurementUnitById = measurementUnitService.getMeasurementUnitById(measurementUnitId);

        } catch (Exception e) {
            return null;
        }
        if (measurementUnitById != null) {
            return measurementUnitById.getMeasureUnitName();
        }
        return null;
    }

    /**
     * 根据计量单位名称查询对应的计量单位对象
     * @param unitName
     * @return
     */
    @RequestMapping(value = "/findByMeasureUnitName/{unitName}", method = RequestMethod.GET)
    public Long findByMeasureUnitName(@PathVariable("unitName") String unitName) {
        Long id = null;
        try {
            id = measurementUnitService.getMeasurementUnitIdByUnitName(unitName);
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage());
        }
        return id;
    }
}
