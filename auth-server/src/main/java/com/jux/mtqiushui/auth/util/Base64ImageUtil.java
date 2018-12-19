package com.jux.mtqiushui.auth.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Base64ImageUtil {
    /**
     * 二进制流转Base64字符串

     * <p>Title: byteArr2String</p>

     * @author Liyan

     * @date   2017年3月6日 下午3:04:47

     * @param byteArr

     * @return String

     * @throws IOException

     */
    public static String byteArr2String(byte[] byteArr) throws UnsupportedEncodingException {

        String str =  null;

        if (byteArr != null) {
            str =  new String(byteArr,"UTF-8");
        }
        return str;

    }

}
