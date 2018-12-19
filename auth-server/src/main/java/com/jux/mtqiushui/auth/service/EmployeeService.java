package com.jux.mtqiushui.auth.service;

import com.jux.mtqiushui.auth.domain.SysEmployee;
import com.jux.mtqiushui.auth.domain.Vo.EmployeeVo;
import com.jux.mtqiushui.auth.domain.Vo.SearchCondition;
import com.jux.mtqiushui.auth.repository.EmployeeRepository;
import com.jux.mtqiushui.auth.repository.SysDeptRepository;
import com.jux.mtqiushui.auth.util.SearchFarmat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private SysDeptRepository deptRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 新增员工
     * @param employee
     * @return
     */
    public SysEmployee addEmpolyee(SysEmployee employee)throws Exception{
        employee.setUpdateTime(Calendar.getInstance().getTime());
        SysEmployee save = employeeRepository.save(employee);
        return save;
    }

    /**
     * 判断新增员工是否重复
     * @param empNumber
     * @return
     */
    public boolean isExitempNumber(String empNumber){
        SysEmployee byEmpNumber = employeeRepository.findByEmpNumber(empNumber);
        if (byEmpNumber == null){
            return false;
        }
        return true;
    }

    /**
     * 删除员工
     * @param id
     * @throws Exception
     */
    @Transactional
    public void deleteEmp(Long id) throws Exception{
        employeeRepository.delete(id);
    }

    /**
     * 修改员工
     * @param sysEmployee
     * @return
     */
    @Transactional
    public SysEmployee modifyEmp(SysEmployee sysEmployee) throws Exception{
        SysEmployee newEmployee = new SysEmployee();

        //获取要修改的员工id
        Long id = sysEmployee.getId();
        //通过id获取旧的对象
        SysEmployee oldemployee = employeeRepository.findById(id);

        newEmployee.setId(oldemployee.getId());
        //判断修改了的数据
        if (sysEmployee.getEmpNumber() == null || sysEmployee.getEmpNumber().equals("")){
            newEmployee.setEmpNumber(oldemployee.getEmpNumber());
        }else {
            newEmployee.setEmpNumber(sysEmployee.getEmpNumber());
        }

        if (sysEmployee.getDepartmentId() == null || sysEmployee.getDepartmentId().equals("")){
            newEmployee.setDepartmentId(oldemployee.getDepartmentId());
        }else {
            newEmployee.setDepartmentId(sysEmployee.getDepartmentId());
        }

        if (sysEmployee.getEmpName() == null || sysEmployee.getEmpName().equals("")){
            newEmployee.setEmpName(oldemployee.getEmpName());
        }else {
            newEmployee.setEmpName(sysEmployee.getEmpName());
        }
        if (sysEmployee.getQuarterId() == null || sysEmployee.getQuarterId().equals("")){
            newEmployee.setQuarterId(oldemployee.getQuarterId());
        }else {
            newEmployee.setQuarterId(sysEmployee.getQuarterId());
        }

        newEmployee.setUpdateTime(Calendar.getInstance().getTime());

        //进行重新存储
        SysEmployee save = employeeRepository.save(newEmployee);
        return save;
    }

    /**
     * 通过id获取员工档案
     * @param id
     * @return
     */
    public EmployeeVo getEmpMeassage(Long id){
        SysEmployee employee = employeeRepository.findOne(id);

        if (employee == null){
            return null;
        }
        EmployeeVo employeeVo = new EmployeeVo();
        employeeVo.setId(employee.getId());
        employeeVo.setEmpName(employee.getEmpName());
        employeeVo.setEmpNumber(employee.getEmpNumber());
        employeeVo.setQuarter(employeeRepository.getQuarterById(employee.getQuarterId()));
        employeeVo.setQuarterId(employee.getQuarterId());
        employeeVo.setDepartment(deptRepository.findDeptNameById(employee.getDepartmentId()));
        employeeVo.setDepartmentId(employee.getDepartmentId());

        return employeeVo;
    }

    /**
     * 获取员工档案
     * @param pageable
     * @return
     */
    public Page<EmployeeVo> getAllEmp(Pageable pageable) throws Exception{
        Page<SysEmployee> pageemployees = employeeRepository.findAll(pageable);
        List<EmployeeVo> employeeVos = new ArrayList<>();


        List<SysEmployee> employees = pageemployees.getContent();
        List<Long> idList = new ArrayList<>();
        for (SysEmployee employee : employees) {
            Long id = employee.getId();
            idList.add(id);
        }
        for (Long aLong : idList) {

            EmployeeVo empMeassage = getEmpMeassage(aLong);
            employeeVos.add(empMeassage);
        }
        Page<EmployeeVo> pages = new PageImpl<>(employeeVos);


        System.out.println(employeeVos+"e************");


        return pages;
    }

    /**
     * 通过员工id获取其姓名
     * @param id
     * @return
     * @throws Exception
     */
    public String getEmpNameById(Long id) throws Exception{
        String quarterById = employeeRepository.getEmPById(id);
        return quarterById;
    }

    /**
     * 查询员工
     * @param conditionList
     * @return
     */
    public List<EmployeeVo> getSimpleEmp(List<SearchCondition> conditionList)throws Exception{

        List<EmployeeVo> employeeList = new ArrayList<>();

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
        if (aNew.indexOf("empNumber")!=-1){
            aNew = aNew.replace("empNumber","emp_number");
        }
        if (aNew.indexOf("empName")!=-1){
            aNew =  aNew.replace("empName","emp_name");
        }
        if (aNew.indexOf("departmentId")!=-1){
            aNew =  aNew.replace("departmentId","d.id");
        }
        if (aNew.indexOf("quarterId")!=-1){
            aNew =  aNew.replace("quarterId","q.id");
        }
        String sql = null;
            sql = "SELECT\n" +
              "\te.id,\n" +
              "\te.department_id,\n" +
              "\te.emp_name,\n" +
              "\te.emp_number,\n" +
              "\te.quarter_id,\n" +
              "\te.update_time\n" +
              "FROM\n" +
              "\temployee e,\n" +
              "\tsys_dept d,\n" +
              "\t`quarter` q\n" +
              "WHERE\n" +
              "\te.department_id = d.id\n" +
              "AND\n" +
              "\te.quarter_id = q.id and"+aNew ;

        System.out.println("sql:"+sql);

        List<Long> idList = new ArrayList<>();

        List<SysEmployee> query = jdbcTemplate.query(sql, new RowMapper<SysEmployee>() {
            @Override
            public SysEmployee mapRow(ResultSet resultSet, int i) throws SQLException {
                SysEmployee employee = new SysEmployee();
                employee.setId(resultSet.getLong("id"));
                return employee;
            }
        });
        if (query.size() != 0) {
            //取出查询到到的所有用户id
            for (SysEmployee employee : query) {
                Long id = employee.getId();
                idList.add(id);
            }
            System.out.println("idList:"+idList);
            List<SysEmployee> employees = employeeRepository.findEmpById(idList);
            List<Long> longList = new ArrayList<>();
            for (SysEmployee employee : employees) {
                Long id = employee.getId();
                longList.add(id);
            }
            for (Long aLong : longList) {
                EmployeeVo empMeassage = getEmpMeassage(aLong);
                employeeList.add(empMeassage);
            }
        }
        return employeeList;
    }


    /**
     * 通过员工的搜索条件查出员工
     * @param conditionList
     * @return
     */
    public List<SysEmployee> searchEmp(List<SearchCondition> conditionList){

        String aNew = " ";
        //将查询条件取出
        for (int i = 0; i < conditionList.size(); i++) {

            aNew += SearchFarmat.getNew(conditionList.get(i));

            if (i < conditionList.size() - 1) {
                aNew += "and ";
            }
        }

        System.out.println("anew:889"+aNew);
        if (aNew.indexOf("operationId")!=-1){
            aNew = aNew.replace("operationId","emp_name");
        }

        String sql = null;
        sql = "SELECT\n" +
          "\te.id,\n" +
          "\te.emp_name\n" +
          "FROM\n" +
          "\temployee e\n" +
          "WHERE\n" +aNew;


        System.out.println("sql:"+sql);

        List<SysEmployee> query = jdbcTemplate.query(sql, new RowMapper<SysEmployee>() {
            @Override
            public SysEmployee mapRow(ResultSet resultSet, int i) throws SQLException {
                SysEmployee employee = new SysEmployee();
                employee.setId(resultSet.getLong("id"));
                employee.setEmpName(resultSet.getString("emp_name"));
                return employee;
            }
        });

        return query;
    }
}
