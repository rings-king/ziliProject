package com.jux.mtqiushui.auth.domain.Vo;

public class DepartmentVo {
    private Long departmentId;
    private String departmentName;

    public DepartmentVo() {
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "DepartmentVo{" +
          "departmentId=" + departmentId +
          ", departmentName='" + departmentName + '\'' +
          '}';
    }
}
