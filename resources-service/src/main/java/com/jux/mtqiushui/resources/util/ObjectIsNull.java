package com.jux.mtqiushui.resources.util;

import java.lang.reflect.Field;

public class ObjectIsNull {
    //判断该对象是否: 返回ture表示所有属性为null  返回false表示不是所有属性都是null
    public static boolean isAllFieldNull(Object obj) throws Exception{
        Class stuCla = (Class) obj.getClass();// 得到类对象
        Field[] fs = stuCla.getDeclaredFields();//得到属性集合
        int count = 0;
        for (Field f : fs) {//遍历属性
            f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
            Object val = f.get(obj);// 得到此属性的值
            if(val == null || val == "null") {//只要有1个属性不为空,那么就不是所有的属性值都为空
                count++;
            }
        }
        return count == fs.length ? true : false;
    }
}
