package com.jux.mtqiushui.dispatching.services;

import com.jux.mtqiushui.dispatching.common.ServerResponse;
import com.jux.mtqiushui.dispatching.model.*;
import com.jux.mtqiushui.dispatching.model.Process;
import com.jux.mtqiushui.dispatching.model.statistic_analysis.Analysis;
import com.jux.mtqiushui.dispatching.repository.*;
import com.jux.mtqiushui.dispatching.util.ObjectIsNull;
import com.jux.mtqiushui.dispatching.util.SearchFarmat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

@Service
public class TaskTwoService {

    @Autowired
    private TaskTwoRepository taskTwoRepository;

    @Autowired
    private ResourcesServiceApi resourcesServiceApi;

    @Autowired
    private ProcessService processService;

    @Autowired
    private ProductionLineService productionLineService;

    @Autowired
    private AuthServiceApi authServiceApi;
    @Autowired
    private DistributionStationTab1Repository distributionStationTab1Repository;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private DeviceDefineService deviceDefineService;

    @Autowired
    private LocationNumRepository locationNumRepository;

    @Autowired
    private PlanManageRepository planManageRepository;
    //根据派工id查询是否存在
    public TaskTwo findByTaskTwoId(String dispatchListCode) throws Exception {
        TaskTwo taskTwo = taskTwoRepository.findByDispatchListCode(dispatchListCode);
        return taskTwo;
    }

    //保存派工对象
    @Transactional
    public TaskTwo addTaskTwo(TaskTwo taskTwoActual) throws Exception {
        Set<DistributionStationTab1> distributionStationTab1 = taskTwoActual.getDistributionStationTab1();
        for (DistributionStationTab1 stationTab1 : distributionStationTab1) {
            stationTab1.setTaskTwo(taskTwoActual);
        }
        taskTwoActual.setUpdateTime(Calendar.getInstance().getTime());
        TaskTwo taskTwo = taskTwoRepository.save(taskTwoActual);
        return taskTwo;
    }

    /**
     * 修改每个工位的当前进度(修改的)
     *
     * @param
     * @return
     * @throws Exception
     */

    @Transactional()
    public void modifyTaskTwoChildren() {
            List<TaskTwo> all = taskTwoRepository.findAll();
            for (TaskTwo taskTwo : all) {
                Set<DistributionStationTab1> distributionStationTab1 = taskTwo.getDistributionStationTab1();
                if (distributionStationTab1.size() != 0) {
                    //遍历分配工位集合
                    for (DistributionStationTab1 stationTab1 : distributionStationTab1) {
                        if (stationTab1.getStationId() != null) {
                            //  String redisByKey = RedisUtil.getRedisByKey(stationTab1.getStationId());
                            String deviceParamName = null;
                            try {
                                deviceParamName = deviceDefineService.getDeviceParamName(stationTab1.getStationId());
                            } catch (Exception e) {
                                throw new RuntimeException("获得设备名称出错!");
                            }
                            //获取每个工位的当前进度
                            String redisByKey = stringRedisTemplate.opsForValue().get(stationTab1.getStationId() + deviceParamName);
                            //判断如果工位的进度和上次的进度不相等的情况下 进行保存
                            if (redisByKey != null && !redisByKey.equals(stationTab1.getComplet())) {
                                //工序号
                                String nr = null;
                                Station stationById = stationRepository.findOne(stationTab1.getStationId());
                                int i = Integer.parseInt(stationById.getStationNum());
                                if (i < 10) {
                                    nr = "0" + stationById.getStationNum();
                                } else {
                                    nr = stationById.getStationNum();
                                }
                                //获得当前修改的时间
                                Date time = Calendar.getInstance().getTime();
                                String format1 = new SimpleDateFormat("yyMMddHHmmss").format(time);
                                // String format1 = format.format(time);
                                //拼接的编号
                                String stationNumber = nr + redisByKey + format1;
                                //记录每个工位的任务量
                                LocationNum locationNum = new LocationNum();
                                locationNum.setStationId(stationTab1.getStationId());
                                locationNum.setStationNumber(stationNumber);
                                locationNum.setUpdateTime(Calendar.getInstance().getTime());
                                locationNumRepository.save(locationNum);
                                distributionStationTab1Repository.updateDById(redisByKey, stationTab1.getId());
                            }
                        }
                    }
                }
            }
        }

    /**
     * 查询所有
     *
     * @param newPageable
     * @return
     * @throws Exception
     */
    public Page<TaskTwo> findAllTaskTwo(Pageable newPageable) throws Exception {
        Page<TaskTwo> all = taskTwoRepository.findAll(newPageable);
        List<TaskTwo> content = all.getContent();
        for (TaskTwo taskTwo : content) {
            //获得计划完成量
            String plannedQuantity = taskTwo.getPlannedQuantity();
            double v2 = Double.parseDouble(plannedQuantity);
            Integer sum =0;
            //返回产品名称
            Long materialId = taskTwo.getMaterialId();
            Map<String, String> modelById = resourcesServiceApi.getMaterialNameAndMaterialModelById(materialId);
            if (modelById != null) {
                taskTwo.setMaterialName(modelById.get("materialName"));
            } else {
                taskTwo.setMaterialName(null);
            }
            //返回工艺名称及版本号
            Long processId = taskTwo.getProcessId();
            Map<String, String> proNameAndVer = processService.getProcessNameAndVersionById(processId);
            if (proNameAndVer != null) {
                taskTwo.setProcessName(proNameAndVer.get("processName"));
                taskTwo.setWorkProcessVersion(proNameAndVer.get("workProcessVersion"));
            } else {
                taskTwo.setProcessName(null);
                taskTwo.setWorkProcessVersion(null);
            }
            //返回计划单号名称
            PlanManage one1 = planManageRepository.findOne(taskTwo.getPlanNum());
            if (one1!=null){
                taskTwo.setPlanNumCode(one1.getPlanNum());
            }
            //返回分配产线名称以及所属工位
            Set<DistributionStationTab1> distributionStationTab1 = taskTwo.getDistributionStationTab1();
            for (DistributionStationTab1 stationTab1 : distributionStationTab1) {
                //根据工序id查询对应的工序名称
                String processNumById = processService.getProcessNumById(stationTab1.getProcessNumId());
                if (processNumById != null) {
                    stationTab1.setProcessName(processNumById);
                } else {
                    stationTab1.setProcessName(null);
                }
                //获得产线id查询对应产线以及所属工位
                Long productionLineId = stationTab1.getProductionLineId();
                Long stationId = stationTab1.getStationId();
                Map<String, String> productionLineNameAndStationName = productionLineService.getProductionLineNameAndStationName(productionLineId, stationId);
                if (productionLineNameAndStationName != null) {
                    stationTab1.setProductionLineName(productionLineNameAndStationName.get("productionLineName"));
                    stationTab1.setStationName(productionLineNameAndStationName.get("stationName"));
                } else {
                    stationTab1.setProductionLineName(null);
                    stationTab1.setStationName(null);
                }
                //返回操作工
                Long operationId = stationTab1.getOperationId();
                String empNameByid = authServiceApi.getEmpNameByid(operationId);
                if (empNameByid != null) {
                    stationTab1.setOperationUser(empNameByid);
                } else {
                    stationTab1.setOperationUser(null);
                }
                String plannedQuantity1 = stationTab1.getPlannedQuantity();
                String complet =null;
                if (!StringUtils.isEmpty(stationTab1.getComplet())){
                    complet = stationTab1.getComplet();
                    sum=sum+Integer.parseInt(complet);
                }else {
                    complet=null;
                }
                if (complet==null){
                    stationTab1.setCompletRate("0.0%");
                }else {
                    double v = Double.parseDouble(plannedQuantity1);
                    double v1 = Double.parseDouble(complet);
                    DecimalFormat decimalFormat=new DecimalFormat("0.00%");
                    String format = decimalFormat.format(v1 / v);
                    stationTab1.setCompletRate(format);
                }
            }
            //总完成量
            double v = Double.parseDouble(String.valueOf(sum));
            DecimalFormat decimalFormat=new DecimalFormat("0.00%");
            String format = decimalFormat.format(v / v2);
            taskTwo.setPercent(format);
            }
       return all;

    }

