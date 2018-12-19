package com.jux.mtqiushui.resources.repository;

import com.jux.mtqiushui.resources.model.MaterialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MaterialTypeRepository extends PagingAndSortingRepository<MaterialType,Long> ,JpaRepository<MaterialType,Long> {
    /**
     * 根据类别编码查询对应的物料分类
     * @param materialTypeId
     * @return
     */
    public MaterialType findByMaterialTypeId(String materialTypeId);

    /**
     * 根据类别编码删除物料分类
     * @param materialTypeId
     */
    @Modifying
    @Query(value = "delete from material_type where material_type_id = ?1", nativeQuery = true)
    public void deleteMaterialTypeByTypeId(String materialTypeId);

    /**
     * 根据parentId删除该物料的所有子物料分类
     * @param parentId
     */
    @Modifying
    @Query(value = "delete from material_type where parent_id = ?1", nativeQuery = true)
    public void deleteMaterialTypeByParentId(Long parentId);

    @Modifying
    @Query(value = "select * from material_type where parent_id = ?1", nativeQuery = true)
    public Set<MaterialType> findMaterialTypeByParentId(String parentId);

    /**
     * 根据物料分类ID 查询对应的物料分类信息
     * @param id
     * @return
     */
    public MaterialType findMaterialTypeById(Long id);

    /**
     * 根据类别名查询
     * @param typeName
     * @return
     */
    public MaterialType findByMaterialTypeName(String typeName);

}
