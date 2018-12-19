package com.jux.mtqiushui.auth.service;

import com.jux.mtqiushui.auth.domain.DefaultSearch;
import com.jux.mtqiushui.auth.domain.SysRole;
import com.jux.mtqiushui.auth.domain.SysUser;
import com.jux.mtqiushui.auth.domain.Vo.SearchCondition;
import com.jux.mtqiushui.auth.domain.Vo.UserVo;
import com.jux.mtqiushui.auth.domain.Vo.XtgnbVo;
import com.jux.mtqiushui.auth.domain.Xtgnb;
import com.jux.mtqiushui.auth.repository.*;
import com.jux.mtqiushui.auth.util.SearchFarmat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
@Transactional
public class SysUserService {

    @Autowired
    private SysUserRepository userRepository;
    @Autowired
    private SysDeptRepository deptRepository;
    @Autowired
    private SysRoleRepository roleRepository;
    @Autowired
    private XtgnbRepository xtgnbRepository;
    @Autowired
    private XtgnbService xtgnbService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SearchConditionRepository searchConditionRepository;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    /**
     * 注册用户
     * @param user
     */
    public void addUser(SysUser user) {
        user.setUpdateTime(Calendar.getInstance().getTime());
        SysUser newUser = userRepository.save(user);
        //为新增用户设置默认角色
        userRepository.setUserRole(newUser.getId(),4l);
    }

    /**
     * 修改用户密码
     * @param userName
     * @param newPassword
     * @return
     */
    public SysUser modifyPassword(String userName, String newPassword) {
        //通过用户名从数据中取出该用户
        SysUser newUser = userRepository.findByUsername(userName);
        //设置新密码
        newUser.setPassword(encoder.encode(newPassword.trim()));
        SysUser save = userRepository.save(newUser);
        return save == null ? null : save;
    }

    /**
     * 修改邮箱
     * @param userName
     * @param newEmail
     * @return
     */
    public SysUser modifyEmail(String userName, String newEmail) {
        //逻辑同上
        SysUser newUser = userRepository.findByUsername(userName);
        newUser.setEmail(newEmail);
        SysUser save = userRepository.save(newUser);
        return save == null ? null : save;
    }

    /**
     * 根据用户名返回用户对象信息
     * @param userName
     * @return
     */
    public SysUser findUserIdByUsername(String userName) {
        SysUser byUsername = userRepository.findByUsername(userName);
        return byUsername;
    }

    /**
     * 判断邮箱是否存在
     * @param email
     * @return
     */
    public SysUser isExistsEmail(String email) {
        SysUser byEmail = userRepository.findByEmail(email);
        return byEmail;
    }


    /**
     * 判断手机号是否存在
     * @param phoneNumber
     * @return
     */
    public SysUser isExistsphone(String phoneNumber){
        SysUser byPhoneNum = userRepository.findByPhoneNum(phoneNumber);
        return byPhoneNum;
    }

    /**
     * 通过id获取部门名称
     * @param id
     * @return
     */
    public String getDeptName(String id){
        if (id==null || id.equals("")){
            return null;
        }
        String deptName = deptRepository.findDeptNameById(Long.valueOf(id));
        return deptName == null ? null : deptName;
    }


    /**
     * 获取用户所有信息清单（支持分页）
     * @param pageable
     * @return
     */
    public Page<SysUser> getUserList(Pageable pageable) throws Exception{
        Page<SysUser> users;
        users= userRepository.findAll(pageable);
        List<SysUser> userList = users.getContent();
        for (SysUser sysUser : userList) {
            String departmentId = sysUser.getDepartmentId();
            sysUser.setDepartmentId(departmentId);
            String deptName = getDeptName(departmentId);
            sysUser.setUserDepartName(deptName);
           //获取每个用户的角色
            List<String> userRoles = userRepository.getUserRole(sysUser.getId());
            sysUser.setRoleList(userRoles);

        }
        return users;
    }

    /*
    用户管理  新增用户 修改用户 删除用户
     */