    //根据id查询对应的派工对象以及所属的工位
    public TaskTwo findTaskTwoById(Long id) throws Exception {
        TaskTwo one = taskTwoRepository.findOne(id);
        //获得计划完成量
        String plannedQuantity = one.getPlannedQuantity();
        double v2 = Double.parseDouble(plannedQuantity);
        Integer sum =0;
        //返回产品名称
        Long materialId = one.getMaterialId();
        Map<String, String> modelById = resourcesServiceApi.getMaterialNameAndMaterialModelById(materialId);
        if (modelById != null) {
            one.setMaterialName(modelById.get("materialName"));
        }
        //返回工艺名称及规则型号
        Long processId = one.getProcessId();
        Map<String, String> proNameAndVer = processService.getProcessNameAndVersionById(processId);
        if (proNameAndVer != null) {
            one.setProcessName(proNameAndVer.get("processName"));
            one.setWorkProcessVersion(proNameAndVer.get("workProcessVersion"));
        }
        PlanManage one1 = planManageRepository.findOne(one.getPlanNum());
        if (one1!=null){
            one.setPlanNumCode(one1.getPlanNum());
        }
        //返回分配产线名称以及所属工位
        Set<DistributionStationTab1> distributionStationTab1 = one.getDistributionStationTab1();
        for (DistributionStationTab1 stationTab1 : distributionStationTab1) {
            //根据工序id查询对应的工序
            String processNumById = processService.getProcessNumById(stationTab1.getProcessNumId());
            if (processNumById != null) {
                stationTab1.setProcessName(processNumById);
            }
            //根据产线id返回产线名称以及所属工位
            Long productionLineId = stationTab1.getProductionLineId();
            Long stationId = stationTab1.getStationId();
            Map<String, String> lineNameAndStationName = productionLineService.getProductionLineNameAndStationName(productionLineId, stationId);
            if (lineNameAndStationName != null) {
                stationTab1.setProductionLineName(lineNameAndStationName.get("productionLineName"));
                stationTab1.setStationName(lineNameAndStationName.get("stationName"));
            }
            //返回操作工
            Long operationId = stationTab1.getOperationId();
            String empNameByid = authServiceApi.getEmpNameByid(operationId);
            if (empNameByid != null) {
                stationTab1.setOperationUser(empNameByid);
            }
            String plannedQuantity1 = stationTab1.getPlannedQuantity();
            String complet =null;
            if (!StringUtils.isEmpty(stationTab1.getComplet())){
                complet = stationTab1.getComplet();
                sum=sum+Integer.parseInt(complet);
            }else {
                complet=null;
            }
            if (complet==null){
                stationTab1.setCompletRate("0.0%");
            }else {
                double v = Double.parseDouble(plannedQuantity1);
                double v1 = Double.parseDouble(complet);
                DecimalFormat decimalFormat=new DecimalFormat("0.00%");
                String format = decimalFormat.format(v1 / v);
                stationTab1.setCompletRate(format);
            }
        }
        //总完成量
        double v = Double.parseDouble(String.valueOf(sum));
        DecimalFormat decimalFormat=new DecimalFormat("0.00%");
        String format = decimalFormat.format(v / v2);
        one.setPercent(format);
        return one;
    }

    //根据id删除对应的派工
    @Transactional
    public void deleteAllTaskTwo(Long id) throws Exception {
        taskTwoRepository.delete(id);
    }

    //修改派工/所属工位以及下属的子表
    @Transactional
    public Boolean modifyTaskTwo(List<TaskTwo> list, TaskTwo taskTwoById) throws Exception {
        //获得要添加的分配工位
        TaskTwo taskTwo = list.get(0);
        Boolean addtaskTwochildren = addTaskChildren(taskTwo, taskTwoById);
        //获得要删除的分配工位
        TaskTwo taskTwo1 = list.get(1);
        Boolean removeTaskTwochildren = removeTaskChildren(taskTwo1, taskTwoById);
        //获得要修改的分配工位
        TaskTwo taskTwo2 = list.get(2);
        Boolean updateTaskChildren = updateTaskChildren(taskTwo2, taskTwoById);

        if (addtaskTwochildren == true && removeTaskTwochildren == true && updateTaskChildren == true) {
            return true;
        }
        return false;
    }

