package com.jux.mtqiushui.resources.repository;

import com.jux.mtqiushui.resources.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends PagingAndSortingRepository<Material, Long>, JpaRepository<Material, Long> {
    public Material findByMaterialId(Long materialId);
    public Page<Material> findByOrganizationId(Long organizationId, Pageable pageable);
    @Query(value = "delete from materials where organization_id=?1 ", nativeQuery = true)
    @Modifying
    public Integer deleteMaterialByOrganizationId(Long organizationId);
    public Integer countByOrganizationId(Long organizationId);

    public List<Material> findByOrganizationId(Long organizationId);
}
