package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "distribution_stationtab1")
/**
 * 分配工位实体类 新建
 */
public class DistributionStationTab1 {
    //主键id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * 接收工序id 去工艺流程里面的工序查找名称
     *
     */
    @Column(nullable = false,name = "process_num_id")
    private Long processNumId;

    @Column(nullable = true,name = "process_name")
    private  String processName;
    /**
     * 分配产线
     * 接收Long类型 去产线定义查询
     * 获得产线名称
     * productionLineId  产线id
     * productionLineName 对应产线名称
     * stationId 该产线对应所属的工位id
     * stationName  工位名称
     * stationNumber 工位编号
     * updateTime 每次修改工位当前进度 时间
     */
    @Column(nullable = false,name = "production_line_id")
    private Long productionLineId;
    @Column(nullable = true,name = "production_line_name")
    private String productionLineName;
    @Column(nullable = false,name = "station_id")
    private Long stationId;
    @Column(nullable = true,name = "station_name")
    private  String stationName;
    /**
     * 操作工
     * 接收Long类型 去员工档案查询
     * 获得员工名称
     * operationId 员工id
     * username 员工姓名
     */
    @Column(nullable = false,name = "operation_id")
    private Long operationId;
    @Column(nullable = true,name = "operation_user")
    private  String operationUser;
    //已完成量
    @Column(nullable = true,name = "complet")
    private  String complet;
    //计划完成数量
    @Column(nullable = true,name = "planned_quantity")
    private String plannedQuantity;
    //完成进度
    @Column(nullable = true,name = "complet_Rate")
    private  String completRate;

    //派工单子任务是否完工
    @Column(name = "is_complete")
    private Integer isComplete = 0;
    /**
     * 一个产品派工  对应 多个工位
     * 表达 多对一关系
     */
    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH}, optional = true, targetEntity = TaskTwo.class)
    @JoinColumn(name = "tsk_two_id")
    private TaskTwo taskTwo;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProductionLineName() {
        return productionLineName;
    }

    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    public String getComplet() {
        return complet;
    }

    public void setComplet(String complet) {
        this.complet = complet;
    }

    public String getCompletRate() {
        return completRate;
    }

    public void setCompletRate(String completRate) {
        this.completRate = completRate;
    }

    public TaskTwo getTaskTwo() {
        return taskTwo;
    }

    public void setTaskTwo(TaskTwo taskTwo) {
        this.taskTwo = taskTwo;
    }

    public Long getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(Long productionLineId) {
        this.productionLineId = productionLineId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public Long getProcessNumId() {
        return processNumId;
    }

    public void setProcessNumId(Long processNumId) {
        this.processNumId = processNumId;
    }

    public String getPlannedQuantity() {
        return plannedQuantity;
    }

    public void setPlannedQuantity(String plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }

    @Override
    public String toString() {
        return "DistributionStationTab1{" +
          "id=" + id +
          ", processNumId=" + processNumId +
          ", processName='" + processName + '\'' +
          ", productionLineId=" + productionLineId +
          ", productionLineName='" + productionLineName + '\'' +
          ", stationId=" + stationId +
          ", stationName='" + stationName + '\'' +
          ", operationId=" + operationId +
          ", operationUser='" + operationUser + '\'' +
          ", complet='" + complet + '\'' +
          ", plannedQuantity='" + plannedQuantity + '\'' +
          ", completRate='" + completRate + '\'' +
          ", isComplete=" + isComplete +
          '}';
    }

    public Integer getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Integer isComplete) {
        this.isComplete = isComplete;
    }

}
