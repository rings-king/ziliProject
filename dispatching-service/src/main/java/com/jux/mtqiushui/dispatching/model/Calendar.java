/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author jux
 * 
 */
public class Calendar {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("parentId")
    private String parentId;
    @JsonProperty("DaysPerWeek")
    private Integer daysPerWeek;
    @JsonProperty("DaysPerMonth")
    private Integer daysPerMonth;
    @JsonProperty("HoursPerDay")
    private Integer hoursPerDay;
    @JsonProperty("WeekendFirstDay")
    private Integer weekendFirstDay;
    @JsonProperty("WeekendSecondDay")
    private Integer weekendSecondDay;
    @JsonProperty("WeekendsAreWorkDays")
    private Boolean weekendsAreWorkDays;
    @JsonProperty("DefaultAvailability")
    private String[] defaultAvailability;
    @JsonProperty("leaf")
    private Boolean leaf;
    @JsonProperty("Days")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Rows<List<Day>> days;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(Integer daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public Integer getDaysPerMonth() {
        return daysPerMonth;
    }

    public void setDaysPerMonth(Integer daysPerMonth) {
        this.daysPerMonth = daysPerMonth;
    }

    public Integer getHoursPerDay() {
        return hoursPerDay;
    }

    public void setHoursPerDay(Integer hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    public Integer getWeekendFirstDay() {
        return weekendFirstDay;
    }

    public void setWeekendFirstDay(Integer weekendFirstDay) {
        this.weekendFirstDay = weekendFirstDay;
    }

    public Integer getWeekendSecondDay() {
        return weekendSecondDay;
    }

    public void setWeekendSecondDay(Integer weekendSecondDay) {
        this.weekendSecondDay = weekendSecondDay;
    }

    public Boolean getWeekendsAreWorkDays() {
        return weekendsAreWorkDays;
    }

    public void setWeekendsAreWorkDays(Boolean weekendsAreWorkDays) {
        this.weekendsAreWorkDays = weekendsAreWorkDays;
    }

    public String[] getDefaultAvailability() {
        return defaultAvailability;
    }

    public void setDefaultAvailability(String[] defaultAvailability) {
        this.defaultAvailability = defaultAvailability;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public Rows<List<Day>> getDays() {
        return days;
    }

    public void setDays(Rows<List<Day>> days) {
        this.days = days;
    }
    
}
