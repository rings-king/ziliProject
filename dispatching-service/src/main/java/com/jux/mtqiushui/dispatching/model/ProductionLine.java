package com.jux.mtqiushui.dispatching.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 产线定义
 */
@Entity
@Table(name = "production_line")
public class ProductionLine {

    //主键
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    //产线编号
    @Column(nullable = false,name = "production_line_code")
    private String productionLineCode;
    //负责人
    @Column(nullable = false,name = "production_line_principle")
    private String productionLinePrinciple;
    //产线名称
    @Column(nullable = false,name = "production_line_name")
    private String productionLineName;
    // 更新时间
    @Column(name = "update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    //一个产线有多个产线工位
    @OneToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY,mappedBy="productionLine")
    private  Set<Station> productionLineStationTab1=new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductionLineCode() {
        return productionLineCode;
    }

    public void setProductionLineCode(String productionLineCode) {
        this.productionLineCode = productionLineCode;
    }

    public String getProductionLinePrinciple() {
        return productionLinePrinciple;
    }

    public void setProductionLinePrinciple(String productionLinePrinciple) {
        this.productionLinePrinciple = productionLinePrinciple;
    }

    public String getProductionLineName() {
        return productionLineName;
    }

    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Set<Station> getProductionLineStationTab1() {
        return productionLineStationTab1;
    }

    public void setProductionLineStationTab1(Set<Station> productionLineStationTab1) {
        this.productionLineStationTab1 = productionLineStationTab1;
    }

    @Override
    public String toString() {
        return "ProductionLine{" +
                "id=" + id +
                ", productionLineCode='" + productionLineCode + '\'' +
                ", productionLinePrinciple='" + productionLinePrinciple + '\'' +
                ", productionLineName='" + productionLineName + '\'' +
                ", updateTime=" + updateTime +
                ", productionLineStationTab1=" + productionLineStationTab1 +
                '}';
    }
}
