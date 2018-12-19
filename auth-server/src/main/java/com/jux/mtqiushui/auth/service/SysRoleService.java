package com.jux.mtqiushui.auth.service;

import com.jux.mtqiushui.auth.domain.SysRole;
import com.jux.mtqiushui.auth.domain.Vo.SearchCondition;
import com.jux.mtqiushui.auth.domain.Vo.XtgnbVo;
import com.jux.mtqiushui.auth.domain.Xtgnb;
import com.jux.mtqiushui.auth.repository.SysRoleRepository;
import com.jux.mtqiushui.auth.repository.XtgnbRepository;
import com.jux.mtqiushui.auth.util.SearchFarmat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class SysRoleService {
    @Autowired
    private SysRoleRepository roleRepository;
    @Autowired
    private XtgnbService xtgnbService;
    @Autowired
    private XtgnbRepository xtgnbRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加角色
     *
     * @param sysRole
     * @return
     * @throws Exception
     */
    @Transient
    public SysRole addRole(SysRole sysRole) throws Exception {
        sysRole.setUpdateTime(Calendar.getInstance().getTime());
        SysRole save = roleRepository.save(sysRole);
        return save;
    }


    /**
     * 修改角色
     *
     * @param sysRole
     * @return
     * @throws Exception
     */
    @Transient
    public Boolean modifyRole(SysRole sysRole) throws Exception {
        //取出原数据
        Long id = sysRole.getId();
        SysRole oldRole = roleRepository.findOne(id);
        //判断是否修改了的数据
        if (sysRole.getRolePermission() != null || (!StringUtils.isEmpty(sysRole.getRolePermission()))) {
            oldRole.setRolePermission(sysRole.getRolePermission());
        }
        if (sysRole.getName() != null) {
            oldRole.setName(sysRole.getName());
        }
        if (sysRole.getValue() != null) {
            oldRole.setValue(sysRole.getValue());
        }
        //跟新时间
        oldRole.setUpdateTime(Calendar.getInstance().getTime());
        SysRole newRole = roleRepository.save(oldRole);
        if (newRole == null) {
            return false;
        }
        return true;

    }


    /**
     * 根据id删除该角色
     *
     * @param id
     * @throws Exception
     */
    @Transient
    public void deletRole(Long id) throws Exception {
        //删除外键关系
        Integer integer = roleRepository.deleteUserRole(id);

        System.out.println(integer + "hhh");
        //删除角色
        roleRepository.delete(id);

    }

    /**
     * 获取所有角色及其权限
     *
     * @return
     */
    public Page<SysRole> getAllRoles(Pageable pageable){
        Page<SysRole> all = roleRepository.findAll(pageable);

        List<SysRole> roles = all.getContent();

        System.out.println("1111111111111111111111111111111111111111" + roles);
        List<String> rolelist = new ArrayList<>();
        String[] rolePerm = null;

        // 将前端传来的字符串转化为list集合
        for (SysRole role : roles) {
            String rolePermission = role.getRolePermission();

            System.out.println("rolePermission:" + rolePermission);


            if (rolePermission == null || rolePermission.equals("")) {
                role.setXtgnbList(null);
            } else {
                rolePerm = rolePermission.split(",");
            }

            if (rolePerm == null || rolePerm.length == 0) {
                rolelist = null;
            } else {
                rolelist = Arrays.asList(rolePerm);
            }


            List<String> gnmcById = xtgnbRepository.getGnmcMessageById(rolelist);

            System.out.println("gnmcById" + gnmcById);

            List newList = new ArrayList();
            Set set = new HashSet();
            for (Iterator iter = gnmcById.iterator(); iter.hasNext(); ) {
                Object element = iter.next();
                if (set.add(element))
                    newList.add(element);
            }
            gnmcById.clear();
            gnmcById.addAll(newList);

            System.out.println("gnmcById:" + gnmcById);


            role.setXtgnbText(StringUtils.join(gnmcById, ","));


            System.out.println("rolelist:-----------++++++++++" + rolelist);

            //通过权限id集合 获取所有权限
            List<Xtgnb> xtgnbList = xtgnbRepository.getRoleAuthotity(rolelist);

            System.out.println("xtgnbList:-----++++" + xtgnbList);

            List<XtgnbVo> xtgnbByxtblist = xtgnbService.getXtgnbByxtblist(xtgnbList);

            System.out.println("xtgnbByxtblist:：：：：：：：：：：：" + xtgnbByxtblist);
            //将权限注入每个角色
            role.setXtgnbList(xtgnbByxtblist);
        }

        return all;
    }

    /**
     * 通过角色名查询该角色
     *
     * @param name
     * @return
     */
    public SysRole getRole(String name) throws Exception {
        //调用接口中方法查询该角色
        SysRole byName = roleRepository.findByName(name);
        return byName;
    }


    /**
     * 通过id获取该角色详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    public SysRole getRoleById(Long id) throws Exception {
        SysRole sysRole = roleRepository.findOne(id);
        if (sysRole == null) {
            return null;
        }
        String rolePermission = sysRole.getRolePermission();
        //判断角色下是否有功能权限
        if (rolePermission == null) {
            sysRole.setXtgnbList(null);
        }

        //将保存功能权限的id字符串 转化成string 集合
        String[] roleSplit = rolePermission.split(",");
        List<String> roles = Arrays.asList(roleSplit);
        System.out.println("roles:--------" + roles);

        List<Xtgnb> roleAuthotity = xtgnbRepository.getRoleAuthotity(roles);
        List<XtgnbVo> xtgnbByxtblist = xtgnbService.getXtgnbByxtblist(roleAuthotity);
        System.out.println("xtgnbByxtblist：+++++++++++++" + xtgnbByxtblist);
        sysRole.setXtgnbList(xtgnbByxtblist);

        return sysRole;
    }


    public List<SysRole> getSimpleRole(List<SearchCondition> conditionList) throws Exception {
        List<SysRole> roleList = new ArrayList<>();

        if (conditionList == null) {
            return null;
        }

        String aNew = " ";
        for (int i = 0; i < conditionList.size(); i++) {

            aNew += SearchFarmat.getNew(conditionList.get(i));

            if (i < conditionList.size() - 1) {
                aNew += "and ";
            }

        }

        if (aNew.indexOf("roleName")!=-1){
            aNew =   aNew.replace("roleName","name");
        }
        if (aNew.indexOf("roleDescription")!=-1){
            aNew =  aNew.replace("roleDescription","value");
        }
        String sql = null;

        sql = "select * from sys_role where " + aNew ;

        System.out.println(sql+"sql");

        List<Long> idList = new ArrayList<>();

        List<SysRole> query = jdbcTemplate.query(sql, new RowMapper<SysRole>() {
            @Override
            public SysRole mapRow(ResultSet resultSet, int i) throws SQLException {
                SysRole sysRole = new SysRole();
                sysRole.setId(resultSet.getLong("id"));
                return sysRole;
            }
        });

        if (query.size() != 0) {

            //取出查询到到的所有用户id
            for (SysRole sysRole : query) {
                Long id = sysRole.getId();
                idList.add(id);
            }

            roleList = roleRepository.findRoleById(idList);

            List<String> roles = new ArrayList<>();
            String[] rolePerm = null;
            for (SysRole role : roleList) {
                String rolePermission = role.getRolePermission();

                System.out.println("rolePermission:" + rolePermission);


                if (rolePermission == null || rolePermission.equals("")) {
                    role.setXtgnbList(null);
                } else {
                    rolePerm = rolePermission.split(",");
                }

                if (rolePerm == null || rolePerm.length == 0) {
                    roles = null;
                } else {
                    roles = Arrays.asList(rolePerm);
                }


                List<String> gnmcById = xtgnbRepository.getGnmcMessageById(roles);

                System.out.println("gnmcById" + gnmcById);

                List newList = new ArrayList();
                Set set = new HashSet();
                for (Iterator iter = gnmcById.iterator(); iter.hasNext(); ) {
                    Object element = iter.next();
                    if (set.add(element))
                        newList.add(element);
                }
                gnmcById.clear();
                gnmcById.addAll(newList);

                System.out.println("gnmcById:" + gnmcById);


                role.setXtgnbText(StringUtils.join(gnmcById, ","));


                System.out.println("rolelist:-----------++++++++++" + roles);

                //通过权限id集合 获取所有权限
                List<Xtgnb> xtgnbList = xtgnbRepository.getRoleAuthotity(roles);

                System.out.println("xtgnbList:-----++++" + xtgnbList);

                List<XtgnbVo> xtgnbByxtblist = xtgnbService.getXtgnbByxtblist(xtgnbList);

                System.out.println("xtgnbByxtblist:：：：：：：：：：：：" + xtgnbByxtblist);
                //将权限注入每个角色
                role.setXtgnbList(xtgnbByxtblist);
            }

        }
        return roleList;

    }
}