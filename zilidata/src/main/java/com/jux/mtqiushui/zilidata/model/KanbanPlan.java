package com.jux.mtqiushui.zilidata.model;

import java.util.Date;

/**
 * @Auther: fp
 * @Date: 2018/11/29 17:28
 * @Description:
 */
public class KanbanPlan {
    //计划单号
    private String planno;
    //创建时间
    private Date createtime;
    //预计完成时间
    private Date finishdate;
    //工段
    private String gongduan;
    //物料号
    private String liaohao;
    //材质
    private String caizhi;
    //砖型
    private String zhuanxing;
    //单位
    private String unit;

    //数量
    private String amount;

    public KanbanPlan() {
    }

    public String getPlanno() {
        return planno;
    }

    public void setPlanno(String planno) {
        this.planno = planno;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getFinishdate() {
        return finishdate;
    }

    public void setFinishdate(Date finishdate) {
        this.finishdate = finishdate;
    }

    public String getGongduan() {
        return gongduan;
    }

    public void setGongduan(String gongduan) {
        this.gongduan = gongduan;
    }

    public String getLiaohao() {
        return liaohao;
    }

    public void setLiaohao(String liaohao) {
        this.liaohao = liaohao;
    }

    public String getCaizhi() {
        return caizhi;
    }

    public void setCaizhi(String caizhi) {
        this.caizhi = caizhi;
    }

    public String getZhuanxing() {
        return zhuanxing;
    }

    public void setZhuanxing(String zhuanxing) {
        this.zhuanxing = zhuanxing;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "KanbanPlan{" +
          "planno='" + planno + '\'' +
          ", createtime=" + createtime +
          ", finishdate=" + finishdate +
          ", gongduan='" + gongduan + '\'' +
          ", liaohao='" + liaohao + '\'' +
          ", caizhi='" + caizhi + '\'' +
          ", zhuanxing='" + zhuanxing + '\'' +
          ", unit='" + unit + '\'' +
          ", amount='" + amount + '\'' +
          '}';
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
