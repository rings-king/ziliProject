package com.jux.mtqiushui.resources.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.resources.model.*;
import com.jux.mtqiushui.resources.repository.AuthServiceApi;
import com.jux.mtqiushui.resources.services.MaterialDefineService;
import com.jux.mtqiushui.resources.services.MaterialTypeService;
import com.jux.mtqiushui.resources.services.MeasurementUnitService;
import com.jux.mtqiushui.resources.util.MyPageable;
import com.jux.mtqiushui.resources.util.ObjectIsNull;
import com.jux.mtqiushui.resources.util.SearchFarmat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping(value = "v1/materialDefine")
public class MaterialDefineServiceController {

    @Autowired
    private MaterialDefineService materialDefineService;

    @Autowired
    private MeasurementUnitService measurementUnitService;

    @Autowired
    private MaterialTypeService materialTypeService;

    @Autowired
    private AuthServiceApi userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加物料定义
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> addMaterialDefine(@RequestBody String param) {
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的物料定义对象
        Object jsonMaterialDefine = jsonMap.get("addnew");
        // 把JSONObject的物料定义对象转成JavaBean的物料定义对象
        MaterialDefine materialDefine = JSONObject.toJavaObject((JSON) jsonMaterialDefine, MaterialDefine.class);
        boolean allFieldNull = false;
        try {
            allFieldNull = ObjectIsNull.isAllFieldNull(materialDefine);
        } catch (Exception e) {
            map.put("error", "解析异常");
            return map;
        }

        if (allFieldNull == true) {
            map.put("error", "添加内容不能为空");
            return map;
        }

        Long unitId = materialDefine.getUnitId();
        if (unitId == null) {
            map.put("error", "主单位不能为空");
            return map;
        }

        String materialTypeId = materialDefine.getMaterialTypeId();
        if (materialTypeId == null) {
            map.put("error", "所属分类不能为空");
            return map;
        }

        try {
            MeasurementUnit measurementUnitById = measurementUnitService.getMeasurementUnitById(unitId);
            if (measurementUnitById == null) {
                map.put("error", "该主单位不存在");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "添加异常，请重新添加");
            return map;
        }

        try {
            MaterialType materialType = materialTypeService.getMaterialByMaterialTypeId(materialTypeId);
            if (materialType == null) {
                map.put("error", "该所属分类不存在");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "添加异常，请重新添加");
            return map;
        }

        try {
            materialDefine.setUpdateTime(Calendar.getInstance().getTime());
            MaterialDefine result = materialDefineService.addMaterialDefine(materialDefine);
            if (result == null) {
                map.put("error", "添加失败");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "添加异常，请重新添加");
            return map;
        }

        map.put("success","true");
        return map;
    }

    /**
     * 修改物料定义
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Map<Object, Object> modifyMaterialDefine(@RequestBody String param) {
        Map<Object, Object> map = new HashMap<>();

        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;

        // 获取系统当前时间
        Date time = Calendar.getInstance().getTime();

        // 从map集合中获取物料定义的id
        Integer materialDefineId = (Integer) jsonMap.get("id");

        // 从map集合中获取addnew对应要添加的物料定义对象
        Object addJsonMaterialDefine = jsonMap.get("addnew");
        // 把JSONObject的物料定义对象转成JavaBean的物料定义对象
        MaterialDefine addMaterialDefine = JSONObject.toJavaObject((JSON) addJsonMaterialDefine, MaterialDefine.class);
        addMaterialDefine.setUpdateTime(time);
        System.out.println("添加："+addMaterialDefine);

        // 从map集合中获取update对应要更新的物料定义对象
        Object updateJsonMaterialDefine = jsonMap.get("update");
        // 把JSONObject的物料定义对象转成JavaBean的物料定义对象
        MaterialDefine updateMaterialDefine = JSONObject.toJavaObject((JSON) updateJsonMaterialDefine, MaterialDefine.class);
        updateMaterialDefine.setUpdateTime(time);
        System.out.println("修改:"+updateMaterialDefine);

        // 从map集合中获取remove对应要删除的物料定义对象
        Object deleteJsonMaterialDefine = jsonMap.get("remove");
        // 把JSONObject的物料定义对象转成JavaBean的物料定义对象
        MaterialDefine deleteMaterialDefine = JSONObject.toJavaObject((JSON) deleteJsonMaterialDefine, MaterialDefine.class);
        deleteMaterialDefine.setUpdateTime(time);
        System.out.println("删除:"+deleteMaterialDefine);

        List<MaterialDefine> listMaterialDefine = new ArrayList<>();
        listMaterialDefine.add(addMaterialDefine);
        listMaterialDefine.add(deleteMaterialDefine);
        listMaterialDefine.add(updateMaterialDefine);

        Boolean restltBoolean = false;
        try {
            restltBoolean = materialDefineService.modifyMaterialDefine(listMaterialDefine, materialDefineId.longValue());
        } catch (Exception e) {
            map.put("error", "修改失败,服务器错误");
            return map;
        }
        if (restltBoolean == true) {
            map.put("success", "true");
            return map;
        }
        map.put("error", "服务器错误");
        return map;
    }

    /**
     * 根据物料定义Id删除对应的物料定义信息以及子表的信息
     * @param materialDefineId
     * @return
     */
    @RequestMapping(value = "/{materialDefineId}",method = RequestMethod.DELETE)
    public Map<Object, Object> deleteMaterialDefineById(@PathVariable("materialDefineId") Long materialDefineId) {
        Map<Object, Object> map = new HashMap<>();
        if (materialDefineId == null) {
            map.put("error","内容不能为空");
            return map;
        }
        MaterialDefine materialDefineById = null;
        try {
            materialDefineById = materialDefineService.getMaterialDefineById(materialDefineId);
            if (materialDefineById == null) {
                map.put("error", "删除失败,该物料定义不存在");
                return map;
            }
            materialDefineService.deleteMaterialDefineById(materialDefineId);
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }

        map.put("success", "true");
        return map;
    }

