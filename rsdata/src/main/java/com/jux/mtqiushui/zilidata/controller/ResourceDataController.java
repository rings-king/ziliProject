package com.jux.mtqiushui.zilidata.controller;

import com.jux.mtqiushui.zilidata.model.ResourceData;
import com.jux.mtqiushui.zilidata.service.ResourceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: fp
 * @Date: 2018/12/7 16:24
 * @Description:
 */
@RestController
//@RequestMapping("v1/rsdata")
public class ResourceDataController {

    @Autowired
    private ResourceDataService resourceDataService;

    @RequestMapping(value = "/getMaterialByMaterialCode/{materialCode}", method = RequestMethod.GET)
    public ResourceData getMaterialByMaterialCode(@PathVariable("materialCode") String materialCode) {
        return  resourceDataService.getResourceData(materialCode);
    }
}
