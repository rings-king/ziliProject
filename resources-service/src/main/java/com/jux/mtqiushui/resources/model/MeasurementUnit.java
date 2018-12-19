package com.jux.mtqiushui.resources.model;

import javax.persistence.*;
import java.util.Date;

// 计量单位
@Entity
@Table(name = "measurement_unit")
public class MeasurementUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 计量单位名称
    @Column(name = "measure_unit_name", nullable = false)
    private String measureUnitName;

    // 计量单位编号
    @Column(name = "measure_unit_code", nullable = false)
    private String measureUnitCode;

    // 更新时间
    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public MeasurementUnit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMeasureUnitName() {
        return measureUnitName;
    }

    public void setMeasureUnitName(String measureUnitName) {
        this.measureUnitName = measureUnitName;
    }

    public String getMeasureUnitCode() {
        return measureUnitCode;
    }

    public void setMeasureUnitCode(String measureUnitCode) {
        this.measureUnitCode = measureUnitCode;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "MeasurementUnit{" +
                "id=" + id +
                ", measureUnitName='" + measureUnitName + '\'' +
                ", measureUnitCode='" + measureUnitCode + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