    /**
     * 要修改的工位
     *
     * @param taskTwo2
     * @param taskTwoById
     * @return
     */
    private Boolean updateTaskChildren(TaskTwo taskTwo2, TaskTwo taskTwoById) throws Exception {
        boolean addAllFieldNull = ObjectIsNull.isAllFieldNull(taskTwo2);
        if (addAllFieldNull == false) {
            if (taskTwo2.getDispatchListCode() != null) {
                taskTwoById.setDispatchListCode(taskTwo2.getDispatchListCode());
            }
            //修改计划派工
            if (taskTwo2.getPlanNum()!=null){
                taskTwoById.setPlanNum(taskTwo2.getPlanNum());
            }
            //判断物料id是否为空
            if (taskTwo2.getMaterialId() != null) {
                Map<String, String> modelById = resourcesServiceApi.getMaterialNameAndMaterialModelById(taskTwo2.getMaterialId());
                if (modelById != null) {
                    taskTwoById.setMaterialId(taskTwo2.getMaterialId());
                }
            }
            //判断派工是否结束
            if (taskTwo2.getIsFinish() != null) {
                taskTwoById.setIsFinish(taskTwo2.getIsFinish());
            }
            //判断工艺流程id是否为空
            if (taskTwo2.getProcessId() != null) {
                Map<String, String> proNameAndVer = processService.getProcessNameAndVersionById(taskTwo2.getProcessId());
                if (proNameAndVer != null) {
                    taskTwoById.setProcessId(taskTwo2.getProcessId());
                }
            }
            if (taskTwo2.getPlannedQuantity() != null) {
                taskTwoById.setPlannedQuantity(taskTwo2.getPlannedQuantity());
            }
            if (taskTwo2.getActualCompletionQuantity() != null) {
                taskTwoById.setActualCompletionQuantity(taskTwo2.getActualCompletionQuantity());
            }
            if (taskTwo2.getPlannedStartTime() != null) {
                taskTwoById.setPlannedStartTime(taskTwo2.getPlannedStartTime());
            }
            if (taskTwo2.getActualStartTime() != null) {
                taskTwoById.setActualStartTime(taskTwo2.getActualStartTime());
            }
            if (taskTwo2.getPlannedEndTime() != null) {
                taskTwoById.setPlannedEndTime(taskTwo2.getPlannedEndTime());
            }
            if (taskTwo2.getActualEndTime() != null) {
                taskTwoById.setActualEndTime(taskTwo2.getActualEndTime());
            }
            if (taskTwo2.getPercent() != null) {
                taskTwoById.setPercent(taskTwo2.getPercent());
            }
            //设置更改最新时间
            taskTwoById.setUpdateTime(taskTwo2.getUpdateTime());
            //获得要修改的工位
            Set<DistributionStationTab1> distributionStationTab1 = taskTwo2.getDistributionStationTab1();
            if (distributionStationTab1.size() != 0) {
                //数据库已经存在的工位
                Set<DistributionStationTab1> distributionStationTab1s = taskTwoById.getDistributionStationTab1();
                //遍历要修改的工位
                for (DistributionStationTab1 stationTab1 : distributionStationTab1) {
                    //遍历数据库原有的工位
                    for (DistributionStationTab1 tab1 : distributionStationTab1s) {
                        //判断要修改的id是否和原有id相同
                        if (stationTab1.getId().longValue() == tab1.getId().longValue()) {
                            //判断工序是否存在
                            if (stationTab1.getProcessNumId() != null) {
                                String processNumById = processService.getProcessNumById(stationTab1.getProcessNumId());
                                if (processNumById != null) {
                                    tab1.setProcessNumId(stationTab1.getProcessNumId());
                                }
                            }
                            //判断分配产线是否存在
                            if (stationTab1.getProductionLineId() != null && stationTab1.getStationId() != null) {
                                Map<String, String> productionLineNameAndStationName = productionLineService.getProductionLineNameAndStationName(stationTab1.getProductionLineId(), stationTab1.getStationId());
                                if (productionLineNameAndStationName != null) {
                                    tab1.setProductionLineId(stationTab1.getProductionLineId());
                                    tab1.setStationId(stationTab1.getStationId());
                                }
                            }
                            //操作工
                            if (stationTab1.getOperationId() != null) {
                                String empNameByid = authServiceApi.getEmpNameByid(stationTab1.getOperationId());
                                if (empNameByid != null) {
                                    tab1.setOperationId(stationTab1.getOperationId());
                                }
                            }
                            //每个工位计划加工数量
                            if (stationTab1.getPlannedQuantity() != null) {
                                tab1.setPlannedQuantity(stationTab1.getPlannedQuantity());
                            }
                            //每个工位的完成进度
                            if (stationTab1.getCompletRate() != null) {
                                tab1.setCompletRate(stationTab1.getCompletRate());
                            }
                        }
                    }
                }
            }
        }
        TaskTwo save = taskTwoRepository.save(taskTwoById);
        return save != null ? true : false;
    }

