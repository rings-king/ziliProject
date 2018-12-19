package com.jux.mtqiushui.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sys_dept")
public class SysDept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "departCode",nullable = false)
    private String departCode;
    @Column(name = "departName",nullable = false)
    private String departName;
    @Column(name = "departParentId")
    private Long departParentId;

    @Column(name = "updateTime",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updateTime;

    @Transient
    private String deparParentName;

    @Transient
    private List<Quarter> quarterDetailsTab;



    public SysDept() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeparParentName() {
        return deparParentName;
    }

    public void setDeparParentName(String deparParentName) {
        this.deparParentName = deparParentName;
    }


    public String getDepartCode() {
        return departCode;
    }

    public void setDepartCode(String departCode) {
        this.departCode = departCode;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public Long getDepartParentId() {
        return departParentId;
    }

    public void setDepartParentId(Long departParentId) {
        this.departParentId = departParentId;
    }


    public List<Quarter> getQuarterDetailsTab() {
        return quarterDetailsTab;
    }

    public void setQuarterDetailsTab(List<Quarter> quarterDetailsTab) {
        this.quarterDetailsTab = quarterDetailsTab;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "SysDept{" +
          "id=" + id +
          ", departCode='" + departCode + '\'' +
          ", departName='" + departName + '\'' +
          ", departParentId=" + departParentId +
          ", deparParentName='" + deparParentName + '\'' +
          ", quarterDetailsTab=" + quarterDetailsTab +
          '}';
    }
}
