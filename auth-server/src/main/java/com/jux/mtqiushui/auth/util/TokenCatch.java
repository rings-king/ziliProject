package com.jux.mtqiushui.auth.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.TimeUnit;


public class TokenCatch {

    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
      .build(new CacheLoader<String, String>() {
          //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载.
          @Override
          public String load(String s) throws Exception {
              return "null";
          }
      });


    public static void setKey(String key,String value){
      localCache.put(key,value);
    }
    public static String getValue(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)|| value == null){
                return null;
            }
            return value;
        }catch (Exception e){
            System.out.println("本地缓存异常");
        }
        return null;
    }

}
