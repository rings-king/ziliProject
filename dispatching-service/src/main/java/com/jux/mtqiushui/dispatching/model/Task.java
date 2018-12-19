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
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tasks")

/**
 *
 * @author jux
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @JsonProperty("Id")
    private Long id;

    @Column(name = "parent_id", nullable = true)
    @JsonIgnore
    private Long parentId;

    @Column(name = "name", nullable = false)
    @JsonProperty("Name")
    private String name;

    @Column(name = "percent_done", nullable = true)
    @JsonProperty("PercentDone")
    private Double percentDone;

    @Column(name = "start_date", nullable = false)
    @JsonProperty("StartDate")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp startDate;

    @Column(name = "end_date", nullable = true)
    @JsonProperty("EndDate")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp endDate;

    @Column(name = "baseline_startdate", nullable = false)
    @JsonProperty("BaselineStartDate")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp baselineStartDate;

    @Column(name = "baseline_enddate", nullable = false)
    @JsonProperty("BaselineEndDate")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp baselineEndDate;

    @Column(name = "leaf", nullable = true)
    @JsonProperty("Leaf")
    private Boolean leaf;

    @Column(name = "duration", nullable = true)
    @JsonProperty("Duration")
    private Integer duration;

    @Column(name = "resizable", nullable = true)
    @JsonProperty("Resizable")
    private Boolean resizable;

    @Column(name = "draggable", nullable = true)
    @JsonProperty("Draggable")
    private Boolean draggable;

    @Column(name = "manually_scheduled", nullable = true)
    @JsonProperty("ManuallyScheduled")
    private Boolean manuallyScheduled;

    @Column(name = "expanded", nullable = true)
    @JsonProperty("Expanded")
    private Boolean expanded;

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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
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
}
