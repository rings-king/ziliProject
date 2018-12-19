package com.jux.mtqiushui.resources.model;

import javax.persistence.*;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate(value = true)
@Table(name = "materials")
public class Material {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @TableGenerator(name = "MaterialIdGen", table = "TABLE_GENERATOR", pkColumnValue = "Materials")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "MaterialIdGen")
    @Column(name = "id", nullable = false)
    private Long materialId;

    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @Column(name = "name", nullable = false)
    private String materialName;

    @Column(name = "code", nullable = true)
    private String materialCode;

    @Column(name = "model", nullable = true)
    private String materialModel;

    @Column(name = "comment", nullable = true)
    private String materialComment;

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialModel() {
        return materialModel;
    }

    public void setMaterialModel(String materialModel) {
        this.materialModel = materialModel;
    }

    public String getMaterialComment() {
        return materialComment;
    }

    public void setMaterialComment(String materialComment) {
        this.materialComment = materialComment;
    }

    public void copyNotNullFields(final Material materialVO) {
        if (materialVO.materialId != null) {
            this.materialId = materialVO.materialId;
        }
        if (materialVO.organizationId != null) {
            this.organizationId = materialVO.organizationId;
        }
        if (materialVO.materialName != null) {
            this.materialName = materialVO.materialName;
        }
        if (materialVO.materialCode != null) {
            this.materialCode = materialVO.materialCode;
        }
        if (materialVO.materialModel != null) {
            this.materialModel = materialVO.materialModel;
        }
        if (materialVO.materialComment != null) {
            this.materialComment = materialVO.materialComment;
        }
    }
}
