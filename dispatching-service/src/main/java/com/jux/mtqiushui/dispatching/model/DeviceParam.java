package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.io.Serializable;

// 设备参数
@Entity
@Table(name = "device_param")
@Proxy(lazy = false)
public class DeviceParam implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 名称
    @Column(name = "device_params_name", nullable = false)
    private String deviceParamsName;

    // 寄存器地址
    @Column(name = "register_address", nullable = false)
    private String registerAddress;

    // 类型
    @Column(name = "device_params_type", nullable = false)
    private String deviceParamsType;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH}, optional = true, targetEntity = DeviceDefine.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "device_define_id")
    private DeviceDefine deviceDefine;

    public DeviceParam() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceParamsName() {
        return deviceParamsName;
    }

    public void setDeviceParamsName(String deviceParamsName) {
        this.deviceParamsName = deviceParamsName;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }

    public String getDeviceParamsType() {
        return deviceParamsType;
    }

    public void setDeviceParamsType(String deviceParamsType) {
        this.deviceParamsType = deviceParamsType;
    }

    public DeviceDefine getDeviceDefine() {
        return deviceDefine;
    }

    public void setDeviceDefine(DeviceDefine deviceDefine) {
        this.deviceDefine = deviceDefine;
    }

    @Override
    public String toString() {
        return "DeviceParam{" +
                "id=" + id +
                ", deviceParamsName='" + deviceParamsName + '\'' +
                ", registerAddress='" + registerAddress + '\'' +
                ", deviceParamsType='" + deviceParamsType + '\'' +
                '}';
    }
}
