package com.jux.mtqiushui.auth.repository;

import com.jux.mtqiushui.auth.domain.SysUser;
import com.jux.mtqiushui.auth.repository.support.WiselyRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * Created by wangyunfei on 2017/6/9.
 */
public interface SysUserRepository extends WiselyRepository<SysUser,Long>, JpaRepository<SysUser,Long> , PagingAndSortingRepository<SysUser,Long> {
    Optional<SysUser> findOneWithRolesByUsername(String username);

    public SysUser findByUsername(String name);

    public SysUser findByEmail(String email);

    public SysUser findById(Long userId);

    public SysUser findByPhoneNum(String phoneNumber);

    @Modifying
    @Query(value = "insert into sys_user_roles values(?1,?2)",nativeQuery = true)
    public void setUserRole(Long userId, Long roleId);

    @Modifying
    @Transactional
    @Query(value = "update sys_user_roles set roles_id = ?2 where sys_user_id = ?1 ",nativeQuery = true)
    public Integer updateUserRole(Long userId,Long roleId);


    //根据用户id获取该用户的角色名称
    @Query(value = "select r.name from sys_role r ,sys_user u,sys_user_roles ur where r.id =  ur.roles_id and ur.sys_user_id= u.id and u.id = ?",nativeQuery = true)
    public List<String> getUserRole(Long userId);

    //根据用户id获取该用户的角色id集合
    @Query(value = "select roles_id from sys_user_roles where sys_user_id = ?",nativeQuery = true)
    public List<BigInteger> getUserRoleId(Long userId);


    //根据用户id获取用户所在部门
    @Query(value = "select d.depart_name from sys_dept d where d.id = '+'('+' select u.department_id from sys_user u where u.id = ?'+')'+'",nativeQuery = true)
    public String getUserDeptment(Long userId);

    //根据用户部门获取用户所在部门id
    @Query(value = "select d.id from sys_dept d where d.depart_name =?",nativeQuery = true)
    public Long getDeptIdByName(String deptName);

    @Modifying
    @Transactional
    @Query(value = "delete from sys_user_roles where sys_user_id =?",nativeQuery = true)
    public Integer deleteUserRole(Long userId);


    @Query(value = "select * from sys_user where id in (:userId)",nativeQuery = true)
    public List<SysUser> getUserBySimple(@Param("userId") List<Long> id );




}
