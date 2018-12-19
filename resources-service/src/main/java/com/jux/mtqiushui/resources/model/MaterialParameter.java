package com.jux.mtqiushui.resources.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

// 物料参数
@Entity
@Table(name = "material_parameter")
public class MaterialParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 参数名
    @Column(name = "material_params_name", nullable = false)
    private String materialParamsName;
    // 参数值
    @Column(name = "material_params_value", nullable = false)
    private String materialParamsValue;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH}, optional = true, targetEntity = MaterialDefine.class)
    @JoinColumn(name = "material_define_id")
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
