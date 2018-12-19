/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.TaskTree;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author jux
 */
@Repository
public interface TaskTreeRepository extends CrudRepository<TaskTree, Long> {

    @Query(value = "SELECT * FROM tasks WHERE start_date < ?2 AND (end_date > ?1 OR end_date IS NULL) AND parent_id IS NULL", nativeQuery = true)
    public List<TaskTree> findByStartDateAndEndDate(Timestamp startDate, Timestamp endDate);

    public List<TaskTree> findByParentId(Long parentId);

}