    /**
     * 根据id返回对应的物料定义信息以及该物料对应的下级物料定义和下级物料参数
     * @param materidlDefineId
     * @return
     */
    @RequestMapping(value = "/{materidlDefineId}", method = RequestMethod.GET)
    public Object getMaterialDefineById(@PathVariable("materidlDefineId") Long materidlDefineId) {
        Map<Object, Object> map = new HashMap<>();

        if (materidlDefineId == null) {
            map.put("error", "信息不能为空");
            return map;
        }

        MaterialDefine findResult = null;
        try {
            findResult = materialDefineService.getMaterialDefineById(materidlDefineId);
        } catch (Exception e) {
            map.put("error", "服务器异常");
            return map;
        }

        if (findResult == null) {
            map.put("error", "没有查到对应的物料定义信息");
            return map;
        }
        // 获取主单位id
        Long unitId = findResult.getUnitId();
        // 获取所属分类id
        String materialTypeId = findResult.getMaterialTypeId();
        if (unitId != null) {
            try {
                MeasurementUnit measurementUnitById = measurementUnitService.getMeasurementUnitById(unitId);
                if (measurementUnitById != null) {
                    findResult.setUnitName(measurementUnitById.getMeasureUnitName());
                }
            } catch (Exception e) {
                map.put("error", "服务器异常");
                return map;
            }
        }

        if (materialTypeId != null) {
            try {
                MaterialType materialType = materialTypeService.getMaterialByMaterialTypeId(materialTypeId);
                if (materialType != null) {
                    findResult.setMaterialTypeName(materialType.getMaterialTypeName());
                }
            } catch (Exception e) {
                map.put("error", "服务器异常");
                return map;
            }
        }
        return findResult;
    }
    /**
     * 返回所有物料定义 降序输出
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object getAllMaterialDefine(@PageableDefault(value = 99999, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<MaterialDefine> materialDefines;
        try {
            materialDefines = materialDefineService.getAllMaterialDefine(pageable);
        } catch (Exception e) {
            Map<Object, Object> map = new HashMap<>();
            map.put("error","服务器错误");
            return map;
        }

//        if (materialDefines != null) {
//            for (MaterialDefine materialDefine : materialDefines) {
//                Long unitId = materialDefine.getUnitId();
//                String materialTypeId = materialDefine.getMaterialTypeId();
//                if (unitId != null) {
//                    try {
//                        MeasurementUnit measurementUnitById = measurementUnitService.getMeasurementUnitById(unitId);
//                        if (measurementUnitById != null) {
//                            materialDefine.setUnitName(measurementUnitById.getMeasureUnitName());
//                        }
//                    } catch (Exception e) {
//                        Map<Object, Object> map = new HashMap<>();
//                        map.put("error", "服务器异常");
//                        return map;
//                    }
//                }
//
//                if (materialTypeId != null) {
//                    try {
//                        MaterialType materialType = materialTypeService.getMaterialByMaterialTypeId(materialTypeId);
//                        if (materialType != null) {
//                            materialDefine.setMaterialTypeName(materialType.getMaterialTypeName());
//                        }
//                    } catch (Exception e) {
//                        Map<Object, Object> map = new HashMap<>();
//                        map.put("error", "服务器异常");
//                        return map;
//                    }
//                }
//
//            }
//        }

        return materialDefines;
    }

    /**
     * 提供给工艺流程 显示产品名和规格型号的接口
     * @param materialDefineId
     * @return
     */
    @RequestMapping(value = "/getMaterialNameAndMaterialModelById/{materialDefineId}", method = RequestMethod.GET)
    public Map<String, String> getMaterialNameAndMaterialModelById(@PathVariable("materialDefineId") Long materialDefineId) {
        Map<String, String> map = new HashMap<>();

        MaterialDefine result = null;
        try {
            result = materialDefineService.getMaterialDefineById(materialDefineId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (result == null) {
            return null;
        }

        map.put("materialName", result.getMaterialName());
        map.put("materialModel",result.getMaterialModel());
        return map;
    }

    @RequestMapping(value = "/searchMaterialDefine" ,method = RequestMethod.GET)
    public Object searchMaterialDefine(String conditionList, @PageableDefault(value = 20,direction = Sort.Direction.DESC) Pageable pageable) {
        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);

        List<MaterialDefine> materialDefines = null;
        try {
            materialDefines = materialDefineService.searchMaterialDefine(newSearchConditions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MyPageable myPageable = new MyPageable(pageable.getPageSize() ,pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(),materialDefines);
        return myPageable;
    }
    /**
     * 提供工艺流程 查询产品和规则型号接口
     */
    @RequestMapping(value = "/getMaterialIdAndModel",method = RequestMethod.POST)
    public List<MaterialDefine> getMaterialIdAndModel( @RequestBody List<SearchCondition> conditionList){
        String sql="";
        for (int i = 0; i < conditionList.size(); i++) {
            sql = sql + SearchFarmat.getNew(conditionList.get(i));
            if (i < conditionList.size() - 1) {
                sql = sql + " and ";
            }
        }
        if (sql.contains("processProductionId")){
            sql=sql.replace("processProductionId","id");
        }
        if (sql.contains("materialId")){
            sql=sql.replace("materialId","id");
        }
        if (sql.contains("materialModel")){
            sql=sql.replace("materialModel","material_model");
        }
        String maSQl="select id,material_model,material_name from material_define as t where "+sql;
        List<MaterialDefine> query = jdbcTemplate.query(maSQl, new RowMapper<MaterialDefine>() {
            @Override
            public MaterialDefine mapRow(ResultSet resultSet, int i) throws SQLException {
                MaterialDefine materialDefine = new MaterialDefine();
                materialDefine.setId(resultSet.getLong("id"));
                materialDefine.setMaterialModel(resultSet.getString("material_model"));
                materialDefine.setMaterialName(resultSet.getString("material_name"));
                return materialDefine;
            }
        });
        return  query;
    }

    /**
     * 根据物料编码返回物料名称 规格型号 主单位
     * @param materialCode
     * @return
     */
    @RequestMapping(value = "/getMaterialByMaterialCode/{materialCode}", method = RequestMethod.GET)
    public Map<String, String> getMaterialByMaterialCode(@PathVariable("materialCode") String materialCode) {
        MaterialDefine materialByMaterialCode = materialDefineService.getMaterialByMaterialCode(materialCode);
        if (materialByMaterialCode == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        map.put("materialName", materialByMaterialCode.getMaterialName());
        map.put("materialModel", materialByMaterialCode.getMaterialModel());
        if (materialByMaterialCode.getUnitId() != null) {
            try {
                MeasurementUnit measurementUnitById = measurementUnitService.getMeasurementUnitById(materialByMaterialCode.getUnitId());
                if (measurementUnitById.getMeasureUnitName() != null) {
                    map.put("unitName", measurementUnitById.getMeasureUnitName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return map;
    }

    /**
     * 根据时间间隔统计不同类型的产量对比
     * @param analyses
     * @return
     */
    @RequestMapping(value = "/statistic", method = RequestMethod.POST)
    public List<Analysis> getMaterialTypeNameByMaterialTypeId(@RequestBody List<Analysis> analyses) {
        if (analyses == null) {
            return analyses;
        }
        return materialDefineService.getMaterialTypeNameByMaterialTypeId(analyses);
    }

    /**
     * 根据物料id获得类别码
     * @param id
     * @return
     */
    @RequestMapping(value = "/getByNum/{id}",method = RequestMethod.GET)
    public Map<String,String> getByNum(@PathVariable("id") Long id){
        Map<String,String> map=new HashMap<>();
            String sql = "SELECT\n" +
                    "\tmt.material_type_name\n" +
                    "FROM\n" +
                    "\tmaterial_define AS md,\n" +
                    "\tmaterial_type AS mt\n" +
                    "WHERE\n" +
                    "\tmd.id = \n" + id + "\n" +
                    "AND md.material_type_id = mt.material_type_id";
        System.out.println(sql);
            List<MaterialType> material_type_name = jdbcTemplate.query(sql, new RowMapper<MaterialType>() {
                public MaterialType mapRow(ResultSet resultSet, int i) throws SQLException {
                    MaterialType materialType = new MaterialType();
                    materialType.setMaterialTypeName(resultSet.getString("material_type_name"));
                    return materialType;
                }
            });
            if (material_type_name.size() != 0) {
                map.put("materialTypeName", material_type_name.get(0).getMaterialTypeName());
                return map;
            }
        map.put("materialTypeName",null);
        return  map;
    }
}
