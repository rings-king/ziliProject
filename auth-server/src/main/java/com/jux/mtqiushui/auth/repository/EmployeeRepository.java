package com.jux.mtqiushui.auth.repository;

import com.jux.mtqiushui.auth.domain.SysEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<SysEmployee,Long> , PagingAndSortingRepository<SysEmployee,Long> {


    SysEmployee findByEmpNumber(String empNumber);

    SysEmployee findById(Long id);


    @Query(value = "SELECT\n" +
      "`quarter`.quartername\n" +
      "FROM\n" +
      "`quarter`\n" +
      "where\n" +
      "id = ?",nativeQuery = true)
    public String getQuarterById(Long id);



    @Query(value = "select emp_name from employee where id = ?",nativeQuery = true)
    public String getEmPById(Long id);

    @Query(value = "select * from employee where id in (:empId)",nativeQuery = true)
    public List<SysEmployee> findEmpById(@Param("empId") List<Long> id );


}
