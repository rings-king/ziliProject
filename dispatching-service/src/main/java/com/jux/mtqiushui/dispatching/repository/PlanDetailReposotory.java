package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanDetailReposotory extends JpaRepository<PlanDetail, Long>, PagingAndSortingRepository<PlanDetail, Long> {
}
