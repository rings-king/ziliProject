package com.jux.mtqiushui.resources.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.resources.model.MaterialType;
import com.jux.mtqiushui.resources.model.MeasurementUnit;
import com.jux.mtqiushui.resources.model.NoticeManage;
import com.jux.mtqiushui.resources.model.SearchCondition;
import com.jux.mtqiushui.resources.services.NoticeManageService;
import com.jux.mtqiushui.resources.util.MyPageable;
import com.jux.mtqiushui.resources.util.SearchFarmat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

/**
 * @Auther: fp
 * @Date: 2018/11/23 17:27
 * @Description:
 */
@RestController
@RequestMapping("v1/noticeManage")
public class NoticeManageServiceController {

    @Autowired
    private NoticeManageService noticeManageService;

    /**
     * 查询所有通知管理 支持分页功能
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object findAllNoticeManage(@PageableDefault(value = 99999, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<NoticeManage> noticeManages;
        try {
            noticeManages = noticeManageService.getAllNoticeManage(pageable);
            return noticeManages;
        } catch (Exception e) {
            Map<Object, Object> map = new HashMap<>();
            map.put("error","服务器错误");
            return map;
        }

    }

    /**
     * 根据通知管理ID查询对应的通知信息 并设置为已读
     * @param noticeManageId
     * @return
     */
    @RequestMapping(value = "/{noticeManageId}", method = RequestMethod.GET)
    public Object findNoticeManageById(@PathVariable("noticeManageId") Long noticeManageId) {
        Map<Object, Object> map = new HashMap<>();
        // 判断物料类别是否为空或者是空的字符串
        if (noticeManageId == null) {
            map.put("error", "通知编号不能为空");
            return map;
        }

        try {
            NoticeManage noticeManageById = noticeManageService.getNoticeManageById(noticeManageId);
            if (noticeManageById == null) {
                map.put("error","该通知不存在");
                return map;
            }
            return noticeManageById;
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
    }

    /**
     * 添加一个通知管理
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> addNoticeManage(@RequestBody String param, Principal user) {
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的通知管理对象
        Object jsonMaterialType = jsonMap.get("addnew");
        // 把JSONObject的物料分类对象转成JavaBean的通知管理对象
        NoticeManage noticeManage = JSONObject.toJavaObject((JSON) jsonMaterialType, NoticeManage.class);
        if (noticeManage == null) {
            map.put("error","内容不能为空");
            return map;
        }

        noticeManage.setUpdateTime(Calendar.getInstance().getTime());
        noticeManage.setUserName(user.getName());
        try {
            NoticeManage resultNoticeManage = noticeManageService.addNoticeManage(noticeManage);
            if (resultNoticeManage != null) {
                map.put("success",true);
                return map;
            }
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
        map.put("error", "新增通知失败");
        return map;
    }

    /**
     * 根据通知管理删除对应的通知信息
     * @param noticeManageId
     * @return
     */
    @RequestMapping(value = "/{noticeManageId}", method = RequestMethod.DELETE)
    public Map<Object, Object> deleteNoticeManageById(@PathVariable("noticeManageId") Long noticeManageId) {
        Map<Object, Object> map = new HashMap<>();
        // 判断通知管理是否为空或者是空的字符串
        if (noticeManageId == null) {
            map.put("error", "通知编号不能为空");
            return map;
        }

        try {
            Boolean aBoolean = noticeManageService.deleteNoticeManage(noticeManageId);
            if (aBoolean == true) {
                map.put("success",true);
                return map;
            }
            map.put("error","删除通知失败");
            return map;
        } catch (Exception e) {
            map.put("error", "服务器错误");
            return map;
        }
    }

    /**
     * 修改通知管理
     * @param param
     * @param user
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Map<Object, Object> modifyNoticeManageById(@RequestBody String param, Principal user) {
        Map<Object, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 获取当前时间
        Date time = Calendar.getInstance().getTime();
        // 从map集合中获取物料定义的id
        Integer noticeManageId = (Integer) jsonMap.get("id");
        // 从map集合中获取update对应要添加的通知管理对象
        Object jsonMaterialType = jsonMap.get("update");
        // 把JSONObject的通知管理对象转成JavaBean的通知管理对象
        NoticeManage noticeManage = JSONObject.toJavaObject((JSON) jsonMaterialType, NoticeManage.class);
        if (noticeManage == null || noticeManageId == null) {
            map.put("error","内容不能为空");
            return map;
        }

        try {
            noticeManage.setUpdateTime(time);
            Boolean aBoolean = noticeManageService.modifyNoticeManage(noticeManageId.longValue(), noticeManage, user.getName());
            if (aBoolean == true) {
                map.put("success", true);
                return map;
            }
            map.put("error","修改失败");
            return map;
        } catch (Exception e) {
            map.put("error", "服务器错误");
            System.out.println(e.getMessage());
            return map;
        }
    }

    /**
     *
     * @param conditionList
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/searchNoticeManage", method = RequestMethod.GET)
    public Object searchNoticeManage(String conditionList, @PageableDefault(direction = Sort.Direction.DESC, value = 20) Pageable pageable) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(conditionList)){
            map.put("error","条件为空请重新输入条件");
            return map;
        }
        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);
        List<NoticeManage> noticeManages = null;
        try {
            noticeManages =  noticeManageService.getNoticeManageBySearch(newSearchConditions);
        } catch (Exception e) {
            System.out.println("***********:"+e.getMessage());
            map.put("error", "服务器错误");
            return map;
        }
        System.out.println("noticeManages***:"+noticeManages);
        MyPageable myPageable = new MyPageable(pageable.getPageSize() ,pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(),noticeManages);
        return myPageable;
    }
}
