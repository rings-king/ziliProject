package com.jux.mtqiushui.auth.domain.Vo;

import java.util.List;

public class XtgnbVo {

    private String sjgbnm;
    private String gnbm;
    private Integer id;
    private Integer leaf;
    private Integer menutype;
    private String name;
    private Integer regiontype;
    //name
    private String text;


    private List<XtgnbVo> children;

    public String getSjgbnm() {
        return sjgbnm;
    }

    public void setSjgbnm(String sjgbnm) {
        this.sjgbnm = sjgbnm;
    }

    public List<XtgnbVo> getChildren() {
        return children;
    }

    public void setChildren(List<XtgnbVo> children) {
        this.children = children;
    }

    public XtgnbVo() {
    }

    public String getGnbm() {
        return gnbm;
    }

    public void setGnbm(String gnbm) {
        this.gnbm = gnbm;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }

    public Integer getMenutype() {
        return menutype;
    }

    public void setMenutype(Integer menutype) {
        this.menutype = menutype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRegiontype() {
        return regiontype;
    }

    public void setRegiontype(Integer regiontype) {
        this.regiontype = regiontype;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "XtgnbVo{" +
          "sjgbnm='" + sjgbnm + '\'' +
          ", gnbm='" + gnbm + '\'' +
          ", id=" + id +
          ", leaf=" + leaf +
          ", menutype=" + menutype +
          ", name='" + name + '\'' +
          ", regiontype=" + regiontype +
          ", text='" + text + '\'' +
          ", children=" + children +
          '}';
    }
}
