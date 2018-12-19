/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tasks")

/**
 *
 * @author jux
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskTree {
    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonProperty("Id")
    private Long id;
    @Column(name = "parent_id", nullable = true)
    @JsonIgnore
    private Long parentId;
    //物料名称
    @Column(name = "name", nullable = false)
    @JsonProperty("Name")
    private String name;
    //规则型号
    @Column(name = "material_model", nullable = false)
    private String materialModel;
    //百分数
    @Column(name = "percent_done", nullable = true)
    @JsonProperty("PercentDone")
    private Double percentDone;
    //实际开工时间
    @Column(name = "start_date", nullable = true)
    @JsonProperty("StartDate")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp startDate;
    //实际完工时间
    @Column(name = "end_date", nullable = true)
    @JsonProperty("EndDate")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp endDate;
    //计划开工时间
    @Column(name = "baseline_startdate", nullable = false)
    @JsonProperty("BaselineStartDate")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp baselineStartDate;
    //计划完工时间
    @Column(name = "baseline_enddate", nullable = false)
    @JsonProperty("BaselineEndDate")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp baselineEndDate;
    //
    @Column(name = "leaf", nullable = true)
    @JsonProperty("leaf")//是否最末级
    private Boolean leaf;
    //
    @Column(name = "duration", nullable = true)
    @JsonProperty("Duration")//花费工时
    private Double duration;

    @Column(name = "resizable", nullable = true)
    @JsonProperty("Resizable")//该列是否缩放
    private Boolean resizable;

    @Column(name = "draggable", nullable = true)
    @JsonProperty("Draggable")//是否拖动
    private Boolean draggable;

    @Column(name = "manually_scheduled", nullable = true)
    @JsonProperty("ManuallyScheduled")//是否
    private Boolean manuallyScheduled;

    @Column(name = "expanded", nullable = true)
    @JsonProperty("expanded")//是否展开
    private Boolean expanded;

    @Column(name = "index_field", nullable = true)
    @JsonProperty("index")//序号
    private Integer index;
    //计划产量
    @Column(nullable = false, name = "planned_quantity")
    private String BaseLineQuantity;
    //实际产量
    @Column(nullable = true, name = "actual_completion_quantity")
    private String ActualQuantity;

    //一个派工对应多个产线
    @Transient
    @JsonProperty("productionLines")
    private  List<ProductionTreeLine> productionTreeLines;
    @Transient
    @JsonProperty("children")//子集
    private List<TaskTree> children = null;

    public List<TaskTree> getChildren() {
        return children;
    }

    public void setChildren(List<TaskTree> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPercentDone() {
        return percentDone;
    }

    public void setPercentDone(Double percentDone) {
        this.percentDone = percentDone;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Timestamp getBaselineStartDate() {
        return baselineStartDate;
    }

    public void setBaselineStartDate(Timestamp baselineStartDate) {
        this.baselineStartDate = baselineStartDate;
    }

    public Timestamp getBaselineEndDate() {
        return baselineEndDate;
    }

    public void setBaselineEndDate(Timestamp baselineEndDate) {
        this.baselineEndDate = baselineEndDate;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Boolean getResizable() {
        return resizable;
    }

    public void setResizable(Boolean resizable) {
        this.resizable = resizable;
    }

    public Boolean getDraggable() {
        return draggable;
    }

    public void setDraggable(Boolean draggable) {
        this.draggable = draggable;
    }

    public Boolean getManuallyScheduled() {
        return manuallyScheduled;
    }

    public void setManuallyScheduled(Boolean manuallyScheduled) {
        this.manuallyScheduled = manuallyScheduled;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getBaseLineQuantity() {
        return BaseLineQuantity;
    }

    public void setBaseLineQuantity(String baseLineQuantity) {
        BaseLineQuantity = baseLineQuantity;
    }

    public String getActualQuantity() {
        return ActualQuantity;
    }

    public void setActualQuantity(String actualQuantity) {
        ActualQuantity = actualQuantity;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }

    public List<ProductionTreeLine> getProductionTreeLines() {
        return productionTreeLines;
    }

    public void setProductionTreeLines(List<ProductionTreeLine> productionTreeLines) {
        this.productionTreeLines = productionTreeLines;
    }
}
