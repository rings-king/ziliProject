package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 派工单  新建
 */
@Entity
//对应数据库表
@Table(name = "tasktwo")
public class TaskTwo {
    //主键
    @Id
    @Column(nullable = false,name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //派工单号
    @Column(nullable = false,name = "dispatch_list_code")
    private String dispatchListCode;
    //选用工艺流程
    /**
     * 需要Long类型的id接收,查询的是工艺流程
     * 获得工艺流程和对应的版本号
     * processId  工艺id
     * processName 工艺流程名称
     * workProcessVersion 版本号
     */
    @Column(nullable = false,name = "process_id")
    private Long processId;
    @Column(nullable = true,name = "process_name")
    private String processName;
    @Column(nullable = true,name = "work_process_version")
    private String workProcessVersion;
    //生产产品名称
    /**
     *
     * 需要Long类型的id接收   查询的是物料定义
     * 获得物料名称
     * materialId   物料id
     * materialName 生产产品名称
     */
    @Column(nullable = false,name = "material_id")
    private  Long materialId;
    @Column(nullable = true,name = "material_name")
    private  String materialName;
    //工艺流程版本号
    /**
     * 计划管理id
     */
    @Column(nullable = false,name = "plan_num_id")
    private  Long planNum;
    @Column(nullable = true,name = "plan_num_code")
    private String planNumCode;
    //计划加工数量
    @Column(nullable = false,name = "planned_quantity")
    private String plannedQuantity;
    //实际完工数量
    @Column(nullable = true,name = "actual_completion_quantity")
    private String actualCompletionQuantity;
    //计划开工时间
    @Column(nullable = false,name = "planned_start_time")
    @Temporal(TemporalType.DATE)
    private Date plannedStartTime;
    //计划完工时间
    @Column(nullable = false,name = "planned_end_time")
    @Temporal(TemporalType.DATE)
    private Date plannedEndTime;
    //实际开工时间
    @Column(nullable = true,name = "actual_start_time")
    @Temporal(TemporalType.DATE)
    private Date actualStartTime;
    //实际完工时间
    @Column(nullable = true,name = "actual_end_time")
    @Temporal(TemporalType.DATE)
    private Date actualEndTime;
    // 更新时间
    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    //当前进度
    @Column(nullable = true,name = "percent")
    private String percent;
    //是否完成字段
    @Column(nullable = false,name = "is_finish", columnDefinition = "varchar(1) default '0'")
    private String isFinish;
    //一个产品派工 有多个工位  一对多关系 外键 taskTwo
    @OneToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY,mappedBy="taskTwo")
    private Set<DistributionStationTab1> distributionStationTab1 = new HashSet<>();
    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDispatchListCode() {
        return dispatchListCode;
    }

    public void setDispatchListCode(String dispatchListCode) {
        this.dispatchListCode = dispatchListCode;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getWorkProcessVersion() {
        return workProcessVersion;
    }

    public void setWorkProcessVersion(String workProcessVersion) {
        this.workProcessVersion = workProcessVersion;
    }

    public String getPlannedQuantity() {
        return plannedQuantity;
    }

    public void setPlannedQuantity(String plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }

    public String getActualCompletionQuantity() {
        return actualCompletionQuantity;
    }

    public void setActualCompletionQuantity(String actualCompletionQuantity) {
        this.actualCompletionQuantity = actualCompletionQuantity;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public Date getPlannedStartTime() {
        return plannedStartTime;
    }

    public void setPlannedStartTime(Date plannedStartTime) {
        this.plannedStartTime = plannedStartTime;
    }


    public Date getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(Date actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public Date getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public Set<DistributionStationTab1> getDistributionStationTab1() {
        return distributionStationTab1;
    }

    public void setDistributionStationTab1(Set<DistributionStationTab1> distributionStationTab1) {
        this.distributionStationTab1 = distributionStationTab1;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getPlannedEndTime() {
        return plannedEndTime;
    }

    public void setPlannedEndTime(Date plannedEndTime) {
        this.plannedEndTime = plannedEndTime;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public Long getPlanNum() {
        return planNum;
    }

    public void setPlanNum(Long planNum) {
        this.planNum = planNum;
    }

    public String getPlanNumCode() {
        return planNumCode;
    }

    public void setPlanNumCode(String planNumCode) {
        this.planNumCode = planNumCode;
    }

    @Override
    public String toString() {
        return "TaskTwo{" +
                "id=" + id +
                ", dispatchListCode='" + dispatchListCode + '\'' +
                ", processId=" + processId +
                ", processName='" + processName + '\'' +
                ", workProcessVersion='" + workProcessVersion + '\'' +
                ", materialId=" + materialId +
                ", materialName='" + materialName + '\'' +
                ", planNum=" + planNum +
                ", planNumCode='" + planNumCode + '\'' +
                ", plannedQuantity='" + plannedQuantity + '\'' +
                ", actualCompletionQuantity='" + actualCompletionQuantity + '\'' +
                ", plannedStartTime=" + plannedStartTime +
                ", plannedEndTime=" + plannedEndTime +
                ", actualStartTime=" + actualStartTime +
                ", actualEndTime=" + actualEndTime +
                ", updateTime=" + updateTime +
                ", percent='" + percent + '\'' +
                ", isFinish='" + isFinish + '\'' +
                ", distributionStationTab1=" + distributionStationTab1 +
                '}';
    }
}
