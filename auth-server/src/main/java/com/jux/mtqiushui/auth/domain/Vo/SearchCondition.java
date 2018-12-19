package com.jux.mtqiushui.auth.domain.Vo;

public class SearchCondition {

    private String fieldname;
    private String tablename;
    private String operator;
    private String value;

    public SearchCondition() {
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SearchCondition{" +
          "fieldname='" + fieldname + '\'' +
          ", tablename='" + tablename + '\'' +
          ", operator='" + operator + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
}
