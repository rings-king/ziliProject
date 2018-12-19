package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * 产线工位
 */
@Entity
@Table(name = "station")
public class Station {
    //主键
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    //工位序号
    @Column(nullable = false,name = "station_num")
    private  String stationNum;
    //工位名称
    @Column(nullable = false,name = "station_name")
    private  String stationName;
    //多个工位对应一个产线
    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH}, optional = true, targetEntity = ProductionLine.class)
    @JoinColumn(name = "production_line_id")
    private ProductionLine productionLine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStationNum() {
        return stationNum;
    }

    public void setStationNum(String stationNum) {
        this.stationNum = stationNum;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public ProductionLine getProductionLine() {
        return productionLine;
    }

    public void setProductionLine(ProductionLine productionLine) {
        this.productionLine = productionLine;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", stationNum='" + stationNum + '\'' +
                ", stationName='" + stationName + '\'' +
                '}';
    }
}