    /**
     * 新增用户
     * @param userVo
     * @return
     */
    @Transactional
    public Boolean addNewUser(UserVo userVo)throws Exception{
       String pass = "123456";
       SysUser sysUser = new SysUser();
       //设置用户信息
       sysUser.setRealName(userVo.getRealName());
       String userEmail = userVo.getEmail();
       sysUser.setEmail(userEmail);
       sysUser.setPhoneNum(userVo.getPhoneNum());
       sysUser.setUsername(userVo.getUsername());
       if (userVo.getUserDepartName()==null || userVo.getUserDepartName().equals("")){
           sysUser.setDepartmentId(null);
       }else {
           sysUser.setDepartmentId(userVo.getUserDepartName());
       }

       sysUser.setPassword(encoder.encode(pass.trim()));
       sysUser.setUpdateTime(Calendar.getInstance().getTime());


        System.out.println("sysUser:+++"+sysUser);
       SysUser user1 = userRepository.save(sysUser);

        //设置角色
        List<Long> roles = userVo.getRoleId();

        if (roles == null || roles.equals("")){
            userRepository.setUserRole(sysUser.getId(),4l);
        }else {

            for (Long role : roles) {
                userRepository.setUserRole(sysUser.getId(),role);
            }
        }


        if (user1 == null){
            return false;
        }
        return true;
    }

    /**
     * 根据id获取用户信息
     * @param userId
     * @return
     */
    public UserVo getUserMessageByUserId(Long userId){

        UserVo userVo = new UserVo();
        //通过id获取用户对象
        SysUser sysUser = userRepository.findById(userId);
        //判断用户是否存在
        if (sysUser == null){
            return null;
         }
        String departmentId = sysUser.getDepartmentId();
        //判断用户是否拥有部门
        if (departmentId == null || departmentId.equals("")){
            //没有就将部门名称设置为空
           userVo.setUserDepartName(null);
        }else {
            //如果有就调用getDeptName 方法 获取部门名称
            userVo.setUserDepartName(getDeptName(departmentId));
            userVo.setUserDepartId(departmentId);
        }
        //获取用户角色数组
        List<String> userRoles = userRepository.getUserRole(userId);
        List<BigInteger> userRoleId = userRepository.getUserRoleId(userId);
        List<Long> userRole = new ArrayList<>();
        for (BigInteger bigInteger : userRoleId) {
            Integer integer = Integer.valueOf(bigInteger.toString());
            userRole.add(integer.longValue());
        }


        //将用户信息set进 vo 类
        userVo.setRoleList(userRoles);
        userVo.setId(userId);
        userVo.setEmail(sysUser.getEmail());
        userVo.setUsername(sysUser.getUsername());
        userVo.setRealName(sysUser.getRealName());
        userVo.setPhoneNum(sysUser.getPhoneNum());
        userVo.setPassword(sysUser.getPassword());
        userVo.setRoleId(userRole);

        System.out.println("****************"+userVo);
        return userVo;
    }


    /**
     * 修改用户信息
     * @param userVo
     * @return
     */
    @Transactional
    public boolean modifyUserMessage(UserVo userVo)throws Exception{

        System.out.println("userVo:----"+userVo);

        Long userId = userVo.getId();
        System.out.println("userID:"+userId);
        //通过id获取该对象原来的信息
        UserVo oldUserVo = getUserMessageByUserId(userId);
        System.out.println("oldUserVo: "+oldUserVo);

        //设置新的用户对象
        SysUser sysNeWUser = new SysUser();
        //设置id
        sysNeWUser.setId(userId);

        System.out.println("sysNeWUser:11111"+sysNeWUser);

        //设置密码
        if (userVo.getPassword() == null || userVo.getPassword().equals("")){
            //没有修改 密码是原来的
            SysUser sysUser = userRepository.findById(userId);
            //判断是否有该用户
            if (sysUser == null){
                return false;
            }

            sysNeWUser.setPassword(sysUser.getPassword());
            System.out.println("sysNeWUser:22222"+sysNeWUser);

        }else {
            //修改后 设置新的密码
            sysNeWUser.setPassword(encoder.encode(userVo.getPassword().trim()));
            System.out.println("sysNeWUser:33333"+sysNeWUser);
        }

       // 判断前端的数据是否修改
       if (userVo.getUserDepartName() == null || userVo.getUserDepartName().equals("")){
        //部门没有修改
           //将部门id设置为原部门id
           sysNeWUser.setDepartmentId(getDeptIdByName(oldUserVo.getUserDepartName()).toString());
           System.out.println("sysNeWUser:44444"+sysNeWUser);

       }else {
           //修改后设置为新获取的部门id
           sysNeWUser.setDepartmentId(userVo.getUserDepartName());

           System.out.println("sysNeWUser:66666"+sysNeWUser);
       }
       //用户名
       if (userVo.getUsername() == null || userVo.getUsername().equals("")){
           sysNeWUser.setUsername(oldUserVo.getUsername());
       }else {
           sysNeWUser.setUsername(userVo.getUsername());
       }
       //yx
       if (userVo.getEmail()==null|| userVo.getEmail().equals("")){
           sysNeWUser.setEmail(oldUserVo.getEmail());
       }else {
           sysNeWUser.setEmail(userVo.getEmail());
       }
       //电话
       if (userVo.getPhoneNum()==null||userVo.getPhoneNum().equals("")){
           sysNeWUser.setPhoneNum(oldUserVo.getPhoneNum());
       }else {
           sysNeWUser.setPhoneNum(userVo.getPhoneNum());
       }

       //真实姓名不允许修改
        sysNeWUser.setRealName(oldUserVo.getRealName());
       sysNeWUser.setUpdateTime(Calendar.getInstance().getTime());


        Set<SysRole> sysRoles = new HashSet<>();
       //判断该用户是否修改角色
        if (userVo.getRoleId() == null){
            List<Long> roleId = oldUserVo.getRoleId();
            for (Long aLong : roleId) {
                SysRole one = roleRepository.findOne(aLong);
                sysRoles.add(one);
            }
        }else {
            List<Long> roleId = userVo.getRoleId();
            for (Long aLong : roleId) {
                SysRole one = roleRepository.findOne(aLong);
                sysRoles.add(one);
            }
        }


        sysNeWUser.setRoles(sysRoles);

        //保存修改后的用户
        System.out.println("sysNeWUser：---------------------"+sysNeWUser);

        SysUser saveUser = userRepository.save(sysNeWUser);

       if (saveUser == null){
           return false;
       }

        return true;

    }

