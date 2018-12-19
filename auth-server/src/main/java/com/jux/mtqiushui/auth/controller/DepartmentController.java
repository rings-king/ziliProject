package com.jux.mtqiushui.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.auth.domain.Quarter;
import com.jux.mtqiushui.auth.domain.SysDept;
import com.jux.mtqiushui.auth.domain.Vo.DepartQuartetVo;
import com.jux.mtqiushui.auth.domain.Vo.DepartmentVo;
import com.jux.mtqiushui.auth.domain.Vo.SearchCondition;
import com.jux.mtqiushui.auth.service.SysDeptService;
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
@RequestMapping(value = "v1/departments")
public class DepartmentController {
    @Autowired
    private SysDeptService deptService;

    /**
     * 添加部门
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Map<String,Object>  addDept(@RequestBody String param){
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的部门
        Object jsonDepartment = jsonMap.get("addnew");
        // 把JSONObject的物料分类对象转成JavaBean的部门对象
        DepartQuartetVo quartetVo = JSONObject.toJavaObject((JSON) jsonDepartment, DepartQuartetVo.class);

        System.out.println("quartetVo:****"+quartetVo);

        try {
            SysDept deptById = deptService.getDeptById(quartetVo.getDepartParentId());
            if (deptById == null){
                map.put("error","上级部门不能为空");
                return map;
            }
        }catch (Exception e){
            System.out.println("sorry");
        }

        if (quartetVo == null){
            map.put("error","内容不能为空");
            return map;
        }
        try {
            SysDept codeDept = deptService.getDeptByCode(quartetVo.getDepartCode());
            if (codeDept != null) {
                map.put("error","添加失败,该部门编码已存在");
                return map;
            }
            SysDept deptByName = deptService.getDeptByName(quartetVo.getDepartName());
            if (deptByName != null){
                map.put("error","添加失败,该部门名称已存在");
                return map;
            }
        }catch (Exception e){
            map.put("error","服务器错误");
            return map;
        }
        SysDept sysDept = deptService.addDept(quartetVo);
        if (sysDept == null) {
            map.put("error","添加部门失败");
            return map;
        }
        map.put("success",true);
        return map;
    }


    /**
     * 获取所有部门信息
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object getAllDepartment(@PageableDefault(sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable){
        Pageable pageable1 = new PageRequest(pageable.getPageNumber() < 1 ? 0 : pageable.getPageNumber()-1,pageable.getPageSize(),pageable.getSort());
        Page<SysDept> departments;
        try {
            departments = deptService.getAllDept(pageable1);
        } catch (Exception e) {
            Map<Object, Object> map = new HashMap<>();
            map.put("error","服务器错误");
            return map;
        }
        return departments;
    }


    /**
     * 修改部门信息
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Map<String,Object> modifyDeptById(@RequestBody String param){
        System.out.println("-----:"+param);
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        System.out.println("ttttttttttt"+jsonMap);

        Integer deptid = (Integer) jsonMap.get("id");

        // 从map集合中获取addnew对应要添加的部门
        Object adepartMent = jsonMap.get("addnew");
        DepartQuartetVo addQuartetVo = JSONObject.toJavaObject((JSON) adepartMent, DepartQuartetVo.class);

        Object udepartMent = jsonMap.get("update");
        DepartQuartetVo updateQuartetVo = JSONObject.toJavaObject((JSON) udepartMent, DepartQuartetVo.class);

        Object rdepartMent = jsonMap.get("remove");
        DepartQuartetVo removeQuartetVo = JSONObject.toJavaObject((JSON) rdepartMent, DepartQuartetVo.class);
        List<DepartQuartetVo> departQuartetVos = new ArrayList<>();

        departQuartetVos.add(addQuartetVo);
        departQuartetVos.add(updateQuartetVo);
        departQuartetVos.add(removeQuartetVo);

        if (departQuartetVos == null ){
            map.put("error","内容不能为空");
            return map;
        }
        try {

            if (updateQuartetVo.getDepartParentId()!=null){
                if (!deptService.compareIdandParentid(updateQuartetVo,deptid.longValue())){
                    map.put("error", "上级部门不能和本身相同");
                    return map;
                }
            }

            if (updateQuartetVo.getDepartCode() != null){
                if (deptService.getDeptByCode(updateQuartetVo.getDepartCode()) != null){
                    map.put("error", "部门编号重复");
                    return map;
                }
            }

            if (updateQuartetVo.getDepartName() != null){
                if (deptService.getDeptByName(updateQuartetVo.getDepartName()) != null){
                    map.put("error", "用户名重复");
                    return map;
                }
            }
           deptService.modifyDept(departQuartetVos,deptid.longValue());
        }catch (Exception e){
            System.out.println("+++++++++++++++++++++++"+e.getMessage());
            map.put("error", "服务器错误");
            return map;
        }
        map.put("success", "true");
        return map;
    }

    /**
     * 根据部门id删除该部门
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Map<String,Object> deleteDept(@PathVariable("id") Long id){
        Map<String, Object> map = new HashMap<>();

        //判断该部门下是否有员工
        if (!deptService.getUserBydeptId(id)){
            map.put("error","该部门下存在员工或用户不允许删除");
            return map;
        }

        try {
            deptService.deleteDepartment(id);
        }catch (Exception e){
            System.out.println( e.getMessage());
            map.put("error","服务器错误");
            return map;
        }
        map.put("success",true);

        return map;
    }


    /**
     * 根据部门id获取该部门详细信息显示
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object getDeptById(@PathVariable("id") Long id){
        Map<String, Object> map = new HashMap<>();
        SysDept sysDept = new SysDept();

        try {
            sysDept = deptService.getDeptById(id);
        }catch (Exception e){
            map.put("error","服务器错误");
        }
        return sysDept;
    }


    /**
     * 获取所有部门名称：员工档案管理使用
     * @return
     */
    @RequestMapping(value = "/getDeptName",method = RequestMethod.GET)
    public Object getAllDeptName(){
        Map<String, Object> map = new HashMap<>();
        List<DepartmentVo> allDeptName = deptService.getAllDeptName();
        if (allDeptName == null){
            map.put("error","服务器错误查询失败");
            return map;
        }
        return allDeptName;
    }

    /**
     * 根据部门id获取其下所有岗位
     * @param id
     * @return
     */
    @RequestMapping(value = "/getQuarter/{deptId}",method = RequestMethod.GET)
    public Object getQuarterByDeptId(@PathVariable("deptId") Long id){
        Map<String, Object> map = new HashMap<>();
        List<Quarter> quarterBydeptId = new ArrayList<>();

        try {
            quarterBydeptId = deptService.getQuarterBydeptId(id);
            if (quarterBydeptId == null ||quarterBydeptId.size() == 0){
                map.put("error","该部门下没有岗位");
                return map;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            map.put("error","服务器错误");
            return map;
        }
      return quarterBydeptId;

    }

    /**
     * 部门搜索
     * @return
     */
    @RequestMapping(value = "/searchDept",method = RequestMethod.GET)
    public Object searchUser(String conditionList,@PageableDefault(direction = Sort.Direction.DESC, value = 20) Pageable pageable){
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(conditionList)){
          map.put("error","条件为空请重新输入条件");
          return map;
        }

        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);

        List<SysDept> sysDepts = null;
        try {
            sysDepts = deptService.getManySearch(newSearchConditions);
            if (sysDepts.size()!=0) {
                MyPageable myPageable = new MyPageable(pageable.getPageSize() ,pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(),sysDepts);
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
