package com.jux.mtqiushui.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.auth.domain.SysRole;
import com.jux.mtqiushui.auth.domain.Vo.SearchCondition;
import com.jux.mtqiushui.auth.service.SysRoleService;
import com.jux.mtqiushui.auth.util.MyPageable;
import com.jux.mtqiushui.auth.util.SearchFarmat;
import com.jux.mtqiushui.auth.util.TokenCatch;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "v1/roles")
public class RolesController {
    @Autowired
    private SysRoleService roleService;

    /**
     * 添加角色包括初始权限
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Map<String,Object> addRole(@RequestBody String param, Principal user){
        String userName = user.getName();

        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的部门
        Object jsonRole = jsonMap.get("addnew");
        // 把JSONObject的物料分类对象转成JavaBean的部门对象
        SysRole role = JSONObject.toJavaObject((JSON) jsonRole, SysRole.class);
        SysRole newRole = new SysRole();

        //判断角色是否存在

        if (TokenCatch.getValue(user.getName())== null){
            map.put("error","用户没有选择所在系统");
            return map;
        }

        if (isExitRole(role.getName())){
            map.put("error","角色已存在请修改");
            return map;
        }

        //为每一个角色添加最顶级权限
        String rolePermission = role.getRolePermission();
        String value = TokenCatch.getValue(user.getName());
        String newRolemission = rolePermission +","+ value;

        //将传入的数据取出再次存入

        newRole.setRolePermission(newRolemission);
        newRole.setName(role.getName());
        newRole.setValue(role.getValue());


        try {
            SysRole sysRole = roleService.addRole(newRole);
            if (sysRole == null){
                map.put("error","添加失败，服务器有问题");
                return map;
            }
        }catch (Exception e){
            System.out.println("1111111111111111111"+e.getMessage());
        }
        map.put("success",true);
        return map;
    }

    /**
     * 判断角色是否存在
     * @param name
     * @return
     */
    public Boolean isExitRole(String name){
        Boolean b = false;
        try {
            SysRole role = roleService.getRole(name);
            if (role == null){
                b = false;
                return b;
            }
          b = true;
        }catch (Exception e){
            b = false;
            return b;
        }
           b = true;
        return b;
    }


    /**
     * 修改角色和权限
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Map<String,Object> modifyRole(@RequestBody String param){
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的部门
        Object jsonRole = jsonMap.get("update");
        // 把JSONObject的物料分类对象转成JavaBean的部门对象
        SysRole role = JSONObject.toJavaObject((JSON) jsonRole, SysRole.class);
        try {
             if (!roleService.modifyRole(role)){
                 map.put("error","修改失败");
                 return map;
             }
        }catch (Exception e){
            map.put("error","服务器错误");
            return map;
        }

        map.put("success",true);
        return map;

    }

    /**
     * 根据id删除该角色
     * @param id
     * @return
     */
    @RequestMapping(value ="/{id}" ,method = RequestMethod.DELETE)
    public Map<String,Object> removeRole(@PathVariable("id") Long id){
        Map<String, Object> map = new HashMap<>();
        try {
            roleService.deletRole(id);
        }catch (Exception e){
            map.put("error","服务器错误"+e.getMessage());
            return map;
        }
        map.put("success",true);

        return map;
    }

    /**
     * 获取所有角色 及其权限
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object getAllRoles(@PageableDefault(sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable){
        Pageable pageable1 = new PageRequest(pageable.getPageNumber() < 1 ? 0 : pageable.getPageNumber()-1,pageable.getPageSize(),pageable.getSort());
        Map<String, Object> map = new HashMap<>();
        Page<SysRole> allRoles;
          allRoles = roleService.getAllRoles(pageable1);
        if (allRoles == null){
            map.put("error","目前没有任何角色，请先添加");
            return map;
        }
        return allRoles;
    }

    /**
     * 通过id获取该角色 及其权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Object getOneRole(@PathVariable("id") Long id){
        Map<String, Object> map = new HashMap<>();
        SysRole role = new SysRole();
        try {
            role = roleService.getRoleById(id);
            if (role == null){
              map.put("error","没有该角色");
              return map;
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
            map.put("error", "服务器错误");
            return map;
        }

        return role;
    }


    /**
     * 多条件查询
     * @param conditionList
     * @return
     */
    @RequestMapping(value = "/searchRole",method = RequestMethod.GET)
    public Object getSearchConding(String conditionList, @PageableDefault(direction = Sort.Direction.DESC, value = 20) Pageable pageable){
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(conditionList)){
            map.put("error","条件为空请重新输入条件");
            return map;
        }
        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);

        List<SysRole> roleList = null;
        try {
            roleList = roleService.getSimpleRole(newSearchConditions);
            if (roleList.size()!=0) {
                MyPageable myPageable = new MyPageable(pageable.getPageSize() ,pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(),roleList);
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







}
