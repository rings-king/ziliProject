package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * 工序
 */
@Entity
@Table(name = "process_num")
public class ProcessNum {
    //主键
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //工序名称
    @Column(nullable = false, name = "process_name")
    private String processName;
    //  工序类型
    @Column(nullable = false, name = "process_type")
    private String processType;
    //节拍要求(秒)
    @Column(nullable = false, name = "limit_time")
    private String limitTime;
    /**
     * 产品名  对应多个工序
     */
    //@JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = true, targetEntity = Process.class)
    @JoinColumn(name = "process_id")
    @JsonIgnore
    private Process process;

    public ProcessNum() {
    }

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

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(String limitTime) {
        this.limitTime = limitTime;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    @Override
    public String toString() {
        return "ProcessNum{" +
                "id=" + id +
                ", processName='" + processName + '\'' +
                ", processType='" + processType + '\'' +
                ", limitTime='" + limitTime + '\'' +
                '}';
    }
}
