package com.jux.mtqiushui.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jux.mtqiushui.auth.domain.SysUser;
import com.jux.mtqiushui.auth.domain.Vo.SearchCondition;
import com.jux.mtqiushui.auth.domain.Vo.UserVo;
import com.jux.mtqiushui.auth.domain.Vo.XtgnbBo;
import com.jux.mtqiushui.auth.domain.Vo.XtgnbVo;
import com.jux.mtqiushui.auth.domain.Xtgnb;
import com.jux.mtqiushui.auth.service.SysUserService;
import com.jux.mtqiushui.auth.service.XtgnbService;
import com.jux.mtqiushui.auth.util.MyPageable;
import com.jux.mtqiushui.auth.util.SearchFarmat;
import com.jux.mtqiushui.auth.util.TokenCatch;
import com.jux.mtqiushui.auth.util.VerifyUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyunfei on 2017/6/12.
 */
@RestController
@RequestMapping(value = "v1/users")
public class UserServiceController {

    @Autowired
    private SysUserService userService;
    @Autowired
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices consumerTokenServices;
    @Autowired
    private XtgnbService xtgnbService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @GetMapping("/user")
    public Principal user(Principal user){
        return user;
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public Map<String,String>  registerUser(SysUser user){
        //密码加密
        user.setPassword(encoder.encode(user.getPassword().trim()));
        userService.addUser(user);
        Map<String,String> map = new HashMap<>();
        if (user == null) {
            map.put("error","注册失败");
            return map;
        }
        map.put("success","true");
        return map;
    }

    /**
     * 判断用户名是否存在
     * @param userName
     * @return
     */
    @RequestMapping(value = "/isExistsUserName", method = RequestMethod.GET)
    public Map<String, Boolean> isExistsUserName(String userName) {
        Map<String, Boolean> map = new HashMap<>();
        SysUser userIdByUsername = userService.findUserIdByUsername(userName);
        if (userIdByUsername != null) {
            map.put("success", true);
            return map;
        }
        map.put("error", false);
        return map;
    }


    /**
     * 判断邮箱是否存在
     * @param email
     * @return
     */
    @RequestMapping(value = "/isExistsEmail", method = RequestMethod.GET)
    public Map<String, String> isExistsEmail(String email) {
        System.out.println("*********"+email);
        Map<String, String> map = new HashMap<>();
        if (email.isEmpty()) {
            map.put("error", "邮箱不能为空");
            return map;
        }
        SysUser existsEmail = userService.isExistsEmail(email);
        if (existsEmail == null) {
            map.put("success", "true");
            return map;
        }
        map.put("error", "邮箱已存在");
        return map;
    }

    /**
     * 判断手机号是否存在
     * @param phoneNum
     * @return
     */
    @RequestMapping(value = "/isExistsPhone",method = RequestMethod.GET)
    public Map<String,String> isExistsPhone(String phoneNum){
        Map<String, String> map = new HashMap<>();
        if (phoneNum.isEmpty()){
            map.put("error", "手机号不能为空");
            return map;
        }
        SysUser existsphone = userService.isExistsphone(phoneNum);
        if (existsphone == null){
            map.put("success", "true");
            return map;
        }
        map.put("error", "手机号已存在");
        return map;
    }


    /**
     * 用户修改密码
     * @param map
     * @param user
     * @param access_token
     * @return
     */
    @RequestMapping(value = "/modifyPassword", method = RequestMethod.PUT)
    public Map<String, Object> modifyPassword(@RequestBody Map<String,String> map, Principal user, String access_token){
        Map<String, Object> maps = new HashMap<>();
        // 根据用户名找对象
        SysUser userIdByUsername = userService.findUserIdByUsername(user.getName());
        // 对前端传过来的原密码进行加密比较
        if (!encoder.matches(map.get("password"),userIdByUsername.getPassword())) {
            maps.put("error", "原密码输入错误");
            return maps;
        }
        // 修改新密码
        SysUser newPassword = userService.modifyPassword(user.getName(), map.get("newPassword"));
        if (newPassword == null) {
            maps.put("error", false);
            return maps;
        }
        maps.put("success", true);
        consumerTokenServices.revokeToken(access_token);
        return maps;
    }

    /**
     * 用户修改邮箱
     * @param map
     * @param user
     * @return
     */
    @RequestMapping(value = "/modifyEmail", method = RequestMethod.PUT)
    public Map<String, Object> modifyEmail(@RequestBody Map<String,String> map, Principal user) {
        Map<String, Object> maps = new HashMap<>();
        if (map.get("newEmail").isEmpty()) {
            maps.put("error", "新邮箱不能为空");
            return maps;
        }
        SysUser newEmail = userService.modifyEmail(user.getName(), map.get("newEmail"));
        if (newEmail == null) {
            maps.put("error", false);
            return maps;
        }
        maps.put("success", true);
        return maps;
    }

    /**
     * 根据token返回用户的Id
     * @param userName
     * @return
     */
    @RequestMapping(value = "/findUserIdByUsername", method = RequestMethod.GET)
    public Long findUserIdByUsername(String userName) {
        SysUser userIdByUsername = userService.findUserIdByUsername(userName);
        if (userIdByUsername == null) {
            return -1l;
        }

        return userIdByUsername.getId();
    }

    /**
     * 生成验证码接口
     * @param response
     * @param session
     * @throws Exception
     */
    @RequestMapping(value = "/valiCode",method = RequestMethod.GET)
    public void valiCode(HttpServletResponse response, HttpSession session) throws Exception{
        //利用图片工具生成图片
        //第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = VerifyUtil.createImage();
        //将验证码存入Session
        session.setAttribute("imageCode",objs[0]);
        //将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
    }

    /**
     * 校对验证码
     * @param vailCode
     * @param session
     * @return
     */
    @RequestMapping(value = "/proofCheckCode",method = RequestMethod.GET)
    public Map<String,String> proofCheckCode(String vailCode, HttpSession session) {
        String imageCode = (String) session.getAttribute("imageCode");
        Map<String,String> map = new HashMap<>();
        System.out.println(vailCode);
        if (vailCode.isEmpty()) {
            map.put("error","验证码不能为空");
            return map;
        }
        if (vailCode.equalsIgnoreCase(imageCode)) {
            map.put("success","true");
            return map;
        }
        map.put("error","验证码错误");
        return map;
    }

    /**
     * 获取用户信息表
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object getUserList(@PageableDefault(sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable){

        Pageable pageable1 = new PageRequest(pageable.getPageNumber() < 1 ? 0 : pageable.getPageNumber()-1,pageable.getPageSize(),pageable.getSort());
        Page<SysUser> sysUsers;
        try {
            sysUsers = userService.getUserList(pageable1);
        }catch (Exception e){
            Map<Object, Object> map = new HashMap<>();
            map.put("error",e);
            return map;
        }
        return sysUsers;
    }


    /*
        用户管理
     */


    /**
     * 管理员添加用户（包括设置角色，部门）
     * @param param
     * @return
     */
    @RequestMapping( method = RequestMethod.POST)
    public Map<String,Object> addNewUser( @RequestBody String param){
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的物料分类对象
        Object jsonMaterialType = jsonMap.get("addnew");
        // 把JSONObject的物料分类对象转成JavaBean的物料分类对象
        UserVo userVo  = JSONObject.toJavaObject((JSON) jsonMaterialType, UserVo.class);
        //判断用户名是否重复
        if (userService.findUserIdByUsername(userVo.getUsername())!=null){
            map.put("error","用户名已存在");
            return map;
        }
        if (userService.isExistsphone(userVo.getPhoneNum())!=null){
            map.put("error","手机号已存在");
            return map;
        }

        //判断邮箱是否存在
        if (userService.isExistsEmail(userVo.getEmail())!=null){
            map.put("error","邮箱已存在");
            return map;
        }
        if (userVo == null){
            map.put("error","内容不能为空");
            return map;
        }

        try {
            Boolean bl = userService.addNewUser(userVo);
            if (bl == false){
                map.put("error","用户保存失败");
                return map;
            }
        }catch (Exception e){
            map.put("error","请检查用户名，邮箱参数");
            return map;
        }


        map.put("success",true);
        return map;

    }


    /**
     * 管理员修改用户信息（包括修改角色，部门）
     * @param param
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Map<String,Object> changeUser(@RequestBody String param){
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的物料分类对象
        Object jsonMaterialType = jsonMap.get("update");
        // 把JSONObject的物料分类对象转成JavaBean的物料分类对象
        UserVo userVo  = JSONObject.toJavaObject((JSON) jsonMaterialType, UserVo.class);
        if (userVo == null){
            map.put("error","传递json对象有误");
        }
        try {
            boolean bl = userService.modifyUserMessage(userVo);
            if (!bl){
                map.put("error","修改失败");
                return map;
            }
        }catch (Exception e){
            System.out.println("e.getMessage::"+e.getMessage());
            map.put("error","服务器错误");
            return map;
        }
        map.put("success",true);

    return map;

    }

    /**
     * 根据id获取用户所有信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Object getUserMessageById(@PathVariable("userId") Long userId){

        System.out.println("```````````````````"+userId);
        Map<String,Object> map = new HashMap<>();
        if (userId == null){
            map.put("error","id不能为空");
            return map;
        }
        UserVo userVo = userService.getUserMessageByUserId(userId);
        if (userVo == null){
            map.put("error","查询出错,用户不存在，请检查用户id");
            return map;
        }
        return userVo;
    }


    /**
     * 删除用户
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public Object deleteUser(@PathVariable("userId") Long userId){
        Map<String, Object> map = new HashMap<>();
        map = new HashMap<>();
        if (userId == null){
            map.put("error","请选择要删除的对象");
            return map;
        }
        userService.deleteUser(userId);
        map.put("success","删除成功");
        return map;
    }

    /**
     * 获取所有功能列表树结构
     * @return
     */
    @RequestMapping(value = "/getAllGnb",method = RequestMethod.GET)
    public Object getAllGnb(HttpServletRequest req){
        String menuList = req.getParameter("menuList");
        List<Integer> mList = new ArrayList<>();
        if (menuList != null){
            String[] split = menuList.split(",");
            for (int i = 0; i < split.length; i++) {
                mList.add(Integer.parseInt(split[i]));
            }
            System.out.println("33333333333333333"+mList);
        }

        Map<String, Object> map = new HashMap<>();
        Object allgnbm = xtgnbService.getAllgnbm(mList);
        if (allgnbm == null){
            map.put("error","服务器错误");
            return map;
        }
        return allgnbm;
    }


    /**
     * 获取系统列表
     * @return
     */
    @RequestMapping(value = "/getAllSysterm",method = RequestMethod.GET)
    public Object getSystermList(){
        Map<String, Object> map = new HashMap<>();
        List<Xtgnb> allSystem = userService.getAllSystem();
         if (allSystem == null){
             map.put("error","没有任何系统");
         }
         xtgnbService.getAllId();

         return allSystem;
    }

    /**
     * 用户系统页面功能展示
     * @param user
     * @return
     */
    @RequestMapping(value = "/showUserSysList",method = RequestMethod.GET)
    public Object getAutherRoleXtgnb(Principal user,HttpServletRequest request){
        //获取用户登录系统id
        String systermId = request.getParameter("systermId");
        //将该id保存到本地缓存中
        //获取登陆用户id
        TokenCatch.setKey(user.getName(),systermId);
        //获取传来的跟节点
        String gnbm = request.getParameter("gnbm");

        String newgnbm =gnbm+"%";


        //获取传来的类别编号
        String menuList = request.getParameter("menuList");

        List<Integer> mList = new ArrayList<>();
        if (menuList != null){
            String[] split = menuList.split(",");
            for (int i = 0; i < split.length; i++) {
                mList.add(Integer.parseInt(split[i]));
            }
        }
        //根据用户id获取其下所有角色 及权限；

        Object userXtgnb = userService.getUserXtgnb(user.getName(),newgnbm,mList);


        return userXtgnb;
    }

    /**
     * 根据传来的根节点获取其下按钮
     * @param user
     * @param request
     * @return
     */
    @RequestMapping(value = "/showAllButton",method = RequestMethod.GET)
    public Object getXtgnbBo(Principal user,HttpServletRequest request) throws UnsupportedEncodingException {

        Map<String, Object> map = new HashMap<>();
        String gnbm = request.getParameter("gnbm");
        String newgnbm =gnbm+"%";
        //获取传来的类别编号
        String menuList = request.getParameter("menuList");

        List<Integer> mList = new ArrayList<>();
        if (menuList != null){
            String[] split = menuList.split(",");
            for (int i = 0; i < split.length; i++) {
                mList.add(Integer.parseInt(split[i]));
            }
        }
        //根据用户id获取其下所有角色 及权限；
        List<XtgnbVo> userXtgnb = userService.getUserXtgnb(user.getName(), newgnbm, mList);

        List<String> list = new ArrayList<>();
        for (XtgnbVo xtgnbVo : userXtgnb) {
            String gnbm1 = xtgnbVo.getGnbm();
            list.add(gnbm1);
        }

        if (list == null || list.size() == 0){
            map.put("error","通过传入的功能编码未能查出任何功能，请检查是否参数有误");
            return map;
        }
        XtgnbBo userXtgnbBo = xtgnbService.getUserXtgnbBo(list);
        if (userXtgnbBo == null){
            map.put("error","数据库错误");
            return map;
        }

        return userXtgnbBo;
    }

    /**
     * 多条件查询
     * @param conditionList
     * @return
     */
    @RequestMapping(value = "/searchUser",method = RequestMethod.GET)
    public Object getBySimpleConditions(String conditionList, @PageableDefault(direction = Sort.Direction.DESC, value = 20) Pageable pageable){
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isEmpty(conditionList)){
            map.put("error","条件为空请重新输入条件");
            return map;
        }
        System.out.println("conditionList:"+conditionList);
        List<SearchCondition> searchConditions = JSONObject.parseArray(conditionList, SearchCondition.class);
        List<SearchCondition> newSearchConditions = SearchFarmat.formatValue(searchConditions);

        List<SysUser> simpleUser = null;
        try {
            simpleUser = userService.getSimpleUser(newSearchConditions);

            if (simpleUser.size()!=0) {
                MyPageable myPageable = new MyPageable(pageable.getPageSize() ,pageable.getPageNumber() <= 1 ? 1 : pageable.getPageNumber(),simpleUser);
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
     *  保存自定义搜索条件
     * @param param 请求条件体
     * @param user
     * @return
     */
    @RequestMapping(value = "/saveCondition",method = RequestMethod.POST)
    public Object saveCustomCondition(@RequestBody String param,Principal user){
        Map<String, Object> map = new HashMap<>();
        // 使用fastjson解析前端传过来的json字符串
        JSONObject jsonObject = JSONObject.parseObject(param);
        // 使用Map集合接收jsonObject
        Map<String, Object> jsonMap = jsonObject;
        // 从map集合中获取addnew对应要添加的物料分类对象
        List<SearchCondition> dd = (List<SearchCondition>) jsonMap.get("condition");
        String rr = (String) jsonMap.get("tablename");

        Boolean aBoolean = null;
        try {
            aBoolean = userService.saveCustomCondition(dd.toString(),user.getName(),rr);
            if (aBoolean){
                map.put("success","已保存");
                return map;
            }
        } catch (Exception e) {
            System.out.println("异常信息"+e.getMessage());
            map.put("error","服务器异常");
            return map;
        }
        map.put("error","保存失败");
        return  map;
    }

    /**
     * 载入默认条件
     * @param tableName
     * @return
     */
    @RequestMapping(value = "/getCondition",method = RequestMethod.GET)
    public Object getCustomCondition(String tableName,Principal user){
        Map<String, Object> map = new HashMap<>();
        String customCondition = userService.getCustomCondition(user.getName(), tableName);
        if (customCondition!= null){
            return customCondition;
        }
        map.put("error","服务器错误");
        return null;
    }

}
