package com.jux.mtqiushui.resources.model;

import java.util.Date;

/**
 * @Auther: fp
 * @Date: 2018/11/29 13:04
 * @Description:
 */
public class KanbanType {

    //物料号
    private String id;
    // 材质
    private String caizhi;
    private String zhuangxing;
    private Date creatTime;
    private String commens;
    private String status;

    @Override
    public String toString() {
        return "KanbanType{" +
          "id='" + id + '\'' +
          ", caizhi='" + caizhi + '\'' +
          ", zhuangxing='" + zhuangxing + '\'' +
          ", creatTime=" + creatTime +
          ", commens='" + commens + '\'' +
          ", status='" + status + '\'' +
          '}';
    }

    public KanbanType() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaizhi() {
        return caizhi;
    }

    public void setCaizhi(String caizhi) {
        this.caizhi = caizhi;
    }

    public String getZhuangxing() {
        return zhuangxing;
    }

    public void setZhuangxing(String zhuangxing) {
        this.zhuangxing = zhuangxing;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public String getCommens() {
        return commens;
    }

    public void setCommens(String commens) {
        this.commens = commens;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