    /**
     * 通过用户的部门名称获取其部门id；
     * @param deptname
     * @return
     */
    private Long getDeptIdByName(String deptname){
       return  userRepository.getDeptIdByName(deptname);
    }



    /**
     * 删除用户
     * @param userId
     */
    @Transactional
    public void deleteUser(Long userId){
        //首先删除外键关联关系
        userRepository.deleteUserRole(userId);

            userRepository.delete(userId);

    }

    /**
     * 查询上级功能为0 的功能列表
     * @return
     */
    public List<Xtgnb> getAllSystem(){
        List<Xtgnb> allSysterm = xtgnbRepository.getAllSysterm();
        return allSysterm;
    }


    /**
     * 用户系统页面功能展示
     * @param userName
     * @return
     */
    public List<XtgnbVo> getUserXtgnb(String userName, String gnbm, List<Integer> menuList){
        SysUser user = findUserIdByUsername(userName);
        Long id = user.getId();
        //获取用户的角色id集合

        List<BigInteger> userRoleId = userRepository.getUserRoleId(id);
        List<Long> userRole = new ArrayList<>();
        for (BigInteger bigInteger : userRoleId) {
            Integer integer = Integer.valueOf(bigInteger.toString());
            userRole.add(integer.longValue());
        }


        List<String> rolePermisssionbyidList = roleRepository.findRolePermisssionbyid(userRole);


        String str = new String();
        for (int i = 0; i < rolePermisssionbyidList.size(); i++) {
           str += rolePermisssionbyidList.get(i)+",";

        }

        String[] split2 = str.split(",");


        List<String> list = new ArrayList<String>();
        for (int i=0; i<split2.length; i++) {
            if(!list.contains(split2[i])) {
                list.add(split2[i]);
            }
        }

        System.out.println("list"+list);



//
//        Set<String> sets = new HashSet<>();
//        List<String> idlist = new ArrayList<>();
//        //去重 用户角色id对应的功能列表
//        for (String permission : rolePermisssionbyidList) {
//            String[] split = permission.split(",");
//
//        }
//        List<String> newPermissions = new ArrayList<String>(sets);
//        System.out.println("/////"+newPermissions);
//        String ids = newPermissions.get(0);
//
//        System.out.println(ids+"-------------------");
//        String[] split = ids.split(",");
//        for (String s : split) {
//            idlist.add(s);
//        }


//
//        通过权限id集合 获取所有权限
//        List<Xtgnb> roleAuthotity = xtgnbRepository.getRoleAuthotity(menulist);
//
//        System.out.println("roleAuthotity:----"+roleAuthotity);
//
        System.out.println("menuList:**************************"+menuList);
        List<Xtgnb> byRule = getByRule(gnbm, menuList, list);


        //判断传来的数据？？？？
        List<XtgnbVo> xtgnbByxtblist = xtgnbService.getXtgnbByxtblist(byRule);

        String[] split1 = gnbm.split("%");


        List<XtgnbVo> xtmnbTree = xtgnbService.getXtmnbTree(byRule,  split1[0]);


        return xtmnbTree;


    }


