package com.jux.mtqiushui.resources.services;

import com.jux.mtqiushui.resources.model.*;
import com.jux.mtqiushui.resources.repository.*;
import com.jux.mtqiushui.resources.util.ObjectIsNull;
import com.jux.mtqiushui.resources.util.SearchFarmat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class MaterialDefineService {

    @Autowired
    private MaterialDefineRepository materialDefineRepository;
    @Autowired
    private JuniorMaterialRepository juniorMaterialRepository;
    @Autowired
    private MaterialParameterRepository materialParameterRepository;

    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;
    @Autowired
    private MaterialTypeRepository materialTypeRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加下级物料
     * @param materialParameter
     * @return
     * @throws Exception
     */
    @Transactional
    public MaterialParameter addMaterialParameter(MaterialParameter materialParameter) throws Exception {
        MaterialParameter save = materialParameterRepository.save(materialParameter);
        return save;
    }
    @Transactional
    public JuniorMaterial addJuniorMaterial(JuniorMaterial juniorMaterial) throws Exception {
        JuniorMaterial save = juniorMaterialRepository.save(juniorMaterial);
        return save;
    }

    /**
     * 添加物料定义 同时添加下级物料以及下级物料参数
     * @param materialDefine
     * @return
     */
    @Transactional
    public MaterialDefine addMaterialDefine(MaterialDefine materialDefine) throws Exception {

        Set<MaterialParameter> materialParameters = materialDefine.getMaterialParamsTab1();
        if (materialParameters != null) {
            for (MaterialParameter materialParameter : materialParameters) {
                materialParameter.setMaterialDefine(materialDefine);
            }
        }

        Set<JuniorMaterial> juniorMaterials = materialDefine.getMaterialParamsTab2();
        if (juniorMaterials != null) {
            for (JuniorMaterial juniorMaterial : juniorMaterials) {
                juniorMaterial.setMaterialDefine(materialDefine);
            }
        }
        MaterialDefine save = materialDefineRepository.save(materialDefine);
        return save;
    }

    /**
     * 根据id返回对应的物料定义信息以及该物料对应的下级物料定义和下级物料参数
     * @param materialId
     * @return
     * @throws Exception
     */
    public MaterialDefine getMaterialDefineById(Long materialId) throws Exception {
        MaterialDefine one = materialDefineRepository.findOne(materialId);
        return one;
    }

    /**
     * 查询所有物料定义 支持分页功能
     * @param pageable
     * @return
     * @throws Exception
     */
    public Page<MaterialDefine> getAllMaterialDefine(Pageable pageable) throws Exception {
        Page<MaterialDefine> all = materialDefineRepository.findAll(pageable);
        return all;
    }

    /**
     * 根据物料定义Id删除对应的物料定义信息以及子表的信息
     * @param materialDefineId
     * @throws Exception
     */
    @Transactional
    public void deleteMaterialDefineById(Long materialDefineId) throws Exception {
        materialDefineRepository.delete(materialDefineId);
    }

    /**
     * 根据物料参数Id删除对应的物料参数
     * @param materialParamId
     * @throws Exception
     */
    @Transactional
    public void  deleteMaterialParamById(Long materialParamId) throws Exception {
        materialParameterRepository.delete(materialParamId);
    }

    /**
     * 根据下级物料Id删除对应的物料参数
     * @param juniorMaterialId
     * @throws Exception
     */
    @Transactional
    public void deleteJuniorMaterialService(Long juniorMaterialId) throws Exception {
        juniorMaterialRepository.delete(juniorMaterialId);
    }

    public List<MaterialDefine> searchMaterialDefine(List<SearchCondition> conditionList) throws Exception {

        if (conditionList.size() == 0){
            return null;
        }

        String aNew = " ";
        for (int i = 0; i < conditionList.size(); i++) {

            aNew += SearchFarmat.getNew(conditionList.get(i));

            if (i<conditionList.size() - 1){
                aNew += " and " ;
            }

        }
        if (aNew.contains("materialCode")) {
            aNew = aNew.replace("materialCode","material_code");
        }
        if (aNew.contains("materialModel")) {
            aNew = aNew.replace("materialModel","material_model");
        }
        if (aNew.contains("materialName")) {
            aNew = aNew.replace("materialName","material_name");
        }
        if (aNew.contains("unitId")) {
            aNew = aNew.replace("unitId","unit_id");
        }
        if (aNew.contains("whName")) {
            aNew = aNew.replace("whName","wh_name");
        }
        if (aNew.contains("techWarCode")) {
            aNew = aNew.replace("techWarCode","tech_war_code");
        }
        if (aNew.contains("checkType")) {
            aNew = aNew.replace("checkType","check_type");
        }
        if (aNew.contains("drawingNo")) {
            aNew = aNew.replace("drawingNo","drawing_no");
        }
        if (aNew.contains("materialComment")) {
            aNew = aNew.replace("materialComment","material_comment");
        }
        if (aNew.contains("materialTypeId")) {
            aNew = aNew.replace("materialTypeId","material_type_id");
        }
        if (aNew.contains("materialParamsName")) {
            aNew = aNew.replace("materialParamsName","material_params_name");
        }
        if (aNew.contains("materialParamsValue")) {
            aNew = aNew.replace("materialParamsValue","material_params_value");
        }
        if (aNew.contains("materialChildCode")) {
            aNew = aNew.replace("materialChildCode","material_child_code");
        }
        if (aNew.contains("materialChildName")) {
            aNew = aNew.replace("materialChildName","material_child_name");
        }
        if (aNew.contains("material_child_count")) {
            aNew = aNew.replace("","");
        }
        if (aNew.contains("materialChildUnit")) {
            aNew = aNew.replace("materialChildUnit","material_child_unit");
        }
        if (aNew.contains("materialChildModel")) {
            aNew = aNew.replace("materialChildModel","material_child_model");
        }
        if (aNew.contains("materialSource")) {
            aNew = aNew.replace("materialSource","material_source");
        }
        if (aNew.contains("yuLiuZiDuan")) {
            aNew = aNew.replace("yuLiuZiDuan","yu_liu_zi_duan");
        }

        System.out.println("aNew:" + aNew);
        String sql = "SELECT  \n" +
                "  material_define.id,\n" +
                "  check_type,\n" +
                "  drawing_no,\n" +
                "  material_code,\n" +
                "  material_comment,\n" +
                "  material_model,\n" +
                "  material_name,\n" +
                "  material_source,\n" +
                "  material_type_id,\n" +
                "  material_type_name,\n" +
                "  tech_war_code,\n" +
                "  unit_id,\n" +
                "  unit_name,\n" +
                "  wh_name\n" +
                "  status\n" +
                "  yu_liu_zi_duan\n" +
                "FROM\n" +
                "  material_define INNER JOIN\n" +
                "  material_parameter ON\n" +
                "  material_define.id = material_parameter.material_define_id LEFT JOIN\n" +
                "  junior_material ON material_define.id = junior_material.material_define_id\n" +
                "WHERE\n" +
                aNew +" GROUP BY material_define.id order by material_define.update_time desc";
        System.out.println("sql:"+sql);
        List<MaterialDefine> query = jdbcTemplate.query(sql, new RowMapper<MaterialDefine>() {
            @Override
            public MaterialDefine mapRow(ResultSet resultSet, int i) throws SQLException {
                MaterialDefine materialDefine = new MaterialDefine();
                materialDefine.setId(resultSet.getLong("material_define.id"));
                materialDefine.setCheckType(resultSet.getString("check_type"));
                materialDefine.setDrawingNo(resultSet.getString("drawing_no"));
                materialDefine.setMaterialCode(resultSet.getString("material_code"));
                materialDefine.setMaterialComment(resultSet.getString("material_comment"));
                materialDefine.setMaterialModel(resultSet.getString("material_model"));
                materialDefine.setMaterialName(resultSet.getString("material_name"));
                materialDefine.setMaterialSource(resultSet.getString("material_source"));
                materialDefine.setMaterialTypeId(resultSet.getString("material_type_id"));
                materialDefine.setMaterialTypeName(resultSet.getString("material_type_name"));
                materialDefine.setTechWarCode(resultSet.getString("tech_war_code"));
                materialDefine.setUnitId(resultSet.getLong("unit_id"));
                materialDefine.setUnitName(resultSet.getString("unit_name"));
                materialDefine.setWhName(resultSet.getString("wh_name"));
                materialDefine.setStatus(resultSet.getString("status"));
                materialDefine.setYuLiuZiDuan(resultSet.getString("yu_liu_zi_duan"));
                return materialDefine;
            }
        });
        System.out.println("query:"+query);
        return query;
    }
    /**
     * 物料定义修改
     * @param listMaterialDefine
     * @param materialDefineId
     * @return
     * @throws Exception
     */
    @Transactional
    public Boolean modifyMaterialDefine(List<MaterialDefine> listMaterialDefine, Long materialDefineId) throws Exception{

        // 获得添加的物料定义及子表信息
        MaterialDefine addMaterialDefine = listMaterialDefine.get(0);
        Boolean addnewBoolean = addNewMaterialDefine(addMaterialDefine, materialDefineId);

        // 获得删除的物料定义及子表信息
        MaterialDefine deleteMaterialDefine = listMaterialDefine.get(1);
        Boolean deleteBoolean = deleteMaterialDefine(deleteMaterialDefine, materialDefineId);

        // 获得修改的物料定义及子表信息
        MaterialDefine updateMaterialDefine = listMaterialDefine.get(2);
        Boolean updateBoolean = updateMaterialDefine(updateMaterialDefine, materialDefineId);

        if (addnewBoolean == true && deleteBoolean == true && updateBoolean == true) {
            return true;
        }
        return false;
    }

    /**
     * 修改物料定义中的addnew
     * @param addMaterialDefine
     * @param materialDefineId
     * @return
     * @throws Exception
     */
    private Boolean addNewMaterialDefine(MaterialDefine addMaterialDefine, Long materialDefineId) throws Exception {
        // 得到Id之后从数据库读取该Id对应的物料定义以及子表的信息
        MaterialDefine materialDefine = null;

        try {
            materialDefine = getMaterialDefineById(materialDefineId);

            // 如果查询到为null 则不进行处理
            if (materialDefine == null) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        boolean addAllFieldNull = false;

        try {
            addAllFieldNull = ObjectIsNull.isAllFieldNull(addMaterialDefine);
        } catch (Exception e) {
            return false;
        }

        if (addAllFieldNull == false) {
            materialDefine.setUpdateTime(addMaterialDefine.getUpdateTime());
            Set<MaterialParameter> materialParameters = addMaterialDefine.getMaterialParamsTab1();
            if (materialParameters.size() != 0) {
                for (MaterialParameter materialParameter : materialParameters) {
                    Set<MaterialParameter> materialParamsTab1 = materialDefine.getMaterialParamsTab1();
                    materialParamsTab1.add(materialParameter);
                }
            }

            Set<JuniorMaterial> juniorMaterials = addMaterialDefine.getMaterialParamsTab2();
            if (juniorMaterials.size() != 0) {
                for (JuniorMaterial juniorMaterial : juniorMaterials) {
                    Set<JuniorMaterial> materialParamsTab2 = materialDefine.getMaterialParamsTab2();
                    materialParamsTab2.add(juniorMaterial);
                }
            }
        }
        MaterialDefine save = materialDefineRepository.save(materialDefine);
        return save != null ? true : false;
    }

    /**
     * 修改物料定义中的remove
     * @param deleteMaterialDefine
     * @param materialDefineId
     * @return
     * @throws Exception
     */
    private Boolean deleteMaterialDefine(MaterialDefine deleteMaterialDefine, Long materialDefineId) throws Exception {

        // 得到Id之后从数据库读取该Id对应的物料定义以及子表的信息
        MaterialDefine materialDefine = null;

        try {
            materialDefine = getMaterialDefineById(materialDefineId);
            System.out.println("删除前:"+materialDefine);
            // 如果查询到为null 则不进行处理
            if (materialDefine == null) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        boolean deleteAllFieldNull = false;
        try {
            deleteAllFieldNull = ObjectIsNull.isAllFieldNull(deleteMaterialDefine);
        } catch (Exception e) {
            return false;
        }
        if (deleteAllFieldNull == false) {
            materialDefine.setUpdateTime(deleteMaterialDefine.getUpdateTime());
            Set<MaterialParameter> materialParameters = deleteMaterialDefine.getMaterialParamsTab1();
            if (materialParameters.size() != 0) {
                Set<MaterialParameter> materialParamsTab1 = materialDefine.getMaterialParamsTab1();
                for (MaterialParameter materialParameter : materialParameters) {
                    try {
                        Iterator<MaterialParameter> iterator = materialParamsTab1.iterator();
                        while (iterator.hasNext()) {
                            MaterialParameter next = iterator.next();
                            if (next.getId().longValue() == materialParameter.getId().longValue()) {
                                iterator.remove();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("删除物料参数失败");
                        return false;
                    }
                }
            }

            Set<JuniorMaterial> juniorMaterials = deleteMaterialDefine.getMaterialParamsTab2();
            if (juniorMaterials.size() != 0) {
                Set<JuniorMaterial> materialParamsTab2 = materialDefine.getMaterialParamsTab2();
                for (JuniorMaterial juniorMaterial : juniorMaterials) {
                    try {
                        Iterator<JuniorMaterial> iterator = materialParamsTab2.iterator();
                        while (iterator.hasNext()) {
                            JuniorMaterial next = iterator.next();
                            if (next.getId().longValue() == juniorMaterial.getId().longValue()) {
                                iterator.remove();
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("删除下级物料参数失败");
                        return false;
                    }
                }
            }
        }
        MaterialDefine save = materialDefineRepository.save(materialDefine);
        return save != null ? true : false;
    }

    /**
     * 修改物料定义中的update
     * @param updateMaterialDefine
     * @param materialDefineId
     * @return
     * @throws Exception
     */
    private Boolean updateMaterialDefine(MaterialDefine updateMaterialDefine, Long materialDefineId) throws Exception {
        // 得到Id之后从数据库读取该Id对应的物料定义以及子表的信息
        MaterialDefine materialDefine = null;

        try {
            materialDefine = getMaterialDefineById(materialDefineId);
            System.out.println("更新前:"+materialDefine);
            // 如果查询到为null 则不进行处理
            if (materialDefine == null) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        boolean updateAllFieldNull = false;
        try {
            updateAllFieldNull = ObjectIsNull.isAllFieldNull(updateMaterialDefine);
        } catch (Exception e) {
            return false;
        }
        // 更新物料定义修改过的属性
        if (updateAllFieldNull == false) {
            if (updateMaterialDefine.getMaterialCode() != null) {
                materialDefine.setMaterialCode(updateMaterialDefine.getMaterialCode());
            }
            if (updateMaterialDefine.getMaterialName() != null) {
                materialDefine.setMaterialName(updateMaterialDefine.getMaterialName());
            }
            if (updateMaterialDefine.getMaterialModel() != null) {
                materialDefine.setMaterialModel(updateMaterialDefine.getMaterialModel());
            }
            if (updateMaterialDefine.getUnitId() != null) {
                MeasurementUnit one = measurementUnitRepository.findOne(updateMaterialDefine.getUnitId());
                if (one != null) {
                    materialDefine.setUnitId(updateMaterialDefine.getUnitId());
                } else {
                    return false;
                }
            }
            if (updateMaterialDefine.getWhName() != null) {
                materialDefine.setWhName(updateMaterialDefine.getWhName());
            }
            if (updateMaterialDefine.getTechWarCode() != null) {
                materialDefine.setTechWarCode(updateMaterialDefine.getTechWarCode());
            }
            if (updateMaterialDefine.getCheckType() != null) {
                materialDefine.setCheckType(updateMaterialDefine.getCheckType());
            }
            if (updateMaterialDefine.getCheckType() != null) {
                materialDefine.setCheckType(updateMaterialDefine.getCheckType());
            }
            if (updateMaterialDefine.getDrawingNo() != null) {
                materialDefine.setDrawingNo(updateMaterialDefine.getDrawingNo());
            }
            if (updateMaterialDefine.getMaterialComment() != null) {
                materialDefine.setMaterialComment(updateMaterialDefine.getMaterialComment());
            }
            if (updateMaterialDefine.getMaterialSource() != null) {
                materialDefine.setMaterialSource(updateMaterialDefine.getMaterialSource());
            }
            if (updateMaterialDefine.getUpdateTime() != null) {
                materialDefine.setUpdateTime(updateMaterialDefine.getUpdateTime());
            }
            if (updateMaterialDefine.getStatus() != null) {
                materialDefine.setStatus(updateMaterialDefine.getStatus());
            }
            if (updateMaterialDefine.getYuLiuZiDuan() != null) {
                materialDefine.setYuLiuZiDuan(updateMaterialDefine.getYuLiuZiDuan());
            }
            if (updateMaterialDefine.getMaterialTypeId() != null) {
                MaterialType one = materialTypeRepository.findByMaterialTypeId(updateMaterialDefine.getMaterialTypeId());
                if (one != null) {
                    materialDefine.setMaterialTypeId(updateMaterialDefine.getMaterialTypeId());
                } else {
                    return false;
                }

            }

            Set<MaterialParameter> updateMaterialParameters = updateMaterialDefine.getMaterialParamsTab1();
            if (updateMaterialParameters.size() != 0) {
                Set<MaterialParameter> materialParameters = materialDefine.getMaterialParamsTab1();
                for (MaterialParameter updateMaterialParameter : updateMaterialParameters) {
                    Long id = updateMaterialParameter.getId();
                    for (MaterialParameter materialParameter : materialParameters) {
                        if (id.longValue() == materialParameter.getId().longValue()) {
                            if (updateMaterialParameter.getMaterialParamsName() != null) {
                                materialParameter.setMaterialParamsName(updateMaterialParameter.getMaterialParamsName());
                            }
                            if (updateMaterialParameter.getMaterialParamsValue() != null) {
                                materialParameter.setMaterialParamsValue(updateMaterialParameter.getMaterialParamsValue());
                            }
                            break;
                        }
                    }
                }
            }


            Set<JuniorMaterial> updateJuniorMaterials = updateMaterialDefine.getMaterialParamsTab2();
            if (updateJuniorMaterials.size() != 0) {
                Set<JuniorMaterial> juniorMaterials = materialDefine.getMaterialParamsTab2();
                for (JuniorMaterial updateJuniorMaterial : updateJuniorMaterials) {
                    Long id = updateJuniorMaterial.getId();
                    for (JuniorMaterial juniorMaterial : juniorMaterials) {
                        if (id.longValue() == juniorMaterial.getId().longValue()) {
                            if (updateJuniorMaterial.getMaterialChildCode() != null) {
                                juniorMaterial.setMaterialChildCode(updateJuniorMaterial.getMaterialChildCode());
                            }
                            if (updateJuniorMaterial.getMaterialChildName() != null) {
                                juniorMaterial.setMaterialChildName(updateJuniorMaterial.getMaterialChildName());
                            }
                            if (updateJuniorMaterial.getMaterialChildCount() != null) {
                                juniorMaterial.setMaterialChildCount(updateJuniorMaterial.getMaterialChildCount());
                            }
                            if (updateJuniorMaterial.getMaterialChildUnit() != null) {
                                juniorMaterial.setMaterialChildUnit(updateJuniorMaterial.getMaterialChildUnit());
                            }
                            if (updateJuniorMaterial.getMaterialChildModel() != null) {
                                juniorMaterial.setMaterialChildModel(updateJuniorMaterial.getMaterialChildModel());
                            }
                        }
                    }
                }
            }
            System.out.println("更新后:"+materialDefine);
            try {
                MaterialDefine saveMaterialDefine = addMaterialDefine(materialDefine);
                if (saveMaterialDefine != null) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        return false;
    }

    /**
     * 根据物料编码查询对应的物料信息
     * @param materialCode
     * @return
     */
    public MaterialDefine getMaterialByMaterialCode(String materialCode) {
        return materialDefineRepository.findByMaterialCode(materialCode);
    }

    /**
     * 根据时间间隔统计不同类型的产量对比
     * @param analyses
     * @return
     * @throws Exception
     */
    public List<Analysis> getMaterialTypeNameByMaterialTypeId(List<Analysis> analyses) {
        for (Analysis analysis : analyses) {
            MaterialDefine byMaterialName = materialDefineRepository.findByMaterialName(analysis.getName());
            if (byMaterialName != null) {
                analysis.setName(byMaterialName.getMaterialModel());
            }
        }
        return analyses;
    }

}
