package com.jux.mtqiushui.resources.repository;

import com.jux.mtqiushui.resources.configuration.FeignConfig;
import feign.Param;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * 使用Feigin调用auth-server服务中的接口
 */
@FeignClient(value = "auth-server")
public interface AuthServiceApi {
    /**
     * 调用auth-server服务中的findUserIdByUsername接口
     * 根据token获得对应的用户信息
     * @return
     */
    @RequestMapping(value = "/v1/users/findUserIdByUsername",method = RequestMethod.GET)
    public Long getUserIdByUsername(@RequestParam("userName") String userName);

    /**
     * 调用auth-server服务中的/v1/organizations/{userId}接口
     * 根据用户Id返回对应的组织信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/v1/organizations/{userId}", method = RequestMethod.GET)
    public Long getOrgIdByUserId(@PathVariable("userId") Long userId);
}
