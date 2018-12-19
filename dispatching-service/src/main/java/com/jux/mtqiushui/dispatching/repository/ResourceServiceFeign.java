package com.jux.mtqiushui.dispatching.repository;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auther: fp
 * @Date: 2018/11/30 15:27
 * @Description:
 */
@FeignClient(name = "resourcesservice")
public interface ResourceServiceFeign {
    /**
     * 根据物料分类名称查询对应的ID
     * @param typeName
     * @return
     */
    @RequestMapping(value = "/v1/materialType/getMaterialTypeIdByName/{typeName}", method = RequestMethod.GET)
    public Long getMaterialTypeIdByName(@PathVariable("typeName") String typeName);

    /**
     * 根据计量单位名称查询对应的计量单位对象
     * @param unitName
     * @return
     */
    @RequestMapping(value = "/v1/measurementUnit/findByMeasureUnitName/{unitName}", method = RequestMethod.GET)
    public Long findByMeasureUnitName(@PathVariable("unitName") String unitName);

}
