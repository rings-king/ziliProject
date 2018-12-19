package com.jux.mtqiushui.resources.model;

public class SearchCondition {

    private String fieldname;
    private String tablename;
    private String operator;
    private Object value;

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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SearchCondition{" +
                "fieldname='" + fieldname + '\'' +
                ", tablename='" + tablename + '\'' +
                ", operator='" + operator + '\'' +
                ", value=" + value +
                '}';
    }
}
