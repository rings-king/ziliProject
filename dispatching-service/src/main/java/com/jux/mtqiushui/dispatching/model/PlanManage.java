package com.jux.mtqiushui.dispatching.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// 计划管理
@Entity
@Table(name = "plan_manage")
public class PlanManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 年度
    @Column(name = "plan_year")
    private String planYear;

    // 月度
    @Column(name = "plan_month")
    private String planMonth;

    // 开始时间
    @Column(name = "start_time")
    @Temporal(value = TemporalType.DATE)
    private Date startTime;

    // 结束时间
    @Column(name = "end_time")
    @Temporal(value = TemporalType.DATE)
    private Date endTime;

    // 计划开始时间
    @Column(name = "plan_start_time")
    @Temporal(value = TemporalType.DATE)
    private Date planStartTime;

    // 计划结束时间
    @Column(name = "plan_end_time")
    @Temporal(value = TemporalType.DATE)
    private Date planEndTime;

    @Column(name = "total_done_percent")
    private Double totalDonePercent;

    // 更新时间
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    // 计划单号
    @Column(name = "plan_num")
    private String planNum;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "planManage")
    private Set<PlanDetail> planDetailsTab = new HashSet<>();

    public PlanManage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanYear() {
        return planYear;
    }

    public void setPlanYear(String planYear) {
        this.planYear = planYear;
    }

    public String getPlanMonth() {
        return planMonth;
    }

    public void setPlanMonth(String planMonth) {
        this.planMonth = planMonth;
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

    public Double getTotalDonePercent() {
        return totalDonePercent;
    }

    public void setTotalDonePercent(Double totalDonePercent) {
        this.totalDonePercent = totalDonePercent;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPlanNum() {
        return planNum;
    }

    public void setPlanNum(String planNum) {
        this.planNum = planNum;
    }

    public Set<PlanDetail> getPlanDetailsTab() {
        return planDetailsTab;
    }

    public void setPlanDetailsTab(Set<PlanDetail> planDetailsTab) {
        this.planDetailsTab = planDetailsTab;
    }

    @Override
    public String toString() {
        return "PlanManage{" +
          "id=" + id +
          ", planYear='" + planYear + '\'' +
          ", planMonth='" + planMonth + '\'' +
          ", startTime=" + startTime +
          ", endTime=" + endTime +
          ", planStartTime=" + planStartTime +
          ", planEndTime=" + planEndTime +
          ", totalDonePercent=" + totalDonePercent +
          ", updateTime=" + updateTime +
          ", planNum='" + planNum + '\'' +
          ", planDetailsTab=" + planDetailsTab +
          '}';
    }
}