    /**
     * 要删除的工位
     *
     * @param taskTwo1
     * @param taskTwoById
     * @return
     */
    private Boolean removeTaskChildren(TaskTwo taskTwo1, TaskTwo taskTwoById) throws Exception {
        boolean addAllFieldNull = ObjectIsNull.isAllFieldNull(taskTwo1);
        if (addAllFieldNull == false) {
            //获得要删除的工位是否为空
            Set<DistributionStationTab1> distributionStationTab1 = taskTwo1.getDistributionStationTab1();
            if (distributionStationTab1.size() != 0) {
                Set<DistributionStationTab1> distributionStationTab1s = taskTwoById.getDistributionStationTab1();
                for (DistributionStationTab1 stationTab1 : distributionStationTab1) {
                    Iterator<DistributionStationTab1> iterator = distributionStationTab1s.iterator();
                    //遍历数据每个工位
                    while (iterator.hasNext()) {
                        //数据库已经存在的每个工位
                        DistributionStationTab1 next = iterator.next();
                        if (next.getId().longValue() == stationTab1.getId().longValue()) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }
        TaskTwo save = taskTwoRepository.save(taskTwoById);
        return save != null ? true : false;
    }

    /**
     * (修改功能)添加工位
     *
     * @param taskTwo
     * @param taskTwoById
     * @return
     */
    private Boolean addTaskChildren(TaskTwo taskTwo, TaskTwo taskTwoById) throws Exception {
        boolean addAllFieldNull = ObjectIsNull.isAllFieldNull(taskTwo);
        if (addAllFieldNull == false) {
            //遍历要添加的工位
            Set<DistributionStationTab1> distributionStationTab1 = taskTwo.getDistributionStationTab1();
            if (distributionStationTab1.size() != 0) {
                Set<DistributionStationTab1> distributionStationTab1s = taskTwoById.getDistributionStationTab1();
                for (DistributionStationTab1 stationTab1 : distributionStationTab1) {
                    stationTab1.setTaskTwo(taskTwoById);
                    distributionStationTab1s.add(stationTab1);
                }
            }
        }
        TaskTwo save = taskTwoRepository.save(taskTwoById);
        return save != null ? true : false;
    }

    public TaskTwo findTaskTwoTById(long l) throws Exception {
        TaskTwo one = taskTwoRepository.findOne(l);
        return one;
    }

    /**
     * 多条件搜索
     *
     * @param conditionList
     * @return
     */
    public List<TaskTwo> findTaskTwo(List<SearchCondition> conditionList) throws Exception {
        //物料集合
        List<SearchCondition> materialList = new ArrayList<>();
        //工艺集合
        List<SearchCondition> processList = new ArrayList<>();
        //产线集合
        List<SearchCondition> processLineList = new ArrayList<>();
        //操作工集合
        List<SearchCondition> operationList = new ArrayList<>();
        //子表集合
        List<SearchCondition> chirdenList = new ArrayList<>();
        //迭代每一个子表条件 并拆分
        Iterator<SearchCondition> conditionIterator = conditionList.iterator();
        while (conditionIterator.hasNext()) {
            SearchCondition next = conditionIterator.next();
            //主表
            if (next.getTablename().equals("Mes_DispatchListManagement_Mes_DispatchListManagementsStore")) {
                //如果存在物料条件  添加物料集合
                if (next.getFieldname().equals("materialId")) {
                    materialList.add(next);
                    conditionIterator.remove();
                    continue;
                }
                //如果存在工艺流程条件 ||版本条件 添加工艺集合
                if (next.getFieldname().equals("processId")) {
                    processList.add(next);
                    conditionIterator.remove();
                    continue;
                }
                if (next.getFieldname().equals("workProcessVersion")) {
                    processList.add(next);
                    conditionIterator.remove();
                    continue;
                }
                next.setFieldname("t." + next.getFieldname());
                continue;
            }
            //子表
            if (next.getTablename().equals("Mes_DispatchListManagement_distributionStationTab1sStore")) {
                //如果子表存在工序条件就删除 并添加到工艺集合中
                if (next.getFieldname().equals("processNumId")) {
                    //将子表工序添加到集合中
                    processList.add(next);
                    conditionIterator.remove();
                    continue;
                }
                //如果子表存在产线条件就删除,并添加到产线集合中
                if (next.getFieldname().equals("productionLineId") || next.getFieldname().equals("stationId")) {
                    //将产线||工位添加到集合中
                    processLineList.add(next);
                    conditionIterator.remove();
                    continue;
                }
                //如果子表存在操作工条件就删除,并添加到操作工集合中
                if (next.getFieldname().equals("operationId")) {
                    //将操作工添加到集合中
                    operationList.add(next);
                    conditionIterator.remove();
                    continue;
                }
                //其它条件都添加子表集合中
                chirdenList.add(next);
                next.setFieldname("d." + next.getFieldname());
                continue;
            }
        }
        //主表
        String maxSQL = "";
        //子表
        String minSQL = "";
        //遍历主表条件集合
        if (conditionList.size() != 0) {
            for (int i = 0; i < conditionList.size(); i++) {
                maxSQL = maxSQL + SearchFarmat.getNew(conditionList.get(i));
                if (i < conditionList.size() - 1) {
                    maxSQL = maxSQL + " and ";
                }
            }
        }
        System.out.println("接收的主表条件" + maxSQL);
        //遍历子表条件集合
        if (chirdenList.size() != 0) {
            for (int i = 0; i < chirdenList.size(); i++) {
                minSQL = minSQL + SearchFarmat.getNew(chirdenList.get(i));
                if (i < chirdenList.size() - 1) {
                    minSQL = minSQL + " and ";
                }
            }
        }
        System.out.println("接收的子表条件" + minSQL);
        //转换字段
        if (maxSQL.contains("dispatchListCode")) {
            maxSQL = maxSQL.replace("dispatchListCode", "dispatch_list_code");
        }
        if (maxSQL.contains("planNum")){
            maxSQL=maxSQL.replace("planNum","plan_num_id");
        }
        if (maxSQL.contains("processId")) {
            maxSQL = maxSQL.replace("processId", "process_id");
        }
        if (maxSQL.contains("workProcessVersion")) {
            maxSQL = maxSQL.replace("workProcessVersion", "work_process_version");
        }
        if (maxSQL.contains("materialId")) {
            maxSQL = maxSQL.replace("materialId", "material_id");
        }
        if (maxSQL.contains("plannedQuantity")) {
            maxSQL = maxSQL.replace("plannedQuantity", "planned_quantity");
        }
        if (maxSQL.contains("actualCompletionQuantity")) {
            maxSQL = maxSQL.replace("actualCompletionQuantity", "actual_completion_quantity");
        }
        if (maxSQL.contains("plannedStartTime")) {
            maxSQL = maxSQL.replace("plannedStartTime", "planned_start_time");
        }
        if (maxSQL.contains("plannedEndTime")) {
            maxSQL = maxSQL.replace("plannedEndTime", "planned_end_time");
        }
        if (maxSQL.contains("actualStartTime")) {
            maxSQL = maxSQL.replace("actualStartTime", "actual_start_time");
        }
        if (maxSQL.contains("actualEndTime")) {
            maxSQL = maxSQL.replace("actualEndTime", "actual_end_time");
        }
        if (maxSQL.contains("percent")) {
            maxSQL = maxSQL.replace("percent", "percent");
        }
        if (maxSQL.contains("isFinish")) {
            maxSQL = maxSQL.replace("isFinish", "is_finish");
        }
        if (minSQL.contains("processNumId")) {
            minSQL = minSQL.replace("processNumId", "process_num_id");
        }
        if (minSQL.contains("productionLineId")) {
            minSQL = minSQL.replace("productionLineId", "production_line_id");
        }
        if (minSQL.contains("stationId")) {
            minSQL = minSQL.replace("stationId", "station_id");
        }
        if (minSQL.contains("operationId")) {
            minSQL = minSQL.replace("operationId", "operation_id");
        }
        if (minSQL.contains("complet")) {
            minSQL = minSQL.replace("complet", "complet");
        }
        if (minSQL.contains("plannedQuantity")) {
            minSQL = minSQL.replace("plannedQuantity", "planned_quantity");
        }
        if (minSQL.contains("completRate")) {
            minSQL = minSQL.replace("completRate", "complet_Rate");
        }
        //转换过的条件
        System.out.println("主表" + maxSQL);
        System.out.println("子表" + minSQL);

        //符合产品名称的条件SQL
        String materialSQL = "";
        if (materialList.size() != 0) {
            //得到物料符合条件的集合
            List<MaterialDefine> materialIdAndModel = resourcesServiceApi.getMaterialIdAndModel(materialList);
            if (materialIdAndModel.size() != 0) {
                materialSQL = "(";
                for (int i = 0; i < materialIdAndModel.size(); i++) {
                    //得到符合条件的产品id
                    Long id = materialIdAndModel.get(i).getId();
                    materialSQL = materialSQL + id;
                    if (i < materialIdAndModel.size() - 1) {
                        materialSQL = materialSQL + ",";
                    }
                }
                materialSQL = materialSQL + ")";
            }
        }
        System.out.println("符合产品条件的SQL拼接" + materialSQL);

        //符合工艺流程的条件SQL
        String processSQL = "";
        if (processList.size() != 0) {
            List<Process> listSearchList = getListSearchList(processList);
            if (listSearchList.size() != 0) {
                processSQL = "(";
                for (int i = 0; i < listSearchList.size(); i++) {
                    Long id = listSearchList.get(i).getId();
                    processSQL = processSQL + id;
                    if (i < listSearchList.size() - 1) {
                        processSQL = processSQL + ",";
                    }
                }
                processSQL = processSQL + ")";
            }
        }
        System.out.println("符合工艺流程的条件结果" + processSQL);

        //符合产线定义的条件SQL
        String processLineSQL = "";
        if (processLineList.size() != 0) {
            List<ProductionLine> productionList = getProductionList(processLineList);
            if (productionList.size() != 0) {
                processLineSQL = "(";
                for (int i = 0; i < productionList.size(); i++) {
                    Long id = productionList.get(i).getId();
                    processLineSQL = processLineSQL + id;
                    if (i < productionList.size() - 1) {
                        processLineSQL = processLineSQL + ",";
                    }
                }
                processLineSQL = processLineSQL + ")";
            }
        }
        System.out.println(processLineSQL.length() + "*****************");
        System.out.println("符合产线的条件结果" + processLineSQL);

        //符合操作工的条件SQL
        String operatorSQL = "";
        if (operationList.size() != 0) {
            List<SysEmployee> sysEmployees = authServiceApi.searchEmpByCondition(operationList);
            if (sysEmployees.size() != 0) {
                operatorSQL = "(";
                for (int i = 0; i < sysEmployees.size(); i++) {
                    Long id = sysEmployees.get(i).getId();
                    operatorSQL = operatorSQL + id;
                    if (i < sysEmployees.size() - 1) {
                        operatorSQL = operatorSQL + ",";
                    }
                }
                operatorSQL = operatorSQL + ")";
            }
        }
        System.out.println("符合操作工的条件结果" + operatorSQL);
        //拼接主表SQL t->主表  d->子表
        String taskTwoSQL = "SELECT DISTINCT\n" +
                "\tt.id,\n" +
                "\t+ t.dispatch_list_code,\n" +
                "\tt.process_id,\n" +
                "\tt.material_id,\n" +
                "\tt.planned_quantity,\n" +
                "\tt.actual_completion_quantity,\n" +
                "\tt.planned_start_time,\n" +
                "\tt.planned_end_time,\n" +
                "\tt.actual_start_time,\n" +
                "\tt.actual_end_time,\n" +
                "\tt.update_time,\n" +
                "\tt.percent,\n" +
                "\tt.is_finish,\n" +
                "\tt.plan_num_id,\n" +
                "\tt.plan_num_code,\n"+
                "\td.id,\n" +
                "\td.process_num_id,\n" +
                "\td.production_line_id,\n" +
                "\td.station_id,\n" +
                "\td.operation_id,\n" +
                "\td.complet,\n" +
                "\td.planned_quantity,\n" +
                "\td.complet_Rate\n" +
                "FROM\n" +
                "\ttasktwo AS t,\n" +
                "\tdistribution_stationtab1 AS d\n" +
                "WHERE\n" +
                "\tt.id = d.tsk_two_id\n" +
                (maxSQL.length() == 0 ? "" : " and  " + maxSQL) + (materialSQL.length() == 0 ? "" : " AND t.material_id IN " + materialSQL)
                + (processSQL.length() == 0 ? "" : " and t.process_id in " + processSQL)
                + (processLineSQL.length() == 0 ? "" : " and d.production_line_id in " + processLineSQL)
                + (operatorSQL.length() == 0 ? "" : " and d.operation_id in " + operatorSQL)
                + " ORDER BY\n" + "\tt.update_time DESC";
        System.out.println("拼接的SQL" + taskTwoSQL);
        if (taskTwoSQL.equals("SELECT DISTINCT\n" +
                "\tt.id,\n" +
                "\t+ t.dispatch_list_code,\n" +
                "\tt.process_id,\n" +
                "\tt.material_id,\n" +
                "\tt.planned_quantity,\n" +
                "\tt.actual_completion_quantity,\n" +
                "\tt.planned_start_time,\n" +
                "\tt.planned_end_time,\n" +
                "\tt.actual_start_time,\n" +
                "\tt.actual_end_time,\n" +
                "\tt.update_time,\n" +
                "\tt.percent,\n" +
                "\tt.is_finish,\n" +
                "\tt.plan_num_id,\n" +
                "\tt.plan_num_code,\n"+
                "\td.id,\n" +
                "\td.process_num_id,\n" +
                "\td.production_line_id,\n" +
                "\td.station_id,\n" +
                "\td.operation_id,\n" +
                "\td.complet,\n" +
                "\td.planned_quantity,\n" +
                "\td.complet_Rate\n" +
                "FROM\n" +
                "\ttasktwo AS t,\n" +
                "\tdistribution_stationtab1 AS d\n" +
                "WHERE\n" +
                "\tt.id = d.tsk_two_id\n" +
                " ORDER BY\n" +
                "\tt.update_time DESC")) {
            taskTwoSQL = "SELECT DISTINCT\n" +
                    "\tt.id,\n" +
                    "\t+ t.dispatch_list_code,\n" +
                    "\tt.process_id,\n" +
                    "\tt.material_id,\n" +
                    "\tt.planned_quantity,\n" +
                    "\tt.actual_completion_quantity,\n" +
                    "\tt.planned_start_time,\n" +
                    "\tt.planned_end_time,\n" +
                    "\tt.actual_start_time,\n" +
                    "\tt.actual_end_time,\n" +
                    "\tt.update_time,\n" +
                    "\tt.percent,\n" +
                    "\tt.is_finish,\n" +
                    "\tt.plan_num_id,\n" +
                    "\tt.plan_num_code,\n"+
                    "\td.id,\n" +
                    "\td.process_num_id,\n" +
                    "\td.production_line_id,\n" +
                    "\td.station_id,\n" +
                    "\td.operation_id,\n" +
                    "\td.complet,\n" +
                    "\td.planned_quantity,\n" +
                    "\td.complet_Rate\n" +
                    "FROM\n" +
                    "\ttasktwo AS t,\n" +
                    "\tdistribution_stationtab1 AS d\n" +
                    "WHERE\n" +
                    "\tt.id = d.tsk_two_id\n" +
                    " and  t.id= -1";
        }
        System.out.println("拼接的主表SQL语句" + taskTwoSQL);
        List<TaskTwo> query = jdbcTemplate.query(taskTwoSQL, new RowMapper<TaskTwo>() {
            public TaskTwo mapRow(ResultSet resultSet, int i) throws SQLException {
                TaskTwo taskTwo = new TaskTwo();
                taskTwo.setId(resultSet.getLong("id"));
                taskTwo.setDispatchListCode(resultSet.getString("dispatch_list_code"));
                taskTwo.setMaterialId(resultSet.getLong("material_id"));
                taskTwo.setProcessId(resultSet.getLong("process_id"));
                taskTwo.setPlannedQuantity(resultSet.getString("planned_quantity"));
                taskTwo.setActualCompletionQuantity(resultSet.getString("actual_completion_quantity"));
                taskTwo.setPlannedStartTime(resultSet.getDate("planned_start_time"));
                taskTwo.setPlannedEndTime(resultSet.getDate("planned_end_time"));
                taskTwo.setActualStartTime(resultSet.getDate("actual_start_time"));
                taskTwo.setActualEndTime(resultSet.getDate("actual_end_time"));
                taskTwo.setUpdateTime(resultSet.getDate("update_time"));
                taskTwo.setPercent(resultSet.getString("percent"));
                taskTwo.setIsFinish(resultSet.getString("is_finish"));
                taskTwo.setPlanNum(resultSet.getLong("plan_num_id"));
                //根据产品id查询
                Map<String, String> md = resourcesServiceApi.getMaterialNameAndMaterialModelById(taskTwo.getMaterialId());
                if (md != null) {
                    taskTwo.setMaterialName(md.get("materialName"));
                }
                //根据计划id查询单号
                PlanManage one = planManageRepository.findOne(taskTwo.getPlanNum());
                if (one!=null){
                    taskTwo.setPlanNumCode(one.getPlanNum());
                }
                //工艺
                try {
                    Map<String, String> pd = processService.getProcessNameAndVersionById(taskTwo.getProcessId());
                    if (pd != null) {
                        taskTwo.setProcessName(pd.get("processName"));
                        taskTwo.setWorkProcessVersion(pd.get("workProcessVersion"));
                    }
                } catch (Exception e) {
                    throw new RuntimeException("查询工艺流程接口失败");
                }
                return taskTwo;
            }
        });
        //去重
        for (int i = 0; i < query.size() - 1; i++) {
            if (query.get(i).getId().longValue() == query.get(i + 1).getId().longValue()) {
                query.remove(i);
                i = i - 1;
            }
        }
        return query;
    }

    /**
     * 根据工艺条件返回集合
     *
     * @param conditionList
     * @return
     */
    public List<Process> getListSearchList(List<SearchCondition> conditionList) {
        //SQL
        String processSQL = "";
        for (int i = 0; i < conditionList.size(); i++) {
            processSQL = processSQL + SearchFarmat.getNew(conditionList.get(i));
            if (i < conditionList.size() - 1) {
                processSQL = processSQL + " and ";
            }
        }
        if (processSQL.contains("processId")) {
            processSQL = processSQL.replace("processId", "t.id");
        }
        if (processSQL.contains("workProcessVersion")) {
            processSQL = processSQL.replace("workProcessVersion", "t.process_version");
        }
        if (processSQL.contains("processNumId")) {
            processSQL = processSQL.replace("processNumId", "pm.process_name");
        }
        System.out.println("转换过的SQL" + processSQL);
        String sql = "SELECT DISTINCT\n" +
                "\tt.id,\n" +
                "\tt.process_name,\n" +
                "\tt.process_version\n" +
                "FROM\n" +
                "\tprocess AS t,\n" +
                "\tprocess_num AS pm\n" +
                "WHERE\n" +
                "\tt.id = pm.process_id\n" +
                "AND " + processSQL;
        System.out.println("拼接的SQL" + sql);
        List<Process> query = jdbcTemplate.query(sql, new RowMapper<Process>() {
            @Override
            public Process mapRow(ResultSet resultSet, int i) throws SQLException {
                Process process = new Process();
                process.setId(resultSet.getLong("id"));
                process.setProcessName(resultSet.getString("process_name"));
                process.setProcessVersion(resultSet.getString("process_version"));
                return process;
            }
        });
        return query;
    }

    /**
     * 根据产线条件返回集合
     *
     * @param conditionList
     * @return
     */
    public List<ProductionLine> getProductionList(List<SearchCondition> conditionList) {
        String prodctionLineSQL = "";
        for (int i = 0; i < conditionList.size(); i++) {
            prodctionLineSQL = prodctionLineSQL + SearchFarmat.getNew(conditionList.get(i));
            if (i < conditionList.size() - 1) {
                prodctionLineSQL = prodctionLineSQL + " and ";
            }
        }
        System.out.println("接收的SQL");
        if (prodctionLineSQL.contains("productionLineId")) {
            prodctionLineSQL = prodctionLineSQL.replace("productionLineId", "p.production_line_name");
        }
        if (prodctionLineSQL.contains("stationId")) {
            prodctionLineSQL = prodctionLineSQL.replace("stationId", "s.station_name");
        }
        String productionSQL = "select distinct p.id,p.production_line_name,s.station_name,s.id from production_line as p,station as s where p.id=s.production_line_id and  " + prodctionLineSQL;
        System.out.println("拼接SQL" + productionSQL);
        List<ProductionLine> query = jdbcTemplate.query(productionSQL, new RowMapper<ProductionLine>() {
            public ProductionLine mapRow(ResultSet resultSet, int i) throws SQLException {
                ProductionLine productionLine = new ProductionLine();
                productionLine.setId(resultSet.getLong("id"));
                productionLine.setProductionLineName(resultSet.getString("production_line_name"));
                return productionLine;
            }
        });
        for (int i = 0; i < query.size() - 1; i++) {
            if (query.get(i).getId().longValue() == query.get(i + 1).getId().longValue()) {
                query.remove(i);
                i = i - 1;
            }
        }
        return query;
    }

    /**
     * 根据工位查询对应编号及其进度(新增的)
     *
     * @param stationId
     * @return
     */
    public List<LocationNum> getDisTabById(Long stationId, Timestamp startDate, Timestamp stopDate) throws Exception {
        String sql = "SELECT distinct \n" +
                "\tlm.id,lm.station_id,lm.station_number,lm.dupdate_time\n" +
                "FROM\n" +
                "\tlocation_num as lm\n" +
                "WHERE\n" +
                (stationId == null ? " 1=1 " : "lm.station_id= " + stationId) +
                (startDate == null && stopDate == null ? "" : " and  lm.dupdate_time between '" + startDate + "'  and  '" + stopDate + "'")
                + " order  by  lm.dupdate_time desc ";
        System.out.println(sql);
        List<LocationNum> query = jdbcTemplate.query(sql, new RowMapper<LocationNum>() {

            public LocationNum mapRow(ResultSet resultSet, int i) throws SQLException {
                LocationNum locationNum = new LocationNum();
                locationNum.setId(resultSet.getLong("id"));
                locationNum.setStationId(resultSet.getLong("station_id"));
                locationNum.setStationNumber(resultSet.getString("station_number"));
                locationNum.setUpdateTime(resultSet.getDate("dupdate_time"));
                return locationNum;
            }
        });
        return query;
    }
    /**
     * 根据时间间隔统计每个类型的产量对比
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public ServerResponse<List<AnalysisTwo>> getStaticMaterialTypeEvery(String startTime, String endTime) {
        List<TaskTwo> byUpdateTime = new ArrayList<>();
        if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
            byUpdateTime = taskTwoRepository.findByUpdateTime();

        } else {
            byUpdateTime = taskTwoRepository.findByStartDateAndEndDate(startTime, endTime);
        }

        List<AnalysisTwo> materiaBydept = getMaterialTypeEvery(byUpdateTime);
        //返回查询结果
        return ServerResponse.createBySuccess(materiaBydept);

    }
    /**
     * 每个类型派工遍历
     *
     * @param taskTwos
     * @return
     */
    public List<AnalysisTwo> getMaterialTypeEvery(List<TaskTwo> taskTwos) {
        //返回的集合
        List<AnalysisTwo> analyses = new ArrayList<>();
        //每个产品的集合
        for (TaskTwo taskTwo : taskTwos) {
            AnalysisTwo analysisTwo = new AnalysisTwo();
            List<Analysis> analysisList=new ArrayList<>();
            //根据当前的物料id找到对应的类别名称
            Map<String, String> byNum = resourcesServiceApi.getByNum(taskTwo.getMaterialId());
            //类别名称
            analysisTwo.setTypeName(byNum.get("materialTypeName"));
            // 通过产品id获取产品名称
            Map<String, String> model = resourcesServiceApi.getMaterialNameAndMaterialModelById(taskTwo.getMaterialId());

            //通过派工单id获取其完成数量
            List<String> complet = distributionStationTab1Repository.findCompletById(taskTwo.getId());

            //System.out.println("---------"+complet);

            Integer completNum = 0;
            for (String s : complet) {
                if (!StringUtils.isEmpty(s)) {
                    completNum += Integer.valueOf(s);
                }
            }
            Analysis analysis=new Analysis();
            analysis.setName(model.get("materialName"));
            analysis.setValue(completNum);
            analysisList.add(analysis);
            analysisTwo.setList(analysisList);
            //放入集合
            analyses.add(analysisTwo);
        }
        //重新去重——整理集合数据

        for ( int i = 0;  i < analyses.size() - 1;  i ++ ) {
            for ( int j = analyses.size() - 1;  j > i; j -- ) {
                if (analyses.get(i).getTypeName().equals(analyses.get(j).getTypeName())){
                    //获取要叠加的产线集合
                    List<Analysis> list = analyses.get(i).getList();
                    //获得要删除的集合
                    Iterator<Analysis> iterator = analyses.get(j).getList().iterator();
                    for (Analysis analysis : list) {
                        while (iterator.hasNext()) {
                            //获得第一个要删除的产线
                            Analysis next = iterator.next();
                            //如果产线名称相等
                            if (next.getName().equals(analysis.getName())){
                                //做工数量叠加
                                analysis.setValue(analysis.getValue()+next.getValue());
                            }
                        }
                    }
                    analyses.remove(j);
                }
            }
        }
        return  analyses;
    }

    /**
     * 根据时间间隔统计各产品产量
     * @param startTime
     * @param endTime
     * @return
     */
    public ServerResponse<List<Analysis>> getStatistic(String startTime ,String endTime){


        List<TaskTwo> byUpdateTime = new ArrayList<>();
        if (StringUtils.isEmpty(startTime)&&StringUtils.isEmpty(endTime)){
           byUpdateTime = taskTwoRepository.findByUpdateTime();

        }else {
            byUpdateTime = taskTwoRepository.findByStartDateAndEndDate(startTime,endTime);
        }

        List<Analysis> materiaByTask = getMateriaByTask(byUpdateTime);

        //返回查询结果
        return ServerResponse.createBySuccess(materiaByTask);

    }

    /**
     *根据时间间隔统计各员工产量
     * @param startTime
     * @param endTime
     * @return
     */
    public ServerResponse<List<Analysis>> getstatisticBydept(String startTime ,String endTime){
        List<TaskTwo> byUpdateTime = new ArrayList<>();
        if (StringUtils.isEmpty(startTime)&&StringUtils.isEmpty(endTime)){
            byUpdateTime = taskTwoRepository.findByUpdateTime();

        }else {
            byUpdateTime = taskTwoRepository.findByStartDateAndEndDate(startTime,endTime);

        }

        System.out.println("bytime"+byUpdateTime);

        List<Analysis> materiaBydept = getMateriaBydept(byUpdateTime);
        //返回查询结果
        return ServerResponse.createBySuccess(materiaBydept);

    }


    /**
     * 根据派工单获取对应的产品和完成数量
     * @param taskTwos
     * @return
     */
    public List<Analysis> getMateriaBydept(List<TaskTwo> taskTwos){
        List<Analysis> analyses = new ArrayList<>();
        for (TaskTwo taskTwo : taskTwos) {
            Analysis analysis = new Analysis();
            String empNameByid = null;
           if (distributionStationTab1Repository.findOperationBytaskId(taskTwo.getId())!=null){
               empNameByid = authServiceApi.getEmpNameByid(distributionStationTab1Repository.findOperationBytaskId(taskTwo.getId()));
            }

            //通过派工单获取产品生产员工

            //通过派工单id获取其完成数量
            List<String> complet = distributionStationTab1Repository.findCompletById(taskTwo.getId());
            Integer completNum = 0;
            for (String s : complet) {
                if (!StringUtils.isEmpty(s)){
                    completNum += Integer.valueOf(s);
                }

            }

            //设置产品名和数量
            analysis.setName(empNameByid);
            analysis.setValue(completNum);
            //放入集合
            analyses.add(analysis);

        }

        //对集合数据进行处理

        List<Analysis> newAnalysisList = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < analyses.size(); i++) {
            for (int j = 0; j < analyses.size(); j++) {
                if (analyses.get(i).getName().equals(analyses.get(j).getName()) && (i != j)){
                    analyses.get(i).setValue(analyses.get(i).getValue()+analyses.get(j).getValue());
                }
            }
            for (Analysis newAnalysis : newAnalysisList) {
                if (newAnalysis.getName().equals(analyses.get(i).getName())) {
                    count++;
                }
            }
            if (count == 0) {
                newAnalysisList.add(analyses.get(i));
            }
            count = 0;
        }

        return newAnalysisList;

    }



    /**
     * 根据派工单获取对应的产品和完成数量
     * @param taskTwos
     * @return
     */
    public List<Analysis> getMateriaByTask(List<TaskTwo> taskTwos){

       List<Analysis> analyses = new ArrayList<>();
        for (TaskTwo taskTwo : taskTwos) {
            Analysis analysis = new Analysis();


            // 通过产品id获取产品名称
            Map<String, String> model = resourcesServiceApi.getMaterialNameAndMaterialModelById(taskTwo.getMaterialId());

            //通过派工单id获取其完成数量
            List<String> complet = distributionStationTab1Repository.findCompletById(taskTwo.getId());

            System.out.println("---------"+complet);

            Integer completNum = 0;
            for (String s : complet) {
                if (!StringUtils.isEmpty(s)){
                    completNum += Integer.valueOf(s);
                }

            }
            //设置产品名和数量
            if (model!=null){
                analysis.setName(model.get("materialName"));
            }
            analysis.setValue(completNum);
            //放入集合
            analyses.add(analysis);
        }

        //对集合数据进行处理

        List<Analysis> newAnalysisList = new ArrayList<>();

        int count = 0;
        if (analyses.size()!=0){
            for (int i = 0; i < analyses.size(); i++) {
                for (int j = 0; j < analyses.size(); j++) {
                    if (analyses.get(i).getName().equals(analyses.get(j).getName()) && (i != j)){
                        analyses.get(i).setValue(analyses.get(i).getValue()+analyses.get(j).getValue());
                    }
                }
                for (Analysis newAnalysis : newAnalysisList) {
                    if (newAnalysis.getName().equals(analyses.get(i).getName())) {
                        count++;
                    }
                }
                if (count == 0) {
                    newAnalysisList.add(analyses.get(i));
                }
                count = 0;
            }
        }

        return newAnalysisList;
    }

    /**
     * 根据时间间隔统计不同类型的产量对比
     * @param startTime
     * @param endTime
     * @return
     */
    public ServerResponse<List<Analysis>> getStaticMaterialType(String startTime ,String endTime){
        List<TaskTwo> byUpdateTime = new ArrayList<>();
        if (StringUtils.isEmpty(startTime)&&StringUtils.isEmpty(endTime)){
            byUpdateTime = taskTwoRepository.findByUpdateTime();

        }else {
            byUpdateTime = taskTwoRepository.findByStartDateAndEndDate(startTime,endTime);
        }

        List<Analysis> materiaBydept = getMaterialType(byUpdateTime);
        //返回查询结果
        return ServerResponse.createBySuccess(materiaBydept);

    }

    /**
     * 根据时间间隔统计不同类型的产量对比
     * @param taskTwos
     * @return
     */
    public List<Analysis> getMaterialType(List<TaskTwo> taskTwos){

        List<Analysis> analyses = new ArrayList<>();
        for (TaskTwo taskTwo : taskTwos) {
            Analysis analysis = new Analysis();


            // 通过产品id获取产品名称
            Map<String, String> model = resourcesServiceApi.getMaterialNameAndMaterialModelById(taskTwo.getMaterialId());

            //通过派工单id获取其完成数量
            List<String> complet = distributionStationTab1Repository.findCompletById(taskTwo.getId());

            System.out.println("---------"+complet);

            Integer completNum = 0;
            for (String s : complet) {
                if (!StringUtils.isEmpty(s)){
                    completNum += Integer.valueOf(s);
                }

            }
            //设置产品名和数量
            analysis.setName(model.get("materialName"));
            analysis.setValue(completNum);
            //放入集合
            analyses.add(analysis);
        }

        //对集合数据进行处理

        List<Analysis> newAnalysisList = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < analyses.size(); i++) {
            for (int j = 0; j < analyses.size(); j++) {
                if (analyses.get(i).getName().equals(analyses.get(j).getName()) && (i != j)){
                    analyses.get(i).setValue(analyses.get(i).getValue()+analyses.get(j).getValue());
                }
            }
            for (Analysis newAnalysis : newAnalysisList) {
                if (newAnalysis.getName().equals(analyses.get(i).getName())) {
                    count++;
                }
            }
            if (count == 0) {
                newAnalysisList.add(analyses.get(i));
            }
            count = 0;
        }

        List<Analysis> resultList = new ArrayList<>();
        if (newAnalysisList != null) {
            List<Analysis> result = resourcesServiceApi.getMaterialType(newAnalysisList);
            System.out.println("result:"+result);
            if (result != null) {
                for (int i = 0; i < result.size(); i++) {
                    for (int j = 0; j < result.size(); j++) {
                        if (result.get(i).getName().equals(result.get(j).getName()) && (i != j)){
                            result.get(i).setValue(result.get(i).getValue()+result.get(j).getValue());
                        }
                    }


                    for (Analysis newAnalysis : resultList) {
                        if (newAnalysis.getName().equals(result.get(i).getName())) {
                            count++;
                        }
                    }
                    if (count == 0) {
                        resultList.add(result.get(i));
                    }
                    count = 0;
                }
            }
        }
        System.out.println("resultList:"+resultList);
        return resultList;
    }
}