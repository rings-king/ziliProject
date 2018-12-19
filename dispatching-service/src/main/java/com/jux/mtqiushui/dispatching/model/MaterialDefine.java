package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// 物料定义
public class MaterialDefine {

    private Long id;
    // 物料编号
    private String materialCode;
    // 物料名称
    private String materialName;
    //  规格型号
    private String materialModel;
    // 主单位
    private Long unitId;
    // 主单位名字
    private String unitName;
    // 存放仓库
    private String whName;
    // 工艺路线代码
    private String techWarCode;
    // 检验方式
    private String checkType;
    // 图号
    private String drawingNo;
    // 详细信息
    private String materialComment;
    // 物料来源
    private String materialSource;

    // 所属分类ID
    private Long materialTypeId;

    // 所属分类名字
    private String materialTypeName;

    // 更新时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;


    private Set<MaterialParameter> materialParamsTab1 = new HashSet<>();

    private Set<JuniorMaterial> materialParamsTab2 = new HashSet<>();

    public MaterialDefine() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }


    public String getWhName() {
        return whName;
    }

    public void setWhName(String whName) {
        this.whName = whName;
    }

    public String getTechWarCode() {
        return techWarCode;
    }

    public void setTechWarCode(String techWarCode) {
        this.techWarCode = techWarCode;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getMaterialComment() {
        return materialComment;
    }

    public void setMaterialComment(String materialComment) {
        this.materialComment = materialComment;
    }

    public String getMaterialSource() {
        return materialSource;
    }

    public void setMaterialSource(String materialSource) {
        this.materialSource = materialSource;
    }


    public Long getMaterialTypeId() {
        return materialTypeId;
    }

    public void setMaterialTypeId(Long materialTypeId) {
        this.materialTypeId = materialTypeId;
    }

    public Set<MaterialParameter> getMaterialParamsTab1() {
        return materialParamsTab1;
    }

    public void setMaterialParamsTab1(Set<MaterialParameter> materialParamsTab1) {
        this.materialParamsTab1 = materialParamsTab1;
    }

    public Set<JuniorMaterial> getMaterialParamsTab2() {
        return materialParamsTab2;
    }

    public void setMaterialParamsTab2(Set<JuniorMaterial> materialParamsTab2) {
        this.materialParamsTab2 = materialParamsTab2;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    @Override
    public String toString() {
        return "MaterialDefine{" +
                "id=" + id +
                ", materialCode='" + materialCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", materialModel='" + materialModel + '\'' +
                ", unitId=" + unitId +
                ", unitName='" + unitName + '\'' +
                ", whName='" + whName + '\'' +
                ", techWarCode='" + techWarCode + '\'' +
                ", checkType='" + checkType + '\'' +
                ", drawingNo='" + drawingNo + '\'' +
                ", materialComment='" + materialComment + '\'' +
                ", materialSource='" + materialSource + '\'' +
                ", materialTypeId=" + materialTypeId +
                ", materialTypeName='" + materialTypeName + '\'' +
                ", updateTime=" + updateTime +
                ", materialParamsTab1=" + materialParamsTab1 +
                ", materialParamsTab2=" + materialParamsTab2 +
                '}';
    }
}
