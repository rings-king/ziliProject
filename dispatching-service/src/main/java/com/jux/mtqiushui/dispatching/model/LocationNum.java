package com.jux.mtqiushui.dispatching.model;

import javax.persistence.*;
import java.util.Date;

/**
 * 工位存储每次的进度及其编号 和时间
 */
@Table(name = "location_num")
@Entity
public class LocationNum {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    //工位id
    @Column(name = "station_id",nullable = false)
    private  Long stationId;


    //工位编号
    @Column(name = "station_number",nullable = false)
    private  String stationNumber;

    //时间
    @Column(name = "dupdate_time", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(String stationNumber) {
        this.stationNumber = stationNumber;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "LocationNum{" +
                "id=" + id +
                ", stationId=" + stationId +
                ", stationNumber='" + stationNumber + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
