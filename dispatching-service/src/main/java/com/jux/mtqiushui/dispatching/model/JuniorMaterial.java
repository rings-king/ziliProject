package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

// 下级物料参数
public class JuniorMaterial {

    private Long id;

    // 下级物料编码
    private String materialChildCode;

    // 下级物料名称
    private String materialChildName;

    // 数量
    private String materialChildCount;

    // 单位
    private String materialChildUnit;

    // 型号
    private String materialChildModel;

    private MaterialDefine materialDefine;

    public JuniorMaterial() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialChildCode() {
        return materialChildCode;
    }

    public void setMaterialChildCode(String materialChildCode) {
        this.materialChildCode = materialChildCode;
    }

    public String getMaterialChildName() {
        return materialChildName;
    }

    public void setMaterialChildName(String materialChildName) {
        this.materialChildName = materialChildName;
    }

    public String getMaterialChildCount() {
        return materialChildCount;
    }

    public void setMaterialChildCount(String materialChildCount) {
        this.materialChildCount = materialChildCount;
    }

    public String getMaterialChildUnit() {
        return materialChildUnit;
    }

    public void setMaterialChildUnit(String materialChildUnit) {
        this.materialChildUnit = materialChildUnit;
    }

    public String getMaterialChildModel() {
        return materialChildModel;
    }

    public void setMaterialChildModel(String materialChildModel) {
        this.materialChildModel = materialChildModel;
    }

    public MaterialDefine getMaterialDefine() {
        return materialDefine;
    }

    public void setMaterialDefine(MaterialDefine materialDefine) {
        this.materialDefine = materialDefine;
    }

    @Override
    public String toString() {
        return "JuniorMaterial{" +
                "id=" + id +
                ", materialChildCode='" + materialChildCode + '\'' +
                ", materialChildName='" + materialChildName + '\'' +
                ", materialChildCount='" + materialChildCount + '\'' +
                ", materialChildUnit='" + materialChildUnit + '\'' +
                ", materialChildModel='" + materialChildModel + '\'' +
                '}';
    }
}
