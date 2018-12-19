/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author jux
 */
public class GanttChart {

    @JsonProperty("success")
    private Boolean success;
    
    @JsonProperty("errorMessage")
    private String errorMessage;


    @JsonProperty("assignments")
    private Rows<Iterable<Assignment>> assignments;

    @JsonProperty("calendars")
    private Rows<List<Calendar>> calendars;

    @JsonProperty("resources")
    private Rows<Iterable<Resource>> resources;
    
    @JsonProperty("dependencies")
    private Rows<Iterable<Dependency>> dependencies;
    
    @JsonProperty("tasks")
    private Rows<List<TaskTree>> tasks;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Rows<Iterable<Assignment>> getAssignments() {
        return assignments;
    }

    public void setAssignments(Rows<Iterable<Assignment>> assignments) {
        this.assignments = assignments;
    }

    public Rows<List<TaskTree>> getTasks() {
        return tasks;
    }

    public void setTasks(Rows<List<TaskTree>> tasks) {
        this.tasks = tasks;
    }

    public Rows<List<Calendar>> getCalendars() {
        return calendars;
    }

    public void setCalendars(Rows<List<Calendar>> calendars) {
        this.calendars = calendars;
    }

    public Rows<Iterable<Resource>> getResources() {
        return resources;
    }

    public void setResources(Rows<Iterable<Resource>> resources) {
        this.resources = resources;
    }

    public Rows<Iterable<Dependency>> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Rows<Iterable<Dependency>> dependencies) {
        this.dependencies = dependencies;
    }
    
}
