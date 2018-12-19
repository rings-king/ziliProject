package com.jux.mtqiushui.auth.repository;

import com.jux.mtqiushui.auth.domain.SysRole;
import com.jux.mtqiushui.auth.repository.support.WiselyRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wangyunfei on 2017/6/9.
 */
public interface SysRoleRepository extends WiselyRepository<SysRole,Long> , PagingAndSortingRepository<SysRole,Long>, JpaRepository<SysRole,Long> {

    public SysRole findByName(String name);

    @Query(value = "select role_permission from sys_role where id in (:roleId)",nativeQuery = true)
    public List<String> findRolePermisssionbyid(@Param("roleId") List<Long> id );

    @Query(value = "select * from sys_role where id in (:roleId)",nativeQuery = true)
    public List<SysRole> findRoleById(@Param("roleId") List<Long> id );

    @Modifying
    @Transactional
    @Query(value = "delete from sys_user_roles where roles_id = ?",nativeQuery = true)
    public Integer deleteUserRole(Long roleId);

}
