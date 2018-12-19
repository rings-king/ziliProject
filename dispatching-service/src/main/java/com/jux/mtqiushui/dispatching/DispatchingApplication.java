package com.jux.mtqiushui.dispatching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableFeignClients
//@EnableCircuitBreaker
@EnableScheduling
public class DispatchingApplication {

    public static void main(String[] args) {
         SpringApplication.run(DispatchingApplication.class, args);

    }
}
