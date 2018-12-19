package com.jux.mtqiushui.zilidata.model;

/**
 * @Auther: fp
 * @Date: 2018/12/7 16:17
 * @Description:
 */
public class ResourceData {
    private Long id;
    private String materialName;
    private String materialModel;

    public ResourceData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "ResourceData{" +
          "id=" + id +
          ", materialName='" + materialName + '\'' +
          ", materialModel='" + materialModel + '\'' +
          '}';
    }
}
