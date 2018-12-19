package com.jux.mtqiushui.dispatching.services;

import com.jux.mtqiushui.dispatching.model.*;
import com.jux.mtqiushui.dispatching.model.Process;
import com.jux.mtqiushui.dispatching.repository.ProcessNumRepository;
import com.jux.mtqiushui.dispatching.repository.ProcessRepository;
import com.jux.mtqiushui.dispatching.repository.ResourcesServiceApi;
import com.jux.mtqiushui.dispatching.util.ObjectIsNull;
import com.jux.mtqiushui.dispatching.util.SearchFarmat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Max;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ProcessService {

    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private ProcessNumRepository processNumRepository;

    @Autowired
    private ResourcesServiceApi resourcesServiceApi;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //工艺流程 添加 --》附带工序
    @Transactional
    public Process addprocess(Process process) throws Exception {
        Set<ProcessNum> correspondProcessTab1 = process.getCorrespondProcessTab1();
        if (correspondProcessTab1 != null) {
            for (ProcessNum processNum : correspondProcessTab1) {
                processNum.setProcess(process);
            }
        }
        Process save = processRepository.save(process);
        return save;
    }

    //查询工艺流程的所有版本号string前台传的产品名称
    public Boolean getProcessVersions(String version, String string) throws Exception {
        //遍历数据库所有的工艺流程
        List<Process> all = processRepository.findAll();
        //遍历数据库所有工艺
        for (Process process : all) {
            //获得每一个产品id
            Long processProductionId = process.getProcessProductionId();
            //查询该产品名
            Map<String, String> modelById = getMaterialNameAndMaterialModelById(processProductionId);
            //版本号与产品名同时满足条件
            if (process.getProcessVersion().equals(version) && modelById.get("materialName").equals(string)) {
                return false;
            }
        }
        return true;
    }

    //查询所有工艺流程 -->以及附带的工序  支持分页
    public Page<Process> getAllProcess(Pageable pageable) throws Exception {
        //查询所有的工艺流程
        Page<Process> all = processRepository.findAll(pageable);
        List<Process> content = all.getContent();
        for (Process process : content) {
            //每遍历一个就根据id去查询对应的产品名称和规则型号
            Map<String, String> model = resourcesServiceApi.getMaterialNameAndMaterialModelById(process.getProcessProductionId());
            if (model != null) {
                process.setProcessProductionName(model.get("materialName"));
                process.setMaterialModel(model.get("materialModel"));
            }
        }
        return all;
    }

    //根据id删除-->级联->工序也删除
    @Transactional
    public void deleteProcessById(Long id) throws Exception {
        //删除子表的外键
        processNumRepository.deleteProcessNumById(id);
        //删除主表的主键
        processRepository.delete(id);
    }

    //根据id查询工艺流程
    public Process findProcessById(Long id) throws Exception {
        return processRepository.getProcessById(id);

    }

    //根据id查询工艺流程名称以及对应的版本号
    public Map<String, String> getProcessNameAndVersionById(Long id) throws Exception {
        Process processById = processRepository.getProcessById(id);
        if (processById == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        if (processById.getProcessName() != null && processById.getProcessVersion() != null) {
            map.put("processName", processById.getProcessName());
            map.put("workProcessVersion", processById.getProcessVersion());
            return map;
        }
        return map;
    }

    //根据工序id查询对应的工序名称
    public String getProcessNumById(Long id) throws Exception {
        ProcessNum one = processNumRepository.findOne(id);
        if (one == null) {
            return null;
        }
        String processName = null;
        if (one.getProcessName() != null) {
            processName = one.getProcessName();
            return processName;
        }
        return processName;
    }

    //根据id查询物料的产品和规则编号
    public Map<String, String> getMaterialNameAndMaterialModelById(Long materialDefineId) throws Exception {
        Map<String, String> materialNameAndMaterialModelById = resourcesServiceApi.getMaterialNameAndMaterialModelById(materialDefineId);
        return materialNameAndMaterialModelById;
    }

    //根据id修改工艺流程//下属的工序
    @Transactional
    public Boolean modifyProcess(List<Process> processs, Process process) throws Exception {
        //获得添加的工艺及其字表信息
        Process addProcess = processs.get(0);
        Boolean addnewBoolean = addProcessNumAndprocess(addProcess, process);

        //删除工艺以及工序
        Process deleteProcess = processs.get(1);
        Boolean deleteBoolean = delProcessNumAndProcess(deleteProcess, process);

        //修改工艺以及工序
        Process updateProcess = processs.get(2);
        Boolean updateBoolean = updateProcessNumAndProcess(updateProcess, process);

        if (addnewBoolean == true && deleteBoolean == true && updateBoolean == true) {
            return true;
        }
        return false;
    }

    /**
     * 修改工艺以及工序
     *
     * @param process
     * @param processYuan
     * @return
     * @throws Exception
     */
    private Boolean updateProcessNumAndProcess(Process process, Process processYuan) throws Exception {
        boolean addAllFieldNull = ObjectIsNull.isAllFieldNull(process);
        if (addAllFieldNull == false) {
            if (process.getProcessCode() != null) {
                processYuan.setProcessCode(process.getProcessCode());
            }
            if (process.getProcessName() != null) {
                processYuan.setProcessName(process.getProcessName());
            }
            if (process.getProcessProductionId() != null) {
                //根据产品id去查询产品名称以及规则型号
                Map<String, String> map = getMaterialNameAndMaterialModelById(process.getProcessProductionId());
                if (map != null) {
                    //直接保存产品id即可
                    processYuan.setProcessProductionId(process.getProcessProductionId());
                }
            }
            if (process.getProcessVersion() != null) {
                processYuan.setProcessVersion(process.getProcessVersion());
            }
            processYuan.setUpdateTime(process.getUpdateTime());
            Set<ProcessNum> correspondProcessTab1 = process.getCorrespondProcessTab1();
            if (correspondProcessTab1.size() != 0) {
                Set<ProcessNum> correspondProcessTabs = processYuan.getCorrespondProcessTab1();
                for (ProcessNum processNum : correspondProcessTab1) {
                    for (ProcessNum correspondProcessTab : correspondProcessTabs) {
                        if (processNum.getId().longValue() == correspondProcessTab.getId().longValue()) {
                            if (processNum.getProcessName() != null) {
                                correspondProcessTab.setProcessName(processNum.getProcessName());
                            }
                            if (processNum.getProcessType() != null) {
                                correspondProcessTab.setProcessType(processNum.getProcessType());
                            }
                            if (processNum.getLimitTime() != null) {
                                correspondProcessTab.setLimitTime(processNum.getLimitTime());
                            }
                        }
                    }
                }
            }

        }
        System.out.println("更新后:" + processYuan);
        Process save = addprocess(processYuan);
        return save != null ? true : false;
    }

    /**
     * 删除工艺以及工序
     *
     * @param process
     * @param processYuan
     * @return
     * @throws Exception
     */
    private Boolean delProcessNumAndProcess(Process process, Process processYuan) throws Exception {
        boolean addAllFieldNull = ObjectIsNull.isAllFieldNull(process);
        if (addAllFieldNull == false) {
            //获得数据库原有的所有工序
            Set<ProcessNum> correspondProcessTabs = processYuan.getCorrespondProcessTab1();
            if (correspondProcessTabs.size() != 0) {
                //获得要删除的工序集合
                Set<ProcessNum> correspondProcessTab1 = process.getCorrespondProcessTab1();
                //遍历要删除的每一个工序
                for (ProcessNum processNum : correspondProcessTab1) {
                    //迭代数据库当中每一个工序
                    Iterator<ProcessNum> iterator = correspondProcessTabs.iterator();
                    //循环遍历
                    while (iterator.hasNext()) {
                        //每遍历一次 取出
                        ProcessNum next = iterator.next();
                        //判断id是否相等
                        if (processNum.getId().longValue() == next.getId().longValue()) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }
        Process save = processRepository.save(processYuan);
        return save != null ? true : false;
    }

    /**
     * 添加工艺以及工序
     *
     * @param process
     * @param processYuan
     * @return
     * @throws Exception
     */
    private Boolean addProcessNumAndprocess(Process process, Process processYuan) throws Exception {
        boolean addAllFieldNull = ObjectIsNull.isAllFieldNull(process);
        if (addAllFieldNull == false) {
            //遍历要增加的工序
            Set<ProcessNum> correspondProcessTab1 = process.getCorrespondProcessTab1();
            if (correspondProcessTab1.size() != 0) {
                //数据库原有的工序
                Set<ProcessNum> correspondProcessTabs = processYuan.getCorrespondProcessTab1();
                for (ProcessNum processNum : correspondProcessTab1) {
                    processNum.setProcess(processYuan);
                    correspondProcessTabs.add(processNum);
                }
            }
        }
        Process save = processRepository.save(processYuan);
        return save != null ? true : false;
    }

    /**
     * 工艺流程多条件搜索
     *
     * @param conditionList
     * @return
     */
    public List<Process> findProcess(List<SearchCondition> conditionList) throws Exception {
        //存子表
        List<SearchCondition> processNumslist = new ArrayList<>();
        //存规则型号和产品id-->物料接口查询
        List<SearchCondition> materDefilist = new ArrayList<>();
        //去除规则型号
        for (SearchCondition searchCondition : conditionList) {
            if (searchCondition.getFieldname().equals("materialModel")) {
                materDefilist.add(searchCondition);
                conditionList.remove(searchCondition);
                break;
            }
        }
        //迭代  给每个字段取别名
        Iterator<SearchCondition> iterator = conditionList.iterator();
        while (iterator.hasNext()) {
            SearchCondition next = iterator.next();
            //主表
            if (next.getTablename().equals("Mes_WorkProcess_Mes_WorkProcesssStore")) {
                next.setFieldname("p." + next.getFieldname());
                continue;
            }
            //子表
            if (next.getTablename().equals("Mes_WorkProcess_correspondProcessTab1sStore")) {
                next.setFieldname("pm." + next.getFieldname());
                continue;
            }
        }
        //主表SQL
        String maxAndChildrenSQL = "";
        //子表SQL
        String minSQL = "";
        //遍历集合
        for (int i = 0; i < conditionList.size(); i++) {
            //如果有包含子表的查询条件 添加另一个集合
            if (conditionList.get(i).getTablename().equals("Mes_WorkProcess_correspondProcessTab1sStore")) {
                processNumslist.add(conditionList.get(i));
            }
            //如果存在产品id或者规则型号  加入集合当中
            if (conditionList.get(i).getFieldname().equals("processProductionId")) {
                materDefilist.add(conditionList.get(i));
            }
            maxAndChildrenSQL = maxAndChildrenSQL + SearchFarmat.getNew(conditionList.get(i));
            if (i < conditionList.size() - 1) {
                maxAndChildrenSQL = maxAndChildrenSQL + " and ";
            }

        }
        //遍历子表条件集合
        if (processNumslist.size() != 0) {
            for (int i = 0; i < processNumslist.size(); i++) {
                minSQL = minSQL + SearchFarmat.getNew(processNumslist.get(i));
                if (i < processNumslist.size() - 1) {
                    minSQL = minSQL + " and ";
                }
            }
        }
        //转换字段  实体字段<-->数据库字段
        if (maxAndChildrenSQL.contains("processCode")) {
            maxAndChildrenSQL = maxAndChildrenSQL.replace("processCode", "process_code");
        }
        //名称
        if (maxAndChildrenSQL.contains("processName")) {
            maxAndChildrenSQL = maxAndChildrenSQL.replace("processName", "process_name");
        }
        //产品id
        if (maxAndChildrenSQL.contains("processProductionId")) {
            maxAndChildrenSQL = maxAndChildrenSQL.replace("processProductionId", "process_production_id");
        }
        //规则型号
        if (maxAndChildrenSQL.contains("materialModel")) {
            maxAndChildrenSQL = maxAndChildrenSQL.replace("materialModel", "material_model");
        }
        //版本号
        if (maxAndChildrenSQL.contains("processVersion")) {
            maxAndChildrenSQL = maxAndChildrenSQL.replace("processVersion", "process_version");
        }
        if (maxAndChildrenSQL.contains("processType")) {
            maxAndChildrenSQL = maxAndChildrenSQL.replace("processType", "process_type");
        }
        if (maxAndChildrenSQL.contains("limitTime")) {
            maxAndChildrenSQL = maxAndChildrenSQL.replace("limitTime", "limit_time");
        }
        //子表转换
        //工序名称
        if (minSQL.contains("processName")) {
            minSQL = minSQL.replace("processName", "process_name");
        }
        //类型
        if (minSQL.contains("processType")) {
            minSQL = minSQL.replace("processType", "process_type");
        }
        //节拍要求
        if (minSQL.contains("limitTime")) {
            minSQL = minSQL.replace("limitTime", "limit_time");
        }
        String maxSQL = "";
        //物料接口-->规则型号
        String sql = "(";
        if (materDefilist.size() != 0) {
            List<MaterialDefine> productionIdAndModel = resourcesServiceApi.getMaterialIdAndModel(materDefilist);
            for (int i = 0; i < productionIdAndModel.size(); i++) {
                Long id = productionIdAndModel.get(i).getId();
                sql = sql + id;
                if (i < productionIdAndModel.size() - 1) {
                    sql = sql + ",";
                }
            }
        }
        sql = sql + ")";
        System.out.println(sql);
        //转换过的主表SQL
        System.out.println("主表SQL" + maxAndChildrenSQL);
        //转换过的子表SQL
        System.out.println("子表SQL" + minSQL);
        //如果主表条件为空
        if (StringUtils.isEmpty(maxAndChildrenSQL)) {
            if (materDefilist.size()!=0){
            maxSQL = "SELECT DISTINCT\n" +
                    "\tp.id,\n" +
                    "\tp.process_code,\n" +
                    "\tp.process_name,\n" +
                    "\tp.update_time,\n" +
                    "\tp.process_production_id,\n" +
                    "\tp.process_version,\n" +
                    "\tpm.id,\n" +
                    "\tpm.process_name,\n" +
                    "\tpm.process_type,\n" +
                    "\tpm.limit_time\n" +
                    "FROM\n" +
                    "\tprocess AS p,\n" +
                    "\tprocess_num AS pm\n" +
                    "WHERE\n" +
                    "\tp.id = pm.process_id\n"
                    + " and p.process_production_id in " + sql +
                    " ORDER BY\n" +
                    "\tupdate_time DESC";
            }else if (materDefilist.size()==0){
                maxSQL = "SELECT DISTINCT\n" +
                        "\tp.id,\n" +
                        "\tp.process_code,\n" +
                        "\tp.process_name,\n" +
                        "\tp.update_time,\n" +
                        "\tp.process_production_id,\n" +
                        "\tp.process_version,\n" +
                        "\tpm.id,\n" +
                        "\tpm.process_name,\n" +
                        "\tpm.process_type,\n" +
                        "\tpm.limit_time\n" +
                        "FROM\n" +
                        "\tprocess AS p,\n" +
                        "\tprocess_num AS pm\n" +
                        "WHERE\n" +
                        "\tp.id = pm.process_id\n"+
                        " ORDER BY\n" +
                        "\tupdate_time DESC";
            }
        } else {
            if (materDefilist.size() != 0) {
                maxSQL = "SELECT DISTINCT\n" +
                        "\tp.id,\n" +
                        "\tp.process_code,\n" +
                        "\tp.process_name,\n" +
                        "\tp.update_time,\n" +
                        "\tp.process_production_id,\n" +
                        "\tp.process_version,\n" +
                        "\tpm.id,\n" +
                        "\tpm.process_name,\n" +
                        "\tpm.process_type,\n" +
                        "\tpm.limit_time\n" +
                        "FROM\n" +
                        "\tprocess AS p,\n" +
                        "\tprocess_num AS pm\n" +
                        "WHERE\n" +
                        "\tp.id = pm.process_id\n" +
                        "AND" + "\t" + maxAndChildrenSQL +" AND  p.process_production_id in "+sql+
                        " ORDER BY\n" +
                        "\tupdate_time DESC";
            }else {
                maxSQL = "SELECT DISTINCT\n" +
                        "\tp.id,\n" +
                        "\tp.process_code,\n" +
                        "\tp.process_name,\n" +
                        "\tp.update_time,\n" +
                        "\tp.process_production_id,\n" +
                        "\tp.process_version,\n" +
                        "\tpm.id,\n" +
                        "\tpm.process_name,\n" +
                        "\tpm.process_type,\n" +
                        "\tpm.limit_time\n" +
                        "FROM\n" +
                        "\tprocess AS p,\n" +
                        "\tprocess_num AS pm\n" +
                        "WHERE\n" +
                        "\tp.id = pm.process_id\n" +
                        "AND" + "\t" + maxAndChildrenSQL+
                        " ORDER BY\n" +
                        "\tupdate_time DESC";
            }
        }
        System.out.println("拼接主表SQL" + maxSQL);

        //主表查询
        List<Process> query = jdbcTemplate.query(maxSQL, new RowMapper<Process>() {
            public Process mapRow(ResultSet resultSet, int i) throws SQLException {
                Process process = new Process();
                process.setId(resultSet.getLong("id"));
                process.setProcessVersion(resultSet.getString("process_version"));
                process.setProcessCode(resultSet.getString("process_code"));
                process.setProcessName(resultSet.getString("process_name"));
                process.setProcessProductionId(resultSet.getLong("process_production_id"));
                //调用物料接口 再查询时就将产品和规则型号赋值每一个工艺对象
                Map<String, String> md = resourcesServiceApi.getMaterialNameAndMaterialModelById(process.getProcessProductionId());
                if (md != null) {
                    process.setProcessProductionName(md.get("materialName"));
                    process.setMaterialModel(md.get("materialModel"));
                }
                return process;
            }
        });
        //去重
        for (int i = 0; i < query.size() - 1; i++) {
            if (query.get(i).getId().longValue() == query.get(i + 1).getId().longValue()) {
                query.remove(i);
                i = i - 1;
            }
        }
        /**
         * *****************
         */
        for (Process process : query) {
            //组装子表SQL
            //获得ID
            Long id = process.getId();
            String ss = "";
            if (StringUtils.isEmpty(minSQL)) {
                ss = "select pm.id,pm.process_name,pm.process_type,pm.limit_time from process_num as pm where pm.process_id =" + id;
            } else {
                ss = "select pm.id,pm.process_name,pm.process_type,pm.limit_time from process_num as pm where pm.process_id =" + id + " and " + minSQL;
            }
            System.out.println("拼接的子表SQL" + ss);
            List<ProcessNum> query1 = jdbcTemplate.query(ss, new RowMapper<ProcessNum>() {
                public ProcessNum mapRow(ResultSet resultSet, int i) throws SQLException {
                    ProcessNum processNum = new ProcessNum();
                    processNum.setId(resultSet.getLong("id"));
                    processNum.setProcessName(resultSet.getString("process_name"));
                    processNum.setLimitTime(resultSet.getString("limit_time"));
                    processNum.setProcessType(resultSet.getString("process_type"));
                    return processNum;
                }
            });
            //比较子表的数据-->排序
            Set<ProcessNum> set = new TreeSet<>(new Comparator<ProcessNum>() {
                public int compare(ProcessNum processNum, ProcessNum t1) {
                    return processNum.getId().intValue() - t1.getId().intValue();
                }
            });
            set.addAll(query1);
        }
        System.out.println("主表数量" + query.size());
        return query;
    }
}
