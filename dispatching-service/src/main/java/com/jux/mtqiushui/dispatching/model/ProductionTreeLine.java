package com.jux.mtqiushui.dispatching.model;


public class ProductionTreeLine {

    //新增产线名称字段
    private  String productionLineName;
    //新增工位字段
    private  String stationName;
    //新增工序字段
    private  String processName;
    //新增操作工字段
    private  String openName;

    public String getProductionLineName() {
        return productionLineName;
    }

    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getOpenName() {
        return openName;
    }

    public void setOpenName(String openName) {
        this.openName = openName;
    }

    @Override
    public String toString() {
        return "ProductionTreeLine{" +
                "productionLineName='" + productionLineName + '\'' +
                ", stationName='" + stationName + '\'' +
                ", processName='" + processName + '\'' +
                ", openName='" + openName + '\'' +
                '}';
    }
}
