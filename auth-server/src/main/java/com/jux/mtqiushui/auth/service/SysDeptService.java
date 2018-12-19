package com.jux.mtqiushui.auth.service;

import com.jux.mtqiushui.auth.domain.Quarter;
import com.jux.mtqiushui.auth.domain.SysDept;
import com.jux.mtqiushui.auth.domain.Vo.DepartQuartetVo;
import com.jux.mtqiushui.auth.domain.Vo.DepartmentVo;
import com.jux.mtqiushui.auth.domain.Vo.SearchCondition;
import com.jux.mtqiushui.auth.repository.QuarterRepository;
import com.jux.mtqiushui.auth.repository.SysDeptRepository;
import com.jux.mtqiushui.auth.util.SearchFarmat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class SysDeptService {


    @Autowired
    private SysDeptRepository deptRepository;
    @Autowired
    private QuarterRepository quarterRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加部门
     * @param quartetVo
     * @return
     */
    @Transactional
    public SysDept addDept(DepartQuartetVo quartetVo){

        SysDept sysDept = new SysDept();

        List<Quarter> quarters = new ArrayList<>();

        System.out.println("11111111111111111111111111"+quartetVo);

        if (quartetVo.getDepartParentId() == null){
            //没有上级部门
            sysDept.setDepartParentId(null);
        }
        sysDept.setDepartName(quartetVo.getDepartName());
        sysDept.setDepartParentId(quartetVo.getDepartParentId());
        sysDept.setDepartCode(quartetVo.getDepartCode());
        sysDept.setUpdateTime(Calendar.getInstance().getTime());
        SysDept dept = deptRepository.save(sysDept);


        List<Quarter> quarterList = quartetVo.getQuarterDetailsTab();
        for (Quarter quarter1 : quarterList) {
            Quarter quarter = new Quarter();
            quarter.setQuarterName(quarter1.getQuarterName());
            quarter.setDeptId(dept.getId());
            quarters.add(quarter);
        }

        System.out.println("quarterList:-----"+quarters);

        List<Quarter> save = quarterRepository.save(quarters);

        if (dept == null || save == null){
            return null;
        }


        return dept;
    }

    /**
     * 根据部门编码获取部门对象
     * @param deptCode
     * @return
     * @throws Exception
     */
    public SysDept getDeptByCode(String deptCode) throws Exception{
        SysDept byDepartCode = deptRepository.findByDepartCode(deptCode);
        return byDepartCode;
    }

    /**
     * 根据部门名称获取部门对象
     * @param deptname
     * @return
     * @throws Exception
     */
    public SysDept getDeptByName(String deptname) throws Exception{
        SysDept byDepartName = deptRepository.findByDepartName(deptname);
        return byDepartName;
    }

    /**
     * 获取所有部门
     * @param pageable
     * @return
     * @throws Exception
     */
    public Page<SysDept> getAllDept(Pageable pageable) throws Exception{
        Page<SysDept> depts = deptRepository.findAll(pageable);

        System.out.println("111111111111111111"+depts);
        //遍历所有部门 判断该部门是否有上级部门
        for (SysDept dept : depts) {
            Long parentId = dept.getDepartParentId();
            if (parentId == null || parentId.equals("")){
                dept.setDeparParentName(null);
            }else {
                dept.setDeparParentName(getParentDept(dept));
            }

        }

        return depts;
    }

    /**
     * 获取上级部门名称
     * @param sysDept
     * @return
     */
    public String getParentDept(SysDept sysDept){
        Long departParentId = sysDept.getDepartParentId();
        SysDept sysDept1 = new SysDept();
        if (departParentId == null){
            System.out.println("没有上级id");
        }else {
            sysDept1 = deptRepository.findById(departParentId);
        }

       return sysDept1.getDepartName();
    }


    //判断才传入的部门id是否同上级部门id 重复
    public Boolean compareIdandParentid(DepartQuartetVo departQuartetVo,Long id){

        if (departQuartetVo.getDepartParentId() == null){
            return true;
        }

        if (departQuartetVo.getDepartParentId().equals(id)){
            System.out.println("00000000000000");
            return false;
        }
        return true;
    }


    /**
     * 修改部门
     * @param departQuartetVos
     * @return
     * @throws Exception
     */
    @Transactional
    public Boolean modifyDept(List<DepartQuartetVo> departQuartetVos,Long deptid)throws Exception{

        System.out.println("000000000000000"+departQuartetVos);

        DepartQuartetVo addQuartetVo = departQuartetVos.get(0);
        DepartQuartetVo updateQuartetVo = departQuartetVos.get(1);
        DepartQuartetVo removeQuartetVo = departQuartetVos.get(2);


        System.out.println("1111111111111111"+addQuartetVo);
        System.out.println("1111111111111111"+updateQuartetVo);
        System.out.println("1111111111111111"+removeQuartetVo);

        Boolean aBoolean = modiAdd(addQuartetVo, deptid);

        Boolean aBoolean1 = modiUpdate(updateQuartetVo, deptid);

        Boolean moddelete = moddelete(removeQuartetVo);


        if (aBoolean==true && aBoolean1==true && moddelete==true){
            return true;
        }


        return false;
    }


    // 修改部门中的岗位表的addnew
    public Boolean modiAdd(DepartQuartetVo addQuartetVo,Long deptid){
        System.out.println("deptid:"+deptid);
        List<Quarter> quarterDetailsTab = addQuartetVo.getQuarterDetailsTab();

        //判读是否添加的岗位存在
      try {
          List<Quarter> quarterBydept = getQuarterBydeptId(deptid);

          System.out.println("1111111111"+quarterBydept);
          if (quarterBydept.size()==0){
              //不存在直接添加

              System.out.println("1111111111111111111");

              Boolean aBoolean = addQuarterone(quarterDetailsTab, deptid);
              if (aBoolean){
                  return true;
              }

          }
              //存在则判断是否有相同 ，相同就不允许添加
              Boolean aBoolean = addQuartertwo(quarterDetailsTab, deptid);
              System.out.println("22222222222222222222");
              if (aBoolean){
                  return true;
              }

      }catch (Exception e){
                  return false;
      }
       return true;

    }

      // 修改部门update

    public Boolean modiUpdate( DepartQuartetVo updateQuartetVo,Long deptId){
        //通过id获取旧部门信息
        SysDept oldDept = deptRepository.findOne(deptId);

        SysDept newSysdept = new SysDept();
        newSysdept.setId(deptId);
        newSysdept.setUpdateTime(Calendar.getInstance().getTime());

        //判断修改的值

        if (updateQuartetVo.getDepartName() == null || updateQuartetVo.getDepartName().equals("")){
            newSysdept.setDepartName(oldDept.getDepartName());
        }else {
            newSysdept.setDepartName(updateQuartetVo.getDepartName());
        }

        if (updateQuartetVo.getDepartCode() == null||updateQuartetVo.getDepartCode().equals("")){
            newSysdept.setDepartCode(oldDept.getDepartCode());
        }else {
            newSysdept.setDepartCode(updateQuartetVo.getDepartCode());
        }
        if (updateQuartetVo.getDepartParentId() == null){
            newSysdept.setDepartParentId(oldDept.getDepartParentId());
        }else {
            newSysdept.setDepartParentId(updateQuartetVo.getDepartParentId());
        }

        if (updateQuartetVo.getQuarterDetailsTab() == null || updateQuartetVo.getQuarterDetailsTab().size() ==0){
            System.out.println("yyyyyyyyyyy");
        }else {
            List<Quarter> quarterDetailsTab = updateQuartetVo.getQuarterDetailsTab();
            //遍历所有要修改的岗位对象
            for (Quarter quarter : quarterDetailsTab) {
                Quarter newQuarter = new Quarter();
                newQuarter.setId(quarter.getId());
                newQuarter.setDeptId(deptId);
                newQuarter.setQuarterName(quarter.getQuarterName());
                Quarter save = quarterRepository.save(newQuarter);
                if (save == null){
                    return false;
                }
            }

        }


        SysDept save = deptRepository.save(newSysdept);
        if(save== null){
            return false;
        }
        return true;
    }


    // 修改部门中的岗位表的delete

    public Boolean moddelete(DepartQuartetVo updateQuartetVo)throws Exception{

        System.out.println("u"+updateQuartetVo);

        if (updateQuartetVo.getQuarterDetailsTab().size()==0){
            return true;
        }
        List<Quarter> quarterDetailsTab = updateQuartetVo.getQuarterDetailsTab();


        for (Quarter quarter : quarterDetailsTab) {
            Long id = quarter.getId();
          quarterRepository.delete(id);
        }

        return true;
    }



    /**
     * 根据id删除该部门
     * @param id
     */
    public void deleteDepartment(Long id) throws Exception{
        //删除部门的同时删除该部门下所有岗位
        quarterRepository.deleteByDeptId(id);

        deptRepository.delete(id);
    }

    /**
     * 根据id查询该部门详细信息
     * @param id
     * @return
     * @throws Exception
     */
    public SysDept getDeptById(Long id) throws Exception{
//        SysDept sysDept = deptRepository.findById(id);
        SysDept sysDept = deptRepository.findOne(id);
        if (sysDept == null) {
            return null;
        }
        System.out.println("++++++++++++++++"+sysDept);
        //将上级部门名称注入
        sysDept.setDeparParentName(getParentDept(sysDept));
        //
        sysDept.setQuarterDetailsTab(getQuarterBydeptId(id));


        System.out.println("------------------"+sysDept);

        return sysDept;
    }

    /**
     *查询该部门下是否有用户或员工
     * @param id
     * @return
     */
    public boolean getUserBydeptId(Long id){
        boolean restful = false;
        String user = deptRepository.findUserByDeptId(Long.toString(id));
        String emp = deptRepository.findEmpByDeptId(id);

        if (user== null && emp == null){
            restful = true;
        }
        return restful;
    }


    /**
     * 获取所有部门名称：员工档案管理使用
     * @return
     */
    public List<DepartmentVo> getAllDeptName(){
        List<SysDept> all = deptRepository.findAll();
        List<DepartmentVo> departmentVos = new ArrayList<>();

        //取出id和name;
        for (SysDept sysDept : all) {
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepartmentId(sysDept.getId());
            departmentVo.setDepartmentName(sysDept.getDepartName());
            departmentVos.add(departmentVo);
        }

        return departmentVos;
    }

    /**
     * 根据部门id 获取其下所有岗位
     * @param deptId
     * @return
     */
    public List<Quarter> getQuarterBydeptId(Long deptId)throws Exception{
        List<Quarter> byDeptId = quarterRepository.findByDeptId(deptId);
        return byDeptId;
    }


    /**
     * 添加岗位01
     * @param quarterList
     * @param deptId
     * @return
     */
    public Boolean addQuarterone(List<Quarter> quarterList ,Long deptId){

        List<Quarter> quarters = new ArrayList<>();

        for (Quarter quarter : quarterList) {
            Quarter newQuarter = new Quarter();
            newQuarter.setQuarterName(quarter.getQuarterName());
            newQuarter.setDeptId(deptId);
            quarters.add(newQuarter);
        }

        System.out.println("quarters:"+quarters);
        List<Quarter> save = quarterRepository.save(quarters);
        if (save != null){
            //添加岗位成功
            return true;
        }
        return false;
    }


    /**
     * 添加岗位02
     * @param quarterName
     * @param deptid
     * @return
     */
   public Boolean addQuarter(String quarterName,Long deptid){
        Quarter newQuarter = new Quarter();

        newQuarter.setQuarterName(quarterName);
        newQuarter.setDeptId(deptid);

       System.out.println("------11444------"+newQuarter);
        Quarter save = quarterRepository.save(newQuarter);

        if (save!= null){
            return true;
        }
        return false;
    }



    /**
     * 判断新添加的岗位是否已存在
     * @param quarterList
     * @param deptId
     * @return
     */
    public Boolean addQuartertwo(List<Quarter> quarterList ,Long deptId){

        //获取新添加的岗位名称
        List<String> quarterNameList = new ArrayList<>();
        for (Quarter quarter : quarterList) {
            String quarterName = quarter.getQuarterName();
            quarterNameList.add(quarterName);
        }

        List<String> oldQuarterNameList = new ArrayList<>();
        try {
            List<Quarter> quarterBydeptId = getQuarterBydeptId(deptId);
            for (Quarter quarter : quarterBydeptId) {
                String quarterName = quarter.getQuarterName();
                oldQuarterNameList.add(quarterName);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        for (String s : quarterNameList) {

            System.out.println("*1*1*"+s);
            if (!oldQuarterNameList.contains(s)){
                Boolean aBoolean = addQuarter(s, deptId);

                if (aBoolean){
                    return true;
                }
                return false;
            }

        }
       return false;
    }

    /**
     * 多条件查询
     * @param conditionList
     * @return
     * @throws Exception
     */
    public List<SysDept> getManySearch(List<SearchCondition> conditionList)throws Exception{

        List<SysDept> sysDeptList = new ArrayList<>();

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

        if (aNew.indexOf("departCode")!=-1){
           aNew = aNew.replace("departCode","depart_code");
        }
        if (aNew.indexOf("departName")!=-1){
            aNew =  aNew.replace("departName","depart_name");
        }
        if (aNew.indexOf("departParentId")!=-1){
            aNew =  aNew.replace("departParentId","s.depart_parent_id");
        }
        if (aNew.indexOf("quarterName")!=-1){
            aNew =  aNew.replace("quarterName","q.quartername");
        }

        String sql = null;

                sql = "SELECT s.* FROM sys_dept s ,`quarter` q WHERE s.id = q.deptid and" + aNew;

        System.out.println("ertttttttt"+sql);
        List<Long> idList = new ArrayList<>();
        List<SysDept> query = jdbcTemplate.query(sql, new RowMapper<SysDept>() {
            @Override
            public SysDept mapRow(ResultSet resultSet, int i) throws SQLException {
                SysDept sysDept = new SysDept();
                sysDept.setId(resultSet.getLong("id"));
                return sysDept;
            }
        });

        if (query.size() != 0) {
            //取出查询到到的所有用户id
            for (SysDept sysDept : query) {
                Long id = sysDept.getId();
                idList.add(id);
            }
            sysDeptList = deptRepository.findDeptByid(idList);
            for (SysDept sysDept : sysDeptList) {
                sysDept.setDeparParentName(getParentDept(sysDept));
            }
        }
       return sysDeptList;

    }
}
