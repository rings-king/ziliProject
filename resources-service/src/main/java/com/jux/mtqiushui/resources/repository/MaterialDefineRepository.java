package com.jux.mtqiushui.resources.repository;

import com.jux.mtqiushui.resources.model.MaterialDefine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialDefineRepository extends PagingAndSortingRepository<MaterialDefine, Long>, JpaRepository<MaterialDefine, Long>, CrudRepository<MaterialDefine, Long> {
    /**
     * 根据物料编码查询是否存在
     * @param defineCode
     * @return
     */
    public MaterialDefine findByMaterialCode(String defineCode);

    /**
     * 根据物料名称查询对应的物料定义
     * @param materialName
     * @return
     */
    public MaterialDefine findByMaterialName(String materialName);

}
