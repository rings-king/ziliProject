package com.jux.mtqiushui.resources.repository;

import com.jux.mtqiushui.resources.model.KanbanType;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Auther: fp
 * @Date: 2018/11/30 13:46
 * @Description:
 */
@FeignClient(value = "zili-server")
public interface ZiLiDataFeign {

    @RequestMapping(value = "/data/kanBanType", method = RequestMethod.GET)
    public List<KanbanType> getAllType();
}
