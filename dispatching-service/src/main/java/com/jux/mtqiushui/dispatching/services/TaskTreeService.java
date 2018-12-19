/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jux.mtqiushui.dispatching.services;

import com.jux.mtqiushui.dispatching.model.DistributionStationTab1;
import com.jux.mtqiushui.dispatching.model.ProductionTreeLine;
import com.jux.mtqiushui.dispatching.model.TaskTree;
import com.jux.mtqiushui.dispatching.model.TaskTwo;
import com.jux.mtqiushui.dispatching.repository.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 * @author jux
 */
@Service
public class TaskTreeService {

    @Autowired
    private TaskTreeRepository taskTreeRepository;

    //注入原派工单数据接口
    @Autowired
    private TaskTwoRepository taskTwoRepository;

    //注入物料接口
    @Autowired
    private ResourcesServiceApi resourcesServiceApi;

    @Autowired
    private ProductionLineService productionLineService;

    @Autowired
    private ProcessService processService;

    @Autowired
    private AuthServiceApi authServiceApi;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 暂时修改的查询语句
     *
     * @param startDate 实际开工时间
     * @param endDate   实际完工时间
     * @return
     * @throws Exception
     */
    public List<TaskTree> getTaskTreesByDateRange(Timestamp startDate, Timestamp endDate) throws Exception {
        if (startDate.after(endDate)) {
            throw new Exception("启始时间晚于结束时间，请核对");
        }
        List<TaskTree> list = new LinkedList<>();
        List<TaskTwo> all = taskTwoRepository.findAll();
        //设置起始初始值为1
        int index = 1;
        //遍历原派工单
        if (all.size() != 0) {
            //原派工单
            for (TaskTwo taskTwo : all) {
                //获得计划开始时间
                Date plandStartTime = taskTwo.getPlannedStartTime();
                //获得计划结束时间
                Date plandEndTime = taskTwo.getPlannedEndTime();
                //页面开始时间与原派工计划开始时间进行比较 如果页面时间小于派工开始时间 返回-1
                int i = startDate.compareTo(plandStartTime);
                //页面结束时间与原派工计划结束时间进行比较 如果页面大于派工结束时间  返回 1
                int i1 = endDate.compareTo(plandEndTime);
                if (i == -1 && i1 == 1) {
                    //工图派工单
                    TaskTree taskTree = new TaskTree();

                    //返回物料名称及规则型号
                    if (taskTwo.getMaterialId() != null) {
                        Map<String, String> materialNameAndMaterialModelById = resourcesServiceApi.getMaterialNameAndMaterialModelById(taskTwo.getMaterialId());
                        if (materialNameAndMaterialModelById != null) {
                            taskTree.setMaterialModel(materialNameAndMaterialModelById.get("materialModel"));
                            taskTree.setName(materialNameAndMaterialModelById.get("materialName"));
                        }
                    }
                    taskTree.setId(taskTwo.getId());
                    //计划开始时间
                    if (taskTwo.getPlannedStartTime() != null) {
                        Timestamp timestamp = new Timestamp(taskTwo.getPlannedStartTime().getTime());
                        //  taskTree.setStartDate(timestamp);
                        taskTree.setBaselineStartDate(timestamp);
                    }
                    //计划结束时间
                    if (taskTwo.getPlannedEndTime() != null) {
                        Timestamp timestamp = new Timestamp(taskTwo.getPlannedEndTime().getTime());
                        taskTree.setBaselineEndDate(timestamp);
                    }
                    //实际开工时间
                    if (taskTwo.getActualStartTime() != null) {
                        Timestamp timestamp = new Timestamp(taskTwo.getActualStartTime().getTime());
                        //taskTree.setBaselineStartDate(timestamp);
                        taskTree.setStartDate(timestamp);
                    }
                    //实际结束时间
                    if (taskTwo.getActualEndTime() != null) {
                        Timestamp timestamp = new Timestamp(taskTwo.getActualEndTime().getTime());
                        //   taskTree.setBaselineEndDate(timestamp);
                        taskTree.setEndDate(timestamp);
                    }
                    //计划产量
                    taskTree.setBaseLineQuantity(taskTwo.getPlannedQuantity());
                    //实际产量
                    taskTree.setActualQuantity(taskTwo.getActualCompletionQuantity());

                    taskTree.setLeaf(true);
                    taskTree.setIndex(index);
                    taskTree.setResizable(true);
                    taskTree.setDraggable(true);
                    taskTree.setManuallyScheduled(true);
                    taskTree.setExpanded(true);
                    //花费工时默认为零
                    taskTree.setDuration(0.0);
                    //实际加工数量
                    double actualQuantity;
                    if (StringUtils.isEmpty(taskTwo.getActualCompletionQuantity())) {
                        actualQuantity = 0.0;
                    } else {
                        actualQuantity = Double.parseDouble(taskTwo.getActualCompletionQuantity());
                    }
                    //计划加工数量
                    double plannedQuantity = Double.parseDouble(taskTwo.getPlannedQuantity());
                    BigDecimal b = new BigDecimal(actualQuantity / plannedQuantity);
                    //计算整个派工的数量百分比
                    taskTree.setPercentDone(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    /**
                     * 拼接 产线 工位 工序 操作工
                     */
                    Set<DistributionStationTab1> distributionStationTab1 = taskTwo.getDistributionStationTab1();
                    List<ProductionTreeLine> lines = new ArrayList<>();
//                    List<ProductionTreeLine> linesTwo = new ArrayList<>();
//                    linesTwo.addAll(lines);
                    //遍历派工分配工位 ->赋值 甘特图
                    for (DistributionStationTab1 stationTab1 : distributionStationTab1) {

                        ProductionTreeLine productionTreeLine = new ProductionTreeLine();
                        Long productionLineId = stationTab1.getProductionLineId();
                        Long stationId = stationTab1.getStationId();//productionLineName  //stationName
                        Map<String, String> plne = productionLineService.getProductionLineNameAndStationName(productionLineId, stationId);
                        if (plne != null) {
                            //产线名称
                            productionTreeLine.setProductionLineName(plne.get("productionLineName"));
                            productionTreeLine.setStationName(plne.get("stationName"));
                        }
                        String processNumName = processService.getProcessNumById(stationTab1.getProcessNumId());
                        if (processNumName != null) {
                            productionTreeLine.setProcessName(processNumName);
                        } else {
                            productionTreeLine.setProcessName(null);
                        }
                        String empName = authServiceApi.getEmpNameByid(stationTab1.getOperationId());
                        if (empName != null) {
                            productionTreeLine.setOpenName(empName);
                        } else {
                            productionTreeLine.setOpenName(null);
                        }
                        lines.add(productionTreeLine);
                    }
                    if (lines.size() == 1 || lines.size() == 0) {
                        taskTree.setProductionTreeLines(lines);
                    } else {
                        for (int ij = 0; ij < lines.size() - 1; ij++) {
                            for (int j = lines.size() - 1; j > ij; j--) {
                                //如果该产线名称与以下名称相同 -->删除  追加工序,工位,操作工
                                if (lines.get(ij).getProductionLineName().equals(lines.get(j).getProductionLineName())) {
                                    lines.get(ij).setOpenName(lines.get(ij).getOpenName() + "、" + lines.get(j).getOpenName());
                                    lines.get(ij).setStationName(lines.get(ij).getStationName() + "、" + lines.get(j).getStationName());
                                    lines.get(ij).setProcessName(lines.get(ij).getProcessName() + "、" + lines.get(j).getProcessName());
                                    lines.remove(j);
                                }
                            }
                        }
                        taskTree.setProductionTreeLines(lines);
                    }
                    list.add(taskTree);
                    index++;
                } else {
                    //如果不满足条件 设置跳过当前循环
                    continue;
                }
            }
            List<TaskTree> taskTrees = taskTreeRepository.findByStartDateAndEndDate(startDate, endDate);
            taskTrees.forEach((taskTree) -> {
                fillChildren(taskTree);
            });
        }
        return list;
    }

    private TaskTree fillChildren(TaskTree taskTree) {
        //查询出以此taskTree实体为父的taskTree
        List<TaskTree> children = taskTreeRepository.findByParentId(taskTree.getId());
        if (!children.isEmpty()) {
            children.forEach((child) -> {
                fillChildren(child);
            });

            taskTree.setChildren(children);
        }
        return taskTree;
    }

    public TaskTree getOne(Long id) throws Exception {
        return taskTreeRepository.findOne(id);
    }

    public void saveList(List<TaskTree> taskTreesList) throws Exception {
        taskTreeRepository.save(taskTreesList);
    }

    public void deleteList(List<TaskTree> taskTreesList) throws Exception {
        taskTreeRepository.delete(taskTreesList);
    }

    /**
     * 员工检索
     *
     * @param appointDate 时间
     * @param empUserId   ID
     * @param isFinish    是否完成
     * @return
     */
    public List<TaskTree> findOpenUserOrById(Timestamp appointDate, Long empUserId, String isFinish) throws Exception {
        String isFinishSql=null;
        if (isFinish!=null) {
            if (isFinish.equals("0")) {
                isFinishSql = ("  AND t.is_finish =0 AND t.planned_end_time <= " + "'" + appointDate + "'");
            } else if (isFinish.equals("1")) {
                isFinishSql = ("  AND t.is_finish =1 AND t.actual_end_time <= " + "'" + appointDate + "'");
            }
        }
        else {
            isFinishSql = " AND t.actual_end_time <=" +"'" + appointDate + "'";
        }
        String sql = "SELECT DISTINCT\n" +
                "\tt.id,\n" +
                "\tt.actual_completion_quantity,\n" +
                "\tt.actual_end_time,\n" +
                "\tt.actual_start_time,\n" +
                "\tt.dispatch_list_code,\n" +
                "\tt.is_finish,\n" +
                "\tt.material_id,\n" +
                "\tt.material_name,\n" +
                "\tt.percent,\n" +
                "\tt.planned_start_time,\n" +
                "\tt.planned_end_time,\n" +
                "\tt.planned_quantity,\n"+
                "\tt.process_id,\n" +
                "\tt.process_name,\n" +
                "\tt.update_time,\n" +
                "\tt.work_process_version,\n" +
                "\td.complet,\n" +
                "\td.complet_rate,\n" +
                "\td.operation_id,\n" +
                "\td.planned_quantity,\n" +
                "\td.process_num_id,\n" +
                "\td.production_line_id,\n" +
                "\td.station_id,\n" +
                "\td.tsk_two_id\n" +
                "FROM\n" +
                "\ttasktwo AS t,\n" +
                "\tdistribution_stationtab1 AS d\n" +
                "WHERE\n" +
                (empUserId == null ? " 1=1" : " d.operation_id =" + empUserId) +
                " AND t.id = d.tsk_two_id\n" + isFinishSql;
        List<TaskTwo> taskTwoList = jdbcTemplate.query(sql, new RowMapper<TaskTwo>() {
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
                String ChildrenSql="SELECT\n" +
                        "\tid,\n" +
                        "\tcomplet,\n" +
                        "\tcomplet_rate,\n" +
                        "\toperation_id,\n" +
                        "\tplanned_quantity,\n" +
                        "\tprocess_num_id,\n" +
                        "\tproduction_line_id,\n" +
                        "\tstation_id\n" +
                        "FROM\n" +
                        "\tdistribution_stationtab1\n" +
                        "WHERE\n" +
                        "\ttsk_two_id = "+taskTwo.getId();
                List<DistributionStationTab1> distributionStationTab1s = jdbcTemplate.query(ChildrenSql, new RowMapper<DistributionStationTab1>() {
                    @Override
                    public DistributionStationTab1 mapRow(ResultSet resultSet, int i) throws SQLException {
                        DistributionStationTab1 distributionStationTab1 = new DistributionStationTab1();
                        distributionStationTab1.setId(resultSet.getLong("id"));
                        distributionStationTab1.setComplet(resultSet.getString("complet"));
                        distributionStationTab1.setCompletRate(resultSet.getString("complet_rate"));
                        distributionStationTab1.setPlannedQuantity(resultSet.getString("planned_quantity"));
                        distributionStationTab1.setOperationId(resultSet.getLong("operation_id"));
                        distributionStationTab1.setProcessNumId(resultSet.getLong("process_num_id"));
                        distributionStationTab1.setProductionLineId(resultSet.getLong("production_line_id"));
                        distributionStationTab1.setStationId(resultSet.getLong("station_id"));
                        return distributionStationTab1;
                    }
                });
                Set<DistributionStationTab1> distributionStationTab1Set=new HashSet<>(distributionStationTab1s);
                taskTwo.setDistributionStationTab1(distributionStationTab1Set);
                return taskTwo;
            }
        });
        //去重
        for (int i = 0; i < taskTwoList.size() - 1; i++) {
            if (taskTwoList.get(i).getId().longValue() == taskTwoList.get(i + 1).getId().longValue()) {
                taskTwoList.remove(i);
                i = i - 1;
            }
        }
        Integer index=1;
        List<TaskTree> list = new ArrayList<>();
        if (taskTwoList.size() != 0) {
            for (TaskTwo taskTwo : taskTwoList) {
                TaskTree taskTree=new TaskTree();
                //返回物料名称及规则型号
                if (taskTwo.getMaterialId() != null) {
                    Map<String, String> materialNameAndMaterialModelById = resourcesServiceApi.getMaterialNameAndMaterialModelById(taskTwo.getMaterialId());
                    if (materialNameAndMaterialModelById != null) {
                        //物料规则型号/名称
                        taskTree.setMaterialModel(materialNameAndMaterialModelById.get("materialModel"));
                        taskTree.setName(materialNameAndMaterialModelById.get("materialName"));
                    }
                }
                taskTree.setId(taskTwo.getId());
                //计划开始时间
                if (taskTwo.getPlannedStartTime() != null) {
                    Timestamp timestamp = new Timestamp(taskTwo.getPlannedStartTime().getTime());
                    taskTree.setBaselineStartDate(timestamp);
                }
                //计划结束时间
                if (taskTwo.getPlannedEndTime() != null) {
                    Timestamp timestamp = new Timestamp(taskTwo.getPlannedEndTime().getTime());
                    taskTree.setBaselineEndDate(timestamp);
                }
                //实际开工时间
                if (taskTwo.getActualStartTime() != null) {
                    Timestamp timestamp = new Timestamp(taskTwo.getActualStartTime().getTime());
                    taskTree.setStartDate(timestamp);
                }
                //实际结束时间
                if (taskTwo.getActualEndTime() != null) {
                    Timestamp timestamp = new Timestamp(taskTwo.getActualEndTime().getTime());
                    //   taskTree.setBaselineEndDate(timestamp);
                    taskTree.setEndDate(timestamp);
                }
                //计划产量
                taskTree.setBaseLineQuantity(taskTwo.getPlannedQuantity());
                //实际产量
                taskTree.setActualQuantity(taskTwo.getActualCompletionQuantity());

                taskTree.setLeaf(true);
                taskTree.setIndex(index);
                taskTree.setResizable(true);
                taskTree.setDraggable(true);
                taskTree.setManuallyScheduled(true);
                taskTree.setExpanded(true);
                //花费工时默认为零
                taskTree.setDuration(0.0);
                //实际加工数量
                double actualQuantity;
                if (StringUtils.isEmpty(taskTwo.getActualCompletionQuantity())) {
                    actualQuantity = 0.0;
                } else {
                    actualQuantity = Double.parseDouble(taskTwo.getActualCompletionQuantity());
                }
                //计划加工数量
                double plannedQuantity = Double.parseDouble(taskTwo.getPlannedQuantity());
                BigDecimal b = new BigDecimal(actualQuantity / plannedQuantity);
                //计算整个派工的数量百分比
                taskTree.setPercentDone(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                //产线集合
                List<ProductionTreeLine> productionTreeLines = new ArrayList<>();
                Set<DistributionStationTab1> distributionStationTab1 = taskTwo.getDistributionStationTab1();
                for (DistributionStationTab1 stationTab1 : distributionStationTab1) {
                    ProductionTreeLine productionTreeLine=new ProductionTreeLine();
                    //工序名称
                    String processNumById = processService.getProcessNumById(stationTab1.getProcessNumId());
                    if (processNumById != null) {
                        productionTreeLine.setProcessName(processNumById);
                    } else {
                        productionTreeLine.setProcessName(null);
                    }
                    //产线名称及工位
                    Long productionLineId = stationTab1.getProductionLineId();
                    Long stationId = stationTab1.getStationId();
                    Map<String, String> productionLineNameAndStationName = productionLineService.getProductionLineNameAndStationName(productionLineId, stationId);
                    if (productionLineNameAndStationName != null) {
                        productionTreeLine.setProductionLineName(productionLineNameAndStationName.get("productionLineName"));
                        productionTreeLine.setStationName(productionLineNameAndStationName.get("stationName"));
                    } else {
                        productionTreeLine.setProductionLineName(null);
                        productionTreeLine.setStationName(null);
                    }
                    //操作工
                    Long operationId = stationTab1.getOperationId();
                    String empName = authServiceApi.getEmpNameByid(operationId);
                    if (empName != null) {
                        productionTreeLine.setOpenName(empName);
                    } else {
                        productionTreeLine.setOpenName(null);
                    }
                    //将每一个分配工位添加集合
                    productionTreeLines.add(productionTreeLine);
                }
               taskTree.setProductionTreeLines(productionTreeLines);
                list.add(taskTree);
            }
        }
        return  list;
    }
}
