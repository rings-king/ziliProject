package com.jux.mtqiushui.auth.domain.Vo;

import com.jux.mtqiushui.auth.domain.Quarter;

import java.util.ArrayList;
import java.util.List;

public class DepartQuartetVo {

    private Long id;
    private String departCode;
    private String departName;
    private Long departParentId;
    private List<Quarter> quarterDetailsTab = new ArrayList<>();

    public DepartQuartetVo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartCode() {
        return departCode;
    }

    public void setDepartCode(String departCode) {
        this.departCode = departCode;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public Long getDepartParentId() {
        return departParentId;
    }

    public void setDepartParentId(Long departParentId) {
        this.departParentId = departParentId;
    }


    public List<Quarter> getQuarterDetailsTab() {
        return quarterDetailsTab;
    }

    public void setQuarterDetailsTab(List<Quarter> quarterDetailsTab) {
        this.quarterDetailsTab = quarterDetailsTab;
    }

    @Override
    public String toString() {
        return "DepartQuartetVo{" +
          "id=" + id +
          ", departCode='" + departCode + '\'' +
          ", departName='" + departName + '\'' +
          ", departParentId=" + departParentId +
          ", quarterDetailsTab=" + quarterDetailsTab +
          '}';
    }

}
