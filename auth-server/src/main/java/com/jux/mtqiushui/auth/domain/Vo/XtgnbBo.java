package com.jux.mtqiushui.auth.domain.Vo;

import java.util.List;

public class XtgnbBo {

    private List<XtgnbBo01> viewlist;
    private List<XtgnbBo02> methodlist;

    public XtgnbBo() {
    }

    public List<XtgnbBo01> getViewlist() {
        return viewlist;
    }

    public void setViewlist(List<XtgnbBo01> viewlist) {
        this.viewlist = viewlist;
    }

    public List<XtgnbBo02> getMethodlist() {
        return methodlist;
    }

    public void setMethodlist(List<XtgnbBo02> methodlist) {
        this.methodlist = methodlist;
    }

    @Override
    public String toString() {
        return "XtgnbBo{" +
          "viewlist=" + viewlist +
          ", methodlist=" + methodlist +
          '}';
    }
}
