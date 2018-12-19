package com.jux.mtqiushui.dispatching.model;

import com.jux.mtqiushui.dispatching.model.statistic_analysis.Analysis;

import java.util.List;

/**
 * 每个类型的实体类 新建
 */
public class AnalysisTwo {

    //类型名称
    private  String typeName;
    //类型名称对应的物料名称以及产量
    private List<Analysis> list;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<Analysis> getList() {
        return list;
    }

    public void setList(List<Analysis> list) {
        this.list = list;
    }
}
