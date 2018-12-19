package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.DistributionStationTab1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DistributionStationTab1Repository extends JpaRepository<DistributionStationTab1,Long> {
    @Modifying
    @Transactional
    @Query(value = "update distribution_stationtab1 set complet =?1 where  id =?2",nativeQuery = true)
     void   updateDById(String complet,Long id);


    @Query(value = "select complet from distribution_stationtab1 where tsk_two_id =? ",nativeQuery = true)
    public List<String> findCompletById(Long id);


    @Query(value = "select operation_id from distribution_stationtab1 where tsk_two_id =?",nativeQuery = true)
    public Long findOperationBytaskId(Long id);
}
