package com.jux.mtqiushui.auth.domain;

import lombok.Data;
//11
import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
@Table(name = "xt_xtgnb")
@IdClass(XtgnbPK.class)
public class Xtgnb {
    @Id
    @Column(name = "id")
    private Integer id;
    @Id
    @Column(name = "gnbm")
    private String gnbm;
    @Id
    @Column(name = "gnsy")
    private String gnsy;
    @Column(name = "gnmc")
    private String gnmc;
    @Column(name = "gnms")
    private String gnms;
    @Column(name = "sjgnbm")
    private String sjgnbm;
    @Column(name = "mjbz")
    private Integer mjbz;
    @Column(name = "menulist")
    private Integer menuList;
    @Column(name = "rightflag")
    private Integer rightFlag;
    @Column(name = "menutype")
    private Integer menuType;
    @Column(name = "iconcls")
    private String iconcls;
    @Column(name = "beforeclick")
    private byte[] beforeclick;
    @Column(name = "click")
    private byte[] click;
    @Column(name = "afterclick")
    private byte[] afterclick;
    @Column(name = "showbystatus")
    private String showbystatus;
    @Column(name = "showbycolumns")
    private String showbycolumns;
    @Column(name = "regiontype")
    private Integer regiontype;
    @Transient
    private List<Xtgnb> children;


    public Xtgnb() {
    }

    @Override
    public String toString() {
        return "Xtgnb{" +
          "id=" + id +
          ", gnbm='" + gnbm + '\'' +
          ", gnsy='" + gnsy + '\'' +
          ", gnmc='" + gnmc + '\'' +
          ", gnms='" + gnms + '\'' +
          ", sjgnbm='" + sjgnbm + '\'' +
          ", mjbz=" + mjbz +
          ", menuList=" + menuList +
          ", rightFlag=" + rightFlag +
          ", menuType=" + menuType +
          ", iconcls='" + iconcls + '\'' +
          ", beforeclick=" + Arrays.toString(beforeclick) +
          ", click=" + Arrays.toString(click) +
          ", afterclick=" + Arrays.toString(afterclick) +
          ", showbystatus='" + showbystatus + '\'' +
          ", showbycolumns='" + showbycolumns + '\'' +
          ", regiontype=" + regiontype +
          '}';
    }
}
