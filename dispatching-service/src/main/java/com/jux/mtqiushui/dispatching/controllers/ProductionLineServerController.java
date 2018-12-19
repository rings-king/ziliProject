package com.jux.mtqiushui.dispatching.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.dispatching.model.ProductionLine;
import com.jux.mtqiushui.dispatching.model.SearchCondition;
import com.jux.mtqiushui.dispatching.services.ProductionLineService;
import com.jux.mtqiushui.dispatching.util.MyPageable;
import com.jux.mtqiushui.dispatching.util.ObjectIsNull;
import com.jux.mtqiushui.dispatching.util.SearchFarmat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.lang.*;
import java.util.*;

/**
 * 产线定义
 */
@RestController
@RequestMapping(value = "v1/processline")
public class ProductionLineServerController {

    @Autowired
    private ProductionLineService productionLineService;
    @Autowired
    private DeviceDefineServiceController deviceDefineServiceController;

    //添加产线以及附带的工位
    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> addProductionLine(@RequestBody String param) {
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的物料定义对象
        Object jsonProductionLine = jsonMap.get("addnew");
        ProductionLine productionLine = JSONObject.toJavaObject((JSON) jsonProductionLine, ProductionLine.class);
        boolean allFieldNull = false;
        try {
            allFieldNull = ObjectIsNull.isAllFieldNull(productionLine);
        } catch (Exception e) {
            map.put("error", "解析异常");
            return map;
        }
        //判断要添加的产线定义以及工位是否为空
        if (allFieldNull == true) {
            map.put("error", "添加内容不能为空");
            return map;
        }
        ProductionLine productionLine1 = null;
        try {
            productionLine1 = productionLineService.addProductionLine(productionLine);
            if (productionLine1 == null) {
                map.put("error", "添加异常");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        map.put("success", true);
        return map;
    }

    //查询所有以及所属工位集合
    @RequestMapping(method = RequestMethod.GET)
    public Object findAllProductionLine(@PageableDefault(sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Pageable newPageble = new PageRequest(pageable.getPageNumber() < 1 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        Page<ProductionLine> productionLines = null;
        try {
            productionLines = productionLineService.findAllProductionLine(newPageble);
        } catch (Exception e) {
            Map<Object, Object> map = new HashMap<>();
            map.put("error", "服务器错误");
            return map;
        }
        return productionLines;
    }

    //根据id查询产线定义以及对应的工位集合
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Object findProductionLingById(@PathVariable("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        if (id == null) {
            map.put("error", "id不能为空");
            return map;
        }
        ProductionLine productionLine = null;
        try {
            productionLine = productionLineService.findProductionLineById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (productionLine == null) {
            map.put("error", "产线内容信息不存在");
            return map;
        }
        return productionLine;
    }

    //根据id删除整个产线以及所属工位
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public Object deleteProductionLineById(@PathVariable("id") Long id) {
        Map<String, Object> map = new HashMap<>();
        if (id == null) {
            map.put("error", "id不能为空");
            return map;
        }
        //先根据id查询设备定义是否关联了产线名称
        Boolean aBoolean = deviceDefineServiceController.selectDeviceLineById(id);
        if (aBoolean == false) {
            map.put("error", "无法删除,此产线与设备关联");
            return map;
        }
        ProductionLine productionLineById = null;
        try {
            productionLineService.deleteProductionLineById(id);
            productionLineById = productionLineService.findProductionLineById(id);
            if (productionLineById != null) {
                map.put("error", "删除失败");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        map.put("success", true);
        return map;
    }

    //修改产线/工位的增删改
    @RequestMapping(method = RequestMethod.PUT)
    public Map<String, Object> modifyProductionLine(@RequestBody String param) {
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 获取系统当前时间
        Date time = Calendar.getInstance().getTime();
        // 从map集合中获取产线定义的id
        Integer productionLineId = (Integer) jsonMap.get("id");
        //根据id查询对应的产线定义以及所属工位
        ProductionLine productionLine = null;
        try {
            productionLine = productionLineService.findProductionLineById(productionLineId.longValue());
            if (productionLine == null) {
                map.put("error", "不存在此产线信息");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        //将工位新增转为object
        Object addnew = jsonMap.get("addnew");
        //object转为工位对象
        ProductionLine addproductionLine = JSONObject.toJavaObject((JSON) addnew, ProductionLine.class);
        //设置最新时间
        addproductionLine.setUpdateTime(time);
        System.out.println("要添加的工位****" + addproductionLine);
        //将工位新增转为object
        Object update = jsonMap.get("update");
        //object转为工位对象
        ProductionLine updateproductionLine = JSONObject.toJavaObject((JSON) update, ProductionLine.class);
        //设置最新时间
        updateproductionLine.setUpdateTime(time);
        System.out.println("要修改的工位****" + updateproductionLine);
        //将工位新增转为object
        Object remove = jsonMap.get("remove");
        //object转为工位对象
        ProductionLine removeproductionLine = JSONObject.toJavaObject((JSON) remove, ProductionLine.class);
        //设置最新时间
        removeproductionLine.setUpdateTime(time);
        System.out.println("要删除的工位****" + removeproductionLine);
        List<ProductionLine> productionLines = new ArrayList<>();
        productionLines.add(addproductionLine);
        productionLines.add(removeproductionLine);
        productionLines.add(updateproductionLine);
        Boolean bn = false;
        try {
            bn = productionLineService.modifyProductionLines(productionLines, productionLine);
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
     * 搜索产线
     */
    @RequestMapping(value = "/searchProductionLine", method = RequestMethod.GET)
    public Object searchProductionLine(String conditionList, @PageableDefault(direction = Sort.Direction.DESC, value = 20) Pageable pageable) {
        Map map = new HashMap();
        if (StringUtils.isEmpty(conditionList)){
            map.put("error","条件为空请重新输入条件");
            return map;
        }
        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);
        List<ProductionLine> productionLine = null;
        if (newSearchConditions.size() != 0) {
            try {
                productionLine = productionLineService.findProductionLine(newSearchConditions);
                if (productionLine.size()!=0) {
                    System.out.println("page:" + pageable.getPageNumber());
                    System.out.println("size:" + pageable.getPageSize());
                    MyPageable myPageable = new MyPageable(pageable.getPageSize() ,pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(),productionLine);
                    return myPageable;
                }
            } catch (Exception e) {
                map.put("error", "服务器错误");
                return map;
            }
        }
        map.put("error", "没有符合条件的信息");
        return map;
    }
}