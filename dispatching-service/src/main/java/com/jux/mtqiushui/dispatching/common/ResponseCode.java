package com.jux.mtqiushui.dispatching.common;

/**
 * @Auther: fp
 * @Date: 2018/12/3 08:52
 * @Description:
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),

    ERROR(1, "ERROR"),

    //需要登陆
    NEED_LOGIN(10, "NEED_LOGIN"),

    //非法参数
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;


    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
