package com.jux.mtqiushui.auth.domain.Vo;

import java.util.List;

public class SearchVo {

    private SearchCondition single;

    private List<SearchVo> and;

    private List<SearchVo> or;

    public SearchVo() {
    }

    public SearchCondition getSingle() {
        return single;
    }

    public void setSingle(SearchCondition single) {
        this.single = single;

    }

    public List<SearchVo> getAnd() {
        return and;
    }

    public void setAnd(List<SearchVo> and) {
        this.and = and;
    }

    public List<SearchVo> getOr() {
        return or;
    }

    public void setOr(List<SearchVo> or) {
        this.or = or;
    }

    @Override
    public String toString() {
        return "SearchVo{" +
          "single=" + single +
          ", and=" + and +
          ", or=" + or +
          '}';
    }
}
