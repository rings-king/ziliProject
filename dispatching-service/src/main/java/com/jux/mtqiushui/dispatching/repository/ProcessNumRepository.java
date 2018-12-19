package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.ProcessNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessNumRepository extends JpaRepository<ProcessNum,Long>{
    //根据id查询对应的工序
    public ProcessNum getProcessNumById(Long id);
    /**
     * 根据parentId删除工艺的所有子工序
     * @param processId
     */
    @Modifying
    @Query(value = "delete from process_num where process_id = ?1", nativeQuery = true)
    public void deleteProcessNumById(Long processId);


}
