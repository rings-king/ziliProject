package com.jux.mtqiushui.auth.domain.Vo;

public class EmployeeVo {

    private Long id;
    private String empNumber;
    private String empName;
    private String quarter;
    private Long quarterId;
    private String department;
    private Long departmentId;

    public EmployeeVo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Long getQuarterId() {
        return quarterId;
    }

    public void setQuarterId(Long quarterId) {
        this.quarterId = quarterId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "EmployeeVo{" +
          "id=" + id +
          ", empNumber='" + empNumber + '\'' +
          ", empName='" + empName + '\'' +
          ", quarter='" + quarter + '\'' +
          ", quarterId=" + quarterId +
          ", department='" + department + '\'' +
          ", departmentId=" + departmentId +
          '}';
    }
}
