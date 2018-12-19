package com.jux.mtqiushui.auth.domain.Vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
public class UserVo {
    private Long id;
    private String realName;
    @JsonIgnore
    private String password;
    private String phoneNum;
    private String email;
    private String userDepartName;
    private String userDepartId;
    private String username;
    private List<Long> roleId = new ArrayList<>();
    private List<String> roleList = new ArrayList<>() ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserVo() {
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserDepartName() {
        return userDepartName;
    }

    public void setUserDepartName(String userDepartName) {
        this.userDepartName = userDepartName;
    }

    public String getUsername() {
        return username;
    }


    public String getUserDepartId() {
        return userDepartId;
    }

    public void setUserDepartId(String userDepartId) {
        this.userDepartId = userDepartId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserVo{" +
          "id=" + id +
          ", realName='" + realName + '\'' +
          ", password='" + password + '\'' +
          ", phoneNum='" + phoneNum + '\'' +
          ", email='" + email + '\'' +
          ", userDepartName='" + userDepartName + '\'' +
          ", username='" + username + '\'' +
          ", roleId=" + roleId +
          ", roleList=" + roleList +
          '}';
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getRoleId() {
        return roleId;
    }

    public void setRoleId(List<Long> roleId) {
        this.roleId = roleId;
    }

}
