package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.PlanManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanManageRepository extends JpaRepository<PlanManage, Long>, PagingAndSortingRepository<PlanManage, Long> {
    /**
     * 根据派工单号查询
     * @param planNum
     * @return
     */
    public PlanManage findByPlanNum(String planNum);

}
