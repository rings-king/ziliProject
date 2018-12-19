package com.jux.mtqiushui.resources.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

// 物料分类
@Entity
@Table(name = "material_type",uniqueConstraints = {@UniqueConstraint(columnNames = {"material_type_id"})})
public class MaterialType {
    // 代理主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 类别编码
    @Column(name = "material_type_id")
    private String materialTypeId;

    // 类别名
    @Column(name = "material_type_name")
    private String materialTypeName;

    // 所属类别
    @Column(name = "parent_id")
    private Long parentId;

    // 所属类别名称
    @Column(name = "parent_name")
    private String parentName;

    // 更新时间
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Transient
    @JsonProperty("children")
    private Set<MaterialType> materialTypes;

    public MaterialType() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Set<MaterialType> getMaterialTypes() {
        return materialTypes;
    }

    public void setMaterialTypes(Set<MaterialType> materialTypes) {
        this.materialTypes = materialTypes;
    }

    @Override
    public String toString() {
        return "MaterialType{" +
                "id=" + id +
                ", materialTypeId='" + materialTypeId + '\'' +
                ", materialTypeName='" + materialTypeName + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", updateTime=" + updateTime +
                ", materialTypes=" + materialTypes +
                '}';
    }
}
