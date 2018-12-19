package com.jux.mtqiushui.auth.domain.Vo;

public class XtgnbBo02 {

    private String gnbm;
    private String beforeclick;
    private String afterclick;

    public XtgnbBo02() {
    }

    public String getGnbm() {
        return gnbm;
    }

    public void setGnbm(String gnbm) {
        this.gnbm = gnbm;
    }

    public String getBeforeclick() {
        return beforeclick;
    }

    public void setBeforeclick(String beforeclick) {
        this.beforeclick = beforeclick;
    }

    public String getAfterclick() {
        return afterclick;
    }

    public void setAfterclick(String afterclick) {
        this.afterclick = afterclick;
    }

    @Override
    public String toString() {
        return "XtgnbBo02{" +
          "gnbm='" + gnbm + '\'' +
          ", beforeclick='" + beforeclick + '\'' +
          ", afterclick='" + afterclick + '\'' +
          '}';
    }
}
