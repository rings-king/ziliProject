package com.jux.mtqiushui.dispatching.model;

public class SearchCondition {
    //表名
    private String fieldname;
    //字段名
    private String tablename;
    //逻辑符
    private String operator;
    //条件符
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
