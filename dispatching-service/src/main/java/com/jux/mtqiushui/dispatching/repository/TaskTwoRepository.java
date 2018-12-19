package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.TaskTwo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskTwoRepository extends PagingAndSortingRepository<TaskTwo,Long>,JpaRepository<TaskTwo,Long> {

    //通过id查询是否存在此对象
    public TaskTwo findByDispatchListCode(String dispatchlustCode);
    //通过分配产线 查询出对应的多个工位
    //public List<DistributionStationTab1> findByProductionLineLike(String productionLine);

    @Query(value = "select * from tasktwo where DATE_SUB(CURDATE(), INTERVAL 1 MONTH) <= date(update_time)",nativeQuery = true)
    public List<TaskTwo> findByUpdateTime();

    @Query(value = "SELECT * FROM tasktwo WHERE actual_start_time < ?2 AND (actual_start_time > ?1 )", nativeQuery = true)
    public List<TaskTwo> findByStartDateAndEndDate(String startDate, String endDate);

    @Query(value = "select *  from tasktwo where plan_num_id =?1 and material_id =?2",nativeQuery = true)
    public TaskTwo findByPlanNumAndMaterialId(Long planNumId,Long materialId);

}
