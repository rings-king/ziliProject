package com.jux.mtqiushui.dispatching.model.statistic_analysis;

/**
 * @Auther: fp
 * @Date: 2018/12/3 13:27
 * @Description:
 */
public class Analysis {
    private String name;
    private Integer value;

    public Analysis() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Analysis{" +
          "name='" + name + '\'' +
          ", value=" + value +
          '}';
    }
}
