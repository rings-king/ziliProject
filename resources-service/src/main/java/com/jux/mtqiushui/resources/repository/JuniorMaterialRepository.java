package com.jux.mtqiushui.resources.repository;

import com.jux.mtqiushui.resources.model.JuniorMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuniorMaterialRepository extends PagingAndSortingRepository<JuniorMaterial, Long>, JpaRepository<JuniorMaterial, Long> {

}
