package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.MaterialDefine;
import com.jux.mtqiushui.dispatching.model.SearchCondition;
import com.jux.mtqiushui.dispatching.model.statistic_analysis.Analysis;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * 使用Feigin调用resources-server服务中的接口
 */
@FeignClient(value = "resourcesservice")
public interface ResourcesServiceApi {
    //获得物料的产品名和规则编号
    @RequestMapping(value = "/v1/materialDefine/getMaterialNameAndMaterialModelById/{materialDefineId}", method = RequestMethod.GET)
    public Map<String, String> getMaterialNameAndMaterialModelById(@PathVariable("materialDefineId")  Long materialDefineId);

    // 根据主单位id获取对应的主单位名字
    @RequestMapping(value = "/v1/measurementUnit/getMeasurementUnitNameById/{measurementUnitId}", method = RequestMethod.GET)
    public String getMeasurementUnitNameById(@PathVariable("measurementUnitId") Long measurementUnitId);

    //根据产品和规则型号查询
    @RequestMapping(value = "/v1/materialDefine/getMaterialIdAndModel",method = RequestMethod.POST)
    public List<MaterialDefine> getMaterialIdAndModel(List<SearchCondition> conditionList);

    // 根据时间间隔统计不同类型的产量对比
    @RequestMapping(value = "/v1/materialDefine/statistic", method = RequestMethod.POST)
    public List<Analysis> getMaterialType(List<Analysis> analyses);

    @RequestMapping(value = "/v1/materialDefine/getByNum/{id}", method = RequestMethod.GET)
    public Map<String,String> getByNum(@PathVariable("id") Long id);

    @RequestMapping(value = "/v1/materialDefine/getMaterialByMaterialCode/{materialCode}", method = RequestMethod.GET)
    public Map<String, String> getMaterialByMaterialCode(@PathVariable("materialCode") String materialCode);

}
