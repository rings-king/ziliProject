package com.jux.mtqiushui.resources.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

// 下级物料参数
@Entity
@Table(name = "junior_material")
public class JuniorMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 下级物料编码
    @Column(name = "material_child_code", nullable = false)
    private String materialChildCode;

    // 下级物料名称
    @Column(name = "material_child_name", nullable = false)
    private String materialChildName;

    // 数量
    @Column(name = "material_child_count", nullable = false)
    private String materialChildCount;

    // 单位
    @Column(name = "material_child_unit", nullable = false)
    private String materialChildUnit;

    // 型号
    @Column(name = "material_child_model", nullable = false)
    private String materialChildModel;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH}, optional = true, targetEntity = MaterialDefine.class)
    @JoinColumn(name = "material_define_id")
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
