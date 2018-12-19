package com.jux.mtqiushui.auth.domain;
//22
import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class XtgnbPK implements Serializable {
    private Integer id;
    private String gnbm;
    private String gnsy;

    @Column(name = "id")
    @Id
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "gnbm")
    @Id
    public String getGnbm() {
        return gnbm;
    }

    public void setGnbm(String gnbm) {
        this.gnbm = gnbm;
    }

    @Column(name = "gnsy")
    @Id
    public String getGnsy() {
        return gnsy;
    }

    public void setGnsy(String gnsy) {
        this.gnsy = gnsy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XtgnbPK xtgnbPK = (XtgnbPK) o;
        return id == xtgnbPK.id &&
          Objects.equals(gnbm, xtgnbPK.gnbm) &&
          Objects.equals(gnsy, xtgnbPK.gnsy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gnbm, gnsy);
    }
}
