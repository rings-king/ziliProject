package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "plan_detail")
public class PlanDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 物料ID
    @Column(name = "material_id")
    private Long materialId;

    // 物料名称
    @Column(name = "material_name")
    private String materialName;

    // 主单位ID
    @Column(name = "unit_id")
    private Long unitId;

    // 主单位
    @Column(name = "unit_name")
    private String unitName;

    // 计划完成数量
    @Column(name = "done_quantity")
    private Integer doneQuantity;

    // 实际完成数量
    @Column(name = "total_done_quantity")
    private Integer totalDoneQuantity;

    // 规格型号
    @Column(name = "material_model")
    private String materialModel;

    // 开始时间
    @Column(name = "start_time")
    @Temporal(value = TemporalType.DATE)
    private Date startTime;

    // 结束时间
    @Column(name = "end_time")
    @Temporal(value = TemporalType.DATE)
    private Date endTime;

    // 计划详情物料编码
    @Column(name = "material_code")
    private String materialCode;

    // 计划开始时间
    @Column(name = "plan_start_time")
    @Temporal(value = TemporalType.DATE)
    private Date planStartTime;

    // 计划结束时间
    @Column(name = "plan_end_time")
    @Temporal(value = TemporalType.DATE)
    private Date planEndTime;

    // 已派工数量
    @Column(name = "task_num")
    private Integer taskNum;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH}, optional = true, targetEntity = PlanManage.class)
    @JoinColumn(name = "plan_manage_id")
    private PlanManage planManage;

    public PlanDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getDoneQuantity() {
        return doneQuantity;
    }

    public void setDoneQuantity(Integer doneQuantity) {
        this.doneQuantity = doneQuantity;
    }

    public Integer getTotalDoneQuantity() {
        return totalDoneQuantity;
    }

    public void setTotalDoneQuantity(Integer totalDoneQuantity) {
        this.totalDoneQuantity = totalDoneQuantity;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }

    public Date getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }

    public PlanManage getPlanManage() {
        return planManage;
    }

    public void setPlanManage(PlanManage planManage) {
        this.planManage = planManage;
    }

    public Integer getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(Integer taskNum) {
        this.taskNum = taskNum;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    @Override
    public String toString() {
        return "PlanDetail{" +
          "id=" + id +
          ", materialId=" + materialId +
          ", materialName='" + materialName + '\'' +
          ", unitId=" + unitId +
          ", unitName='" + unitName + '\'' +
          ", doneQuantity=" + doneQuantity +
          ", totalDoneQuantity=" + totalDoneQuantity +
          ", materialModel='" + materialModel + '\'' +
          ", startTime=" + startTime +
          ", endTime=" + endTime +
          ", materialCode='" + materialCode + '\'' +
          ", planStartTime=" + planStartTime +
          ", planEndTime=" + planEndTime +
          ", taskNum=" + taskNum +
          ", planManage=" + planManage +
          '}';
    }
}
