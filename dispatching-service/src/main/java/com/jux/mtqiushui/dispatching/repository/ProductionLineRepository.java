package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.ProductionLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionLineRepository extends JpaRepository<ProductionLine,Long>,PagingAndSortingRepository<ProductionLine,Long> {
    //根据id查询对应的产线以及附带的工位
    ProductionLine findProductionLingById(Long id);
}
