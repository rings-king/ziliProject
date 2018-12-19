package com.jux.mtqiushui.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.auth.domain.SysEmployee;
import com.jux.mtqiushui.auth.domain.Vo.EmployeeVo;
import com.jux.mtqiushui.auth.domain.Vo.SearchCondition;
import com.jux.mtqiushui.auth.service.EmployeeService;
import com.jux.mtqiushui.auth.util.MyPageable;
import com.jux.mtqiushui.auth.util.SearchFarmat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 添加新员工
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Map<String,Object> addEmployee(@RequestBody String param){

        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的部门
        Object jsonRole = jsonMap.get("addnew");
        // 把JSONObject的物料分类对象转成JavaBean的部门对象
        SysEmployee employee = JSONObject.toJavaObject((JSON) jsonRole, SysEmployee.class);

        //添加前判断此员工是否已存在
        if (employeeService.isExitempNumber(employee.getEmpNumber())){
            map.put("error","该员工编码已存在，请重新添加");
            return map;
        }

        try {
            SysEmployee employee1 = employeeService.addEmpolyee(employee);
            if (employee1 == null){
                map.put("error","添加失败");
                return map;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            map.put("error","服务器错误");
        }
        map.put("success",true);

        return map;
    }

    /**
     * 删除员工
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Map<String,Object> deleteEmp(@PathVariable("id") Long id){
        Map<String, Object> map = new HashMap<>();
       try {
           employeeService.deleteEmp(id);

       }catch (Exception e){
           System.out.println(e.getMessage());
           map.put("error","服务器错误");
           return map;
       }
       map.put("success",true);
        return map;
    }

    /**
     * 修改员工
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Map<String,Object> modify(@RequestBody String param){
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的部门
        Object jsonRole = jsonMap.get("update");
        // 把JSONObject的物料分类对象转成JavaBean的部门对象
        SysEmployee employee = JSONObject.toJavaObject((JSON) jsonRole, SysEmployee.class);

        try {
            SysEmployee employee1 = employeeService.modifyEmp(employee);
            if (employee1 == null){
                map.put("error","修改失败");
                return map;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            map.put("error","服务器错误");
            return map;
        }
        map.put("success",true);

        return map;
    }


    /**
     * 根据id获取该员工档案
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}" ,method = RequestMethod.GET)
    public Object getEmpMessage(@PathVariable("id") Long id){
        Map<String, Object> map = new HashMap<>();
        EmployeeVo empMeassage = employeeService.getEmpMeassage(id);

        if (empMeassage == null){
            map.put("error","查询失败,没有该员工");
            return map;
        }
       return empMeassage;
    }

    /**
     * 获取所有员工
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object getAllEmp(@PageableDefault(sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable){
        Pageable pageable1 = new PageRequest(pageable.getPageNumber() < 1 ? 0 : pageable.getPageNumber()-1,pageable.getPageSize(),pageable.getSort());
        Page<EmployeeVo> employees = null;
        Map<String, Object> map = new HashMap<>();


        try {
            employees = employeeService.getAllEmp(pageable1);

            if (employees == null){
                map.put("error","没有员工");
                return map;
            }
        }catch (Exception e){
            map.put("error","服务器错误");
            return map;
        }


        return employees;
    }

    /**
     * 通过员工id获取其姓名
     * @param id
     * @return
     */
    @RequestMapping(value = "/getEmpNameByid/{id}",method = RequestMethod.GET)
    public String getEmpNameByid(@PathVariable("id") Long id){
        String str = null;
        try {
            String empNameById = employeeService.getEmpNameById(id);
            str = empNameById;

        }catch (Exception e){
            str = "error";
        }
        return str;
   }

    /**
     * 多条件查询员工
     * @param conditionList
     * @return
     */
    @RequestMapping(value = "/searchEmp",method = RequestMethod.GET)
   public Object getEmployee(String conditionList,@PageableDefault(direction = Sort.Direction.DESC, value = 20) Pageable pageable){
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(conditionList)){
            map.put("error","条件为空请重新输入条件");
            return map;
        }
        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);
        List<EmployeeVo> employeeList = null;
        try {
            employeeList = employeeService.getSimpleEmp(newSearchConditions);
            if (employeeList.size()!=0) {
                MyPageable myPageable = new MyPageable(pageable.getPageSize() ,pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(),employeeList);
                return myPageable;
            }
        } catch (Exception e) {
            System.out.println("异常信息"+e.getMessage());
            map.put("error","服务器错误");
            return map;
        }
            map.put("error","该条件无效");
            return map;
   }


    /**
     * 通过员工的搜索条件查出员工 服务注册使用
     * @param conditionList
     * @return
     */
    @RequestMapping(value = "/searchEmpByCondition",method = RequestMethod.POST)
   public List<SysEmployee> searchEmpByCondition(@RequestBody List<SearchCondition> conditionList){

       List<SysEmployee> employeeList = new ArrayList<>();

       if (conditionList.size()!=0){
           employeeList = employeeService.searchEmp(conditionList);
       }

       return employeeList;
   }


}
