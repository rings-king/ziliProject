package com.jux.mtqiushui.auth.repository;

import com.jux.mtqiushui.auth.domain.SysDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SysDeptRepository extends JpaRepository<SysDept,Long> {

    @Query(value = "select depart_name from sys_dept where id = ?",nativeQuery = true)
    public String findDeptNameById(Long id);

    public SysDept findByDepartCode(String depart_code);

    public SysDept findByDepartName(String depart_name);


    public  SysDept findById(Long id);

    @Query(value = "select username from sys_user where department_id = ?",nativeQuery = true)
    public String findUserByDeptId(String id);

    @Query(value = "select emp_name from employee where department_id = ?",nativeQuery = true)
    public String findEmpByDeptId(Long id);

    @Query(value = "select * from sys_dept where id in (:deptId)",nativeQuery = true)
    public List<SysDept> findDeptByid(@Param("deptId") List<Long> id );

}
