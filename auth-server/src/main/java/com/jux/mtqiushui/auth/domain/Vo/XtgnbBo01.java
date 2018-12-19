package com.jux.mtqiushui.auth.domain.Vo;

public class XtgnbBo01 {

    private String gnbm;
    private Integer menutype;
    private String scope;
    private String text;
    private String iconCls;
    private String handler;
    private String showbystatus;

    public XtgnbBo01() {
    }

    public Integer getMenutype() {
        return menutype;
    }

    public void setMenutype(Integer menutype) {
        this.menutype = menutype;
    }




    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getShowbystatus() {
        return showbystatus;
    }

    public void setShowbystatus(String showbystatus) {
        this.showbystatus = showbystatus;
    }

    public String getGnbm() {
        return gnbm;
    }

    public void setGnbm(String gnbm) {
        this.gnbm = gnbm;
    }

    @Override
    public String toString() {
        return "XtgnbBo01{" +
          "gnbm='" + gnbm + '\'' +
          ", menutype=" + menutype +
          ", scope='" + scope + '\'' +
          ", text='" + text + '\'' +
          ", iconCls='" + iconCls + '\'' +
          ", handler='" + handler + '\'' +
          ", showbystatus='" + showbystatus + '\'' +
          '}';
    }
}
