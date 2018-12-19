package com.jux.mtqiushui.resources.services;

import com.jux.mtqiushui.resources.model.MaterialType;
import com.jux.mtqiushui.resources.model.SearchCondition;
import com.jux.mtqiushui.resources.repository.MaterialTypeRepository;
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
import java.util.List;
import java.util.Set;

@Service
public class MaterialTypeService {

    @Autowired
    private MaterialTypeRepository materialTypeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加一个物料分类
     * @param materialType
     * @return
     */
    @Transactional
    public MaterialType addMaterialType(MaterialType materialType) {
        MaterialType save = materialTypeRepository.save(materialType);
        return save;
    }

    /**
     * 查询所有物料分类,支持分页
     * @param pageable
     * @return
     * @throws Exception
     */
    public Page<MaterialType> getAllMaterialType(Pageable pageable) throws Exception{
        Page<MaterialType> all = materialTypeRepository.findAll(pageable);
        return all;
    }

    /**
     * 修改物料分类信息
     * @param materialType
     * @return
     * @throws Exception
     */
    @Transactional
    public MaterialType modifyMaterialTypeById(MaterialType materialType) throws Exception {
        MaterialType save = materialTypeRepository.save(materialType);
        return save;
    }

    /**
     * 根据类别编码返回物料分类对象
     * @param materialTypeId
     * @return
     * @throws Exception
     */
    public MaterialType getMaterialTypeById(String materialTypeId) throws Exception {
        MaterialType byMaterialTypeId = materialTypeRepository.findByMaterialTypeId(materialTypeId);
        return byMaterialTypeId;
    }

    /**
     * 根据物料分类ID 查询对应的物料分类信息
     * @param id
     * @return
     * @throws Exception
     */
    public MaterialType getMaterialType(Long id) throws Exception {
        MaterialType materialTypeById = materialTypeRepository.findMaterialTypeById(id);
        return materialTypeById;
    }

    /**
     * 根据类别编码查询对应的物料分类
     * @param materialTypeId
     * @return
     * @throws Exception
     */
    public MaterialType getMaterialByMaterialTypeId(String materialTypeId) throws Exception {
        MaterialType materialTypeById = materialTypeRepository.findByMaterialTypeId(materialTypeId);
        return materialTypeById;
    }
    /**
     * 根据类别编码删除该物料分类
     * @param materialTypeId
     * @throws Exception
     */
    @Transactional
    public void deleteMaterialTypeByTypeId(String materialTypeId) throws Exception {
        materialTypeRepository.deleteMaterialTypeByTypeId(materialTypeId);
    }

    /**
     * 删除类别编码为子物料分类的parentId的所有物料
     * @param parentId
     * @throws Exception
     */
    @Transactional
    public void deleteMaterialTypeByParentId(Long parentId) throws Exception {
        materialTypeRepository.deleteMaterialTypeByParentId(parentId);
    }

    public Long getMaterialTypeIdByTypeName(String typeName) throws Exception {
        MaterialType byMaterialTypeName = materialTypeRepository.findByMaterialTypeName(typeName);
        return byMaterialTypeName == null ? null : byMaterialTypeName.getId();
    }

    /**
     * 删除类别编码为子物料分类的parentId的所有物料
     * @param parentId
     * @throws Exception
     */
    public Set<MaterialType> getMaterialTypeByParentId(String parentId) throws Exception {
        return materialTypeRepository.findMaterialTypeByParentId(parentId);
    }

    /**
     * 多条件查询
     * @param conditionList
     * @return
     */
    public List<MaterialType> getMaterialType(List<SearchCondition> conditionList) throws Exception{

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
        if (aNew.contains("parentId")) {
            aNew = aNew.replace("parentId", "parent_id");
        }
        if (aNew.contains("materialTypeId")) {
            aNew = aNew.replace("materialTypeId","material_type_id");
        }
        if (aNew.contains("materialTypeName")) {
            aNew = aNew.replace("materialTypeName","material_type_name");
        }
        System.out.println("aNew:" + aNew);
        String sql = "select id,material_type_id,material_type_name,parent_id,parent_name from material_type where" + aNew + " order by update_time desc";
        System.out.println("sql:" + sql);
        List<MaterialType> query = jdbcTemplate.query(sql, new RowMapper<MaterialType>() {
            @Override
            public MaterialType mapRow(ResultSet resultSet, int i) throws SQLException {
                MaterialType materialType = new MaterialType();
                materialType.setId(resultSet.getLong("id"));
                materialType.setMaterialTypeId(resultSet.getString("material_type_id"));
                materialType.setMaterialTypeName(resultSet.getString("material_type_name"));
                materialType.setParentId(resultSet.getLong("parent_id"));
                if (materialType.getParentId().longValue() != 0l) {
                    // 根据自己物料分类的所属类别查询对应的类别名
                    String parentNameSql = "select material_type_name from material_type where id = " + materialType.getParentId();
                    String s = jdbcTemplate.queryForObject(parentNameSql, String.class);
                    if (s != null) {
                        materialType.setParentName(s);
                    }

                }
                return materialType;
            }
        });

        return query;
    }
}
