package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 工艺流程  实体  新建
 */
@Entity
@Table(name = "process")
public class Process {
    //代理主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //工艺流程编号
    @Column(nullable = false, name = "process_code")
    private String processCode;
    //工艺流程名称
    @Column(nullable = false, name = "process_name")
    private String processName;
    //版本号
    @Column(nullable = false, name = "process_version")
    private String processVersion;

    //产品对应id
    @Column(nullable = false,name = "process_production_id")
    private  Long  processProductionId;
    //产品名
    @Column(nullable = true, name = "process_production_name")
    private String processProductionName;

    //规则型号
    @Column(nullable = true, name = "material_model")
    private String materialModel;

    // 更新时间
    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    /**
     * 一个工艺流程  对应多个工序
     */
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "process")
    //前台过来 接收 json名称
    private Set<ProcessNum> correspondProcessTab1 = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(String processVersion) {
        this.processVersion = processVersion;
    }


    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Set<ProcessNum> getCorrespondProcessTab1() {
        return correspondProcessTab1;
    }

    public void setCorrespondProcessTab1(Set<ProcessNum> correspondProcessTab1) {
        this.correspondProcessTab1 = correspondProcessTab1;
    }

    public String getProcessProductionName() {
        return processProductionName;
    }

    public void setProcessProductionName(String processProductionName) {
        this.processProductionName = processProductionName;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }

    public Long getProcessProductionId() {
        return processProductionId;
    }

    public void setProcessProductionId(Long processProductionId) {
        this.processProductionId = processProductionId;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", processCode='" + processCode + '\'' +
                ", processName='" + processName + '\'' +
                ", processVersion='" + processVersion + '\'' +
                ", processProductionId=" + processProductionId +
                ", processProductionName='" + processProductionName + '\'' +
                ", materialModel='" + materialModel + '\'' +
                ", updateTime=" + updateTime +
                ", correspondProcessTab1=" + correspondProcessTab1 +
                '}';
    }
}
