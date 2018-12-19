package com.jux.mtqiushui.dispatching.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// 设备定义
@Entity
@Table(name = "device_define")
public class DeviceDefine implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 设备编码
    @Column(name = "device_code", nullable = false)
    private String deviceCode;

    // 设备名称
    @Column(name = "device_name", nullable = false)
    private String deviceName;

    // 设备型号
    @Column(name = "device_model", nullable = false)
    private String deviceModel;

    // 设备IP
    @Column(name = "device_ip", nullable = false)
    private String deviceIp;

    // 设备通讯地址
    @Column(name = "device_address", nullable = false)
    private String deviceAddress;

    // 所属产线Id
    @Column(name = "production_line_id", nullable = false)
    private Long productionLineId;
    // 所属产线
    @Column(name = "production_line", nullable = true)
    private String productionLine;

    // 所属工位Id
    @Column(name = "station_id", nullable = false)
    private Long stationId;
    // 所属工位
    @Column(name = "station_name", nullable = true)
    private String stationName;

    // 更新时间
    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "deviceDefine")
    private Set<DeviceParam> deviceParamsTab = new HashSet<>();

    public DeviceDefine() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public Long getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(Long productionLineId) {
        this.productionLineId = productionLineId;
    }

    public String getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(String productionLine) {
        this.productionLine = productionLine;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Set<DeviceParam> getDeviceParamsTab() {
        return deviceParamsTab;
    }

    public void setDeviceParamsTab(Set<DeviceParam> deviceParamsTab) {
        this.deviceParamsTab = deviceParamsTab;
    }

    @Override
    public String toString() {
        return "DeviceDefine{" +
                "id=" + id +
                ", deviceCode='" + deviceCode + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", deviceIp='" + deviceIp + '\'' +
                ", deviceAddress='" + deviceAddress + '\'' +
                ", productionLineId=" + productionLineId +
                ", productionLine='" + productionLine + '\'' +
                ", stationId=" + stationId +
                ", stationName='" + stationName + '\'' +
                ", updateTime=" + updateTime +
                ", deviceParamsTab=" + deviceParamsTab +
                '}';
    }
}
