package com.jux.mtqiushui.zilidata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
/**
 * 启动类 读取sqlserver 数据库数据
 */
public class ZilidataServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZilidataServerApplication.class, args);
    }
}