package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessRepository extends JpaRepository<Process,Long>,PagingAndSortingRepository<Process,Long> {
//根据id查询工艺流程
    public Process getProcessById(Long id);
}
