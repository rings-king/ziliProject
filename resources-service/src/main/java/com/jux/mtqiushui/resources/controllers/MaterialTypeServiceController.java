package com.jux.mtqiushui.resources.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.resources.model.MaterialType;
import com.jux.mtqiushui.resources.model.SearchCondition;
import com.jux.mtqiushui.resources.repository.AuthServiceApi;
import com.jux.mtqiushui.resources.services.MaterialTypeService;
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
@RequestMapping(value = "v1/materialType")
public class MaterialTypeServiceController {

    @Autowired
    private MaterialTypeService materialTypeService;

    @Autowired
    private AuthServiceApi userService;

    /**
     * 添加物料分类
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> addMaterialType(@RequestBody String param) {
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的物料分类对象
        Object jsonMaterialType = jsonMap.get("addnew");
        // 把JSONObject的物料分类对象转成JavaBean的物料分类对象
        MaterialType materialType = JSONObject.toJavaObject((JSON) jsonMaterialType, MaterialType.class);
        if (materialType == null) {
            map.put("error","内容不能为空");
            return map;
        }

        try {
            MaterialType materialTypeById = materialTypeService.getMaterialTypeById(materialType.getMaterialTypeId());

            if (materialTypeById != null) {
                map.put("error","添加失败,该类别编码已存在");
                return map;
            }

            if (materialType.getParentId() != null) {
                // 根据所属类别Id查询对应的所属类别信息
                MaterialType findMaterialType = materialTypeService.getMaterialType(materialType.getParentId());
                if (findMaterialType == null) {
                    map.put("error","添加失败,所属类别不存在");
                    return map;
                } else {
                    if (materialType.getMaterialTypeId().equals(findMaterialType.getMaterialTypeId())) {
                        map.put("error","添加失败,类别编码与所属类别重复");
                        return map;
                    }
                }

            }
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        materialType.setUpdateTime(Calendar.getInstance().getTime());
        MaterialType newMaterialType = materialTypeService.addMaterialType(materialType);
        if (newMaterialType == null) {
            map.put("error","添加物料分类失败");
            return map;
        }

        map.put("success",true);
        return map;
    }

    /**
     * 查询所有物料分类,支持分页功能
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object getAllMaterialType(@PageableDefault(value = 99999, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MaterialType> materialTypes;
        try {
            materialTypes = materialTypeService.getAllMaterialType(pageable);
            if (materialTypes != null) {
                for (MaterialType materialType : materialTypes) {
                    Long parentId = materialType.getParentId();
                    if (parentId != null) {
                        MaterialType materialType1 = materialTypeService.getMaterialType(parentId);
                        if (materialType1 != null) {
                            materialType.setParentName(materialType1.getMaterialTypeName());
                        }
                    }
                }
            }
            return materialTypes;
        } catch (Exception e) {
            Map<Object, Object> map = new HashMap<>();
            map.put("error","服务器错误");
            return map;
        }
    }

    /**
     * 修改物料分类信息
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Map<Object, Object> modifyMaterialTypeById(@RequestBody String param) {
        Map<Object, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的物料分类对象
        Object jsonMaterialType = jsonMap.get("update");
        // 把JSONObject的物料分类对象转成JavaBean的物料分类对象
        MaterialType materialType = JSONObject.toJavaObject((JSON) jsonMaterialType, MaterialType.class);

        if (materialType == null) {
            map.put("error","内容不能为空");
            return map;
        }

        try {
            MaterialType materialTypeById = materialTypeService.getMaterialType(materialType.getId());
            if (materialTypeById == null) {
                map.put("error","修改失败,该物料分类不存在");
                return map;
            }
            if (materialType.getMaterialTypeName() != null) {
                materialTypeById.setMaterialTypeName(materialType.getMaterialTypeName());
            }
            if (materialType.getParentId() != null) {
                // 根据所属类别Id查询对应的所属类别信息
                MaterialType findMaterialType = materialTypeService.getMaterialType(materialType.getParentId());
                if (findMaterialType == null) {
                    map.put("error","添加失败,所属类别不存在");
                    return map;
                } else {
                    if (materialTypeById.getMaterialTypeId().equals(findMaterialType.getMaterialTypeId())) {
                        map.put("error","添加失败,类别编码与所属类别重复");
                        return map;
                    } else {
                        materialTypeById.setParentId(materialType.getParentId());
                    }
                }
            }
            materialTypeById.setUpdateTime(Calendar.getInstance().getTime());
            System.out.println("修改后:"+materialTypeById);
            materialTypeService.modifyMaterialTypeById(materialTypeById);
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }

        map.put("success",true);
        return map;
    }

    /**
     * 删除物料分类及该分类下所有的子类
     * @param materialId
     * @return
     */
    @RequestMapping(value = "/{materialId}", method = RequestMethod.DELETE)
    public Map<Object, Object> deleteMaterialTypeById(@PathVariable("materialId") Long materialId) {
        Map<Object, Object> map = new HashMap<>();
        if (materialId == null) {
            map.put("error","内容不能为空");
            return map;
        }

        try {
            // 根据类别编码寻找该对象

            MaterialType materialTypeById = materialTypeService.getMaterialType(materialId);
            if (materialTypeById == null) {
                map.put("error","删除失败,该物料分类不存在");
                return map;
            }
            // 如果对象不为空 根据对象的类别编码删除所有parentId为该类别编码的物料分类
            // 1.删除子物料分类
            materialTypeService.deleteMaterialTypeByParentId(materialId);
            // 2.删除本物料分类
            materialTypeService.deleteMaterialTypeByTypeId(materialTypeById.getMaterialTypeId());
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        map.put("success", true);
        return map;
    }

    /**
     * 多条件查询
     * @param conditionList
     * @return
     */
    @RequestMapping(value = "/searchMaterialType", method = RequestMethod.GET)
    public Object searchMaterialType(String conditionList, @PageableDefault(value = 20, direction = Sort.Direction.DESC) Pageable pageable) {

        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(conditionList)){
            map.put("error","条件为空请重新输入条件");
            return map;
        }
        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);
        List<MaterialType> materialType = null;
        try {
            materialType = materialTypeService.getMaterialType(newSearchConditions);
        } catch (Exception e) {
            System.out.println("e"+e.getMessage());
        }
        System.out.println("materialType***:"+materialType);
        MyPageable myPageable = new MyPageable(pageable.getPageSize() ,pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(),materialType);
        return myPageable;
    }

    /**
     * 根据物料分类ID查询对应的信息
     * @param materialId
     * @return
     */
    @RequestMapping(value = "/{materialId}", method = RequestMethod.GET)
    public Object getMaterialTypeByTypeId(@PathVariable("materialId") Long materialId) {
        Map<Object, Object> map = new HashMap<>();
        // 判断物料类别是否为空或者是空的字符串
        if (materialId == null) {
            map.put("error", "类别编号不能为空");
            return map;
        }
        try {
            MaterialType materialTypeById = materialTypeService.getMaterialType(materialId);
            Long parentId = materialTypeById.getParentId();
            if (parentId != null) {
                MaterialType materialType = materialTypeService.getMaterialType(parentId);
                if (materialType != null) {
                    materialTypeById.setParentName(materialType.getMaterialTypeName());
                }
            }
            return materialTypeById;
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
    }

    /**
     * 根据物料分类名称查询对应的ID
     * @param typeName
     * @return
     */
    @RequestMapping(value = "/getMaterialTypeIdByName/{typeName}", method = RequestMethod.GET)
    public Long getMaterialTypeIdByName(@PathVariable("typeName") String typeName) {
        Long id = null;
        try {
            id = materialTypeService.getMaterialTypeIdByTypeName(typeName);
        } catch (Exception e) {
            System.out.println("error:"+e.getMessage());
        }
        return id;
    }
}