    /**
     * 根据传来的节点获取其下所有功能编码，
     * @param gnbm
     * @return
     */
    public List<Xtgnb> getByRule (String gnbm,List<Integer> list,List<String> idlist){

        List<Xtgnb> xtgnbList = new ArrayList<>();

        if (gnbm != null && list != null && idlist != null){
            xtgnbList = xtgnbRepository.getByRuleOne(gnbm,list,idlist);

        }
       return xtgnbList;

    }

    /**
     * 多条件查询
     * @param conditionList
     * @return
     */
    public List<SysUser> getSimpleUser(List<SearchCondition> conditionList)throws Exception{


        List<SysUser> userBySimple = new ArrayList<>();

        if (conditionList == null){
            return null;
        }

        String aNew = " ";
        for (int i = 0; i < conditionList.size(); i++) {

            aNew += SearchFarmat.getNew(conditionList.get(i));

            if (i<conditionList.size()-1){
                aNew += "and " ;
            }

        }
        if (aNew.indexOf("realName")!=-1){
            aNew =   aNew.replace("realName","real_name");
        }
        if (aNew.indexOf("phoneNum")!=-1){
            aNew =  aNew.replace("phoneNum","phone_num");
        }
        if (aNew.indexOf("userDepartName")!=-1){
            aNew =  aNew.replace("userDepartName","d.id");
        }
        if (aNew.indexOf("roleId")!=-1){
            aNew =  aNew.replace("roleId","r.id");
        }



        String sql = null;
        //条件中有角色

                sql = "SELECT\n" +
                  "\tu.id,\n" +
                  "u.department_id,\n" +
                  "u.email,\n" +
                  "u.`password`,\n" +
                  "u.phone_num,\n" +
                  "u.real_name,\n" +
                  "u.username,\n" +
                  "u.update_time\n" +
                  "FROM\n" +
                  "\tsys_user u,\n" +
                  "\tsys_user_roles ur,\n" +
                  "  sys_role r,\n" +
                  "\tsys_dept d\n" +
                  "WHERE\n" +
                  "\tu.id = ur.sys_user_id\n" +
                  "AND\n" +
                  "\tur.roles_id = r.id\n" +
                  "AND\n" +
                  "u.department_id = d.id and" +aNew ;



        System.out.println("aNew:"+aNew);

        List<Long> idList = new ArrayList<>();


        System.out.println("sql:"+sql);
        List<SysUser> query = jdbcTemplate.query(sql, new RowMapper<SysUser>() {
            @Override
            public SysUser mapRow(ResultSet resultSet, int i) throws SQLException {
                SysUser sysUser = new SysUser();
                sysUser.setId(resultSet.getLong("id"));
                sysUser.setDepartmentId(resultSet.getString("department_id"));
                return sysUser;
            }
        });


        if (query.size()!=0){

            //取出查询到到的所有用户id
            for (SysUser sysUser : query) {
                Long id = sysUser.getId();
                idList.add(id);
            }
            userBySimple = userRepository.getUserBySimple(idList);
            System.out.println("ttt"+userBySimple);

            for (SysUser sysUser : userBySimple) {
                String departmentId = sysUser.getDepartmentId();
                String deptName = getDeptName(departmentId);
                sysUser.setUserDepartName(deptName);
                //获取每个用户的角色
                List<String> userRoles = userRepository.getUserRole(sysUser.getId());
                sysUser.setRoleList(userRoles);

            }
        }

        return userBySimple;
    }


    /**
     * 保存搜索自定义条件
     * @param searchCondition
     * @param userName
     * @return
     */
    @Transactional
    public Boolean saveCustomCondition(String searchCondition,String userName,String tableName) throws Exception{
        DefaultSearch defaultSearch = new DefaultSearch();
        defaultSearch.setUserName(userName);
        defaultSearch.setSearchConding(searchCondition);
        defaultSearch.setTableName(tableName);

        //存入默认条件
        //先查询是否已有条件
        DefaultSearch byUserNameAndTableName = searchConditionRepository.findByUserNameAndTableName(userName, tableName);
        if (byUserNameAndTableName!=null){
            //有条件删除
            searchConditionRepository.delete(byUserNameAndTableName);
        }

        DefaultSearch save = searchConditionRepository.save(defaultSearch);
        if (save!=null){
            return true;
        }
        return false;
    }


    /**
     * 载入搜索自定义条件
     * @param userName
     * @param tableName
     * @return
     */
    public String getCustomCondition(String userName,String tableName){
        DefaultSearch byUserNameAndTableName = searchConditionRepository.findByUserNameAndTableName(userName, tableName);
        String searchConding = null;
        if (byUserNameAndTableName!=null){
            searchConding = byUserNameAndTableName.getSearchConding();
        }

        return searchConding;
    }




}
