package com.jux.mtqiushui.auth.repository;

import com.jux.mtqiushui.auth.domain.Quarter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface QuarterRepository extends JpaRepository<Quarter,Long>, CrudRepository<Quarter,Long> {

    List<Quarter> findByDeptId(Long id);

    @Query(value = "delete quarter where id in (:list)",nativeQuery = true)
    @Transactional
    @Modifying
    public void deleteById(@Param("list")List<Long> list);

    @Transactional
    public void deleteByIdAndAndDeptId(Long id,Long deptId);

    @Transactional
    @Modifying
    @Query(value = "delete from quarter where deptid =?",nativeQuery = true)
    public int deleteByDeptId(Long deptId);
}
