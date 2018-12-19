package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

// 物料参数
public class MaterialParameter {

    private Long id;

    // 参数名
    private String materialParamsName;
    // 参数值
    private String materialParamsValue;

    private MaterialDefine materialDefine;

    public MaterialParameter() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialParamsName() {
        return materialParamsName;
    }

    public void setMaterialParamsName(String materialParamsName) {
        this.materialParamsName = materialParamsName;
    }

    public String getMaterialParamsValue() {
        return materialParamsValue;
    }

    public void setMaterialParamsValue(String materialParamsValue) {
        this.materialParamsValue = materialParamsValue;
    }

    public MaterialDefine getMaterialDefine() {
        return materialDefine;
    }

    public void setMaterialDefine(MaterialDefine materialDefine) {
        this.materialDefine = materialDefine;
    }

    @Override
    public String toString() {
        return "MaterialParameter{" +
                "id=" + id +
                ", materialParamsName='" + materialParamsName + '\'' +
                ", materialParamsValue='" + materialParamsValue + '\'' +
                '}';
    }
}
