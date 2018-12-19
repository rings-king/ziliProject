package com.jux.mtqiushui.dispatching.util;

import redis.clients.jedis.Jedis;

public class RedisUtil {
    private  static  final Jedis jedis=new Jedis("localhost",6379);
    public static String getRedisByKey(Long id){
        String s = Long.toString(id);
        String s1 = jedis.get(s);
            return  s1;
    }
}
