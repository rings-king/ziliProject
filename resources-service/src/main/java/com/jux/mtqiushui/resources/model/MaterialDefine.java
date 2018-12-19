package com.jux.mtqiushui.resources.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// 物料定义
@Entity
@Table(name = "material_define")
public class MaterialDefine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    // 物料编号
    @Column(name = "material_code")
    private String materialCode;
    // 物料名称
    @Column(name = "material_name")
    private String materialName;
    //  规格型号
    @Column(name = "material_model")
    private String materialModel;
    // 主单位
    @Column(name = "unit_id")
    private Long unitId;
    // 主单位名字
    @Column(name = "unit_name")
    private String unitName;
    // 存放仓库
    @Column(name = "wh_name")
    private String whName;
    // 工艺路线代码
    @Column(name = "tech_war_code")
    private String techWarCode;
    // 检验方式
    @Column(name = "check_type")
    private String checkType;
    // 图号
    @Column(name = "drawing_no")
    private String drawingNo;
    // 详细信息
    @Column(name = "material_comment")
    private String materialComment;
    // 物料来源
    @Column(name = "material_source")
    private String materialSource;

    // 所属分类ID
    @Column(name = "material_type_id")
    private String materialTypeId;

    // 所属分类名字
    @Column(name = "material_type_name")
    private String materialTypeName;

    // 状态
    @Column(name = "status")
    private String status = "0";

    // 预留字段
    @Column(name = "yu_liu_zi_duan")
    private String yuLiuZiDuan;

    // 更新时间
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "materialDefine")
    @JsonIgnoreProperties("materialDefine")
    private Set<MaterialParameter> materialParamsTab1 = new HashSet<>();

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "materialDefine")
    @JsonIgnoreProperties("materialDefine")
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

    public String getMaterialTypeId() {
        return materialTypeId;
    }

    public void setMaterialTypeId(String materialTypeId) {
        this.materialTypeId = materialTypeId;
    }

    public String getMaterialTypeName() {
        return materialTypeName;
    }

    public void setMaterialTypeName(String materialTypeName) {
        this.materialTypeName = materialTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYuLiuZiDuan() {
        return yuLiuZiDuan;
    }

    public void setYuLiuZiDuan(String yuLiuZiDuan) {
        this.yuLiuZiDuan = yuLiuZiDuan;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
          ", status='" + status + '\'' +
          ", yuLiuZiDuan='" + yuLiuZiDuan + '\'' +
          ", updateTime=" + updateTime +
          ", materialParamsTab1=" + materialParamsTab1 +
          ", materialParamsTab2=" + materialParamsTab2 +
          '}';
    }

}
