package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.SearchCondition;
import com.jux.mtqiushui.dispatching.model.SysEmployee;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value ="auth-server")
public interface AuthServiceApi {

    //根据id查询对应员工姓名
    @RequestMapping(value = "/v1/employee/getEmpNameByid/{id}", method = RequestMethod.GET)
    public String getEmpNameByid(@PathVariable("id")  Long id);

    //根据操作工
    @RequestMapping(value = "/v1/employee/searchEmpByCondition",method = RequestMethod.POST)
    public List<SysEmployee> searchEmpByCondition(List<SearchCondition> conditionList);
}
