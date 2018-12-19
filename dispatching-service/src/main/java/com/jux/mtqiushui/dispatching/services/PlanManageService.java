package com.jux.mtqiushui.dispatching.services;

import com.jux.mtqiushui.dispatching.model.*;
import com.jux.mtqiushui.dispatching.repository.PlanDetailReposotory;
import com.jux.mtqiushui.dispatching.repository.PlanManageRepository;
import com.jux.mtqiushui.dispatching.repository.ResourcesServiceApi;
import com.jux.mtqiushui.dispatching.repository.TaskTwoRepository;
import com.jux.mtqiushui.dispatching.util.ObjectIsNull;
import com.jux.mtqiushui.dispatching.util.SearchFarmat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PlanManageService {

    @Autowired
    private PlanManageRepository planManageRepository;

    @Autowired
    private PlanDetailReposotory planDetailReposotory;

    @Autowired
    private ResourcesServiceApi resourcesServiceApi;

    @Autowired
    private TaskTwoRepository taskTwoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * 添加计划管理
     * @param planManage
     * @return
     * @throws Exception
     */
    @Transactional
    public PlanManage addPlanManage(PlanManage planManage) throws Exception {
        Set<PlanDetail> planDetailsTab = planManage.getPlanDetailsTab();
        if (planDetailsTab != null) {
            for (PlanDetail planDetail : planDetailsTab) {
                planDetail.setPlanManage(planManage);
            }
        }
        PlanManage save = planManageRepository.save(planManage);
        return save;
    }

    /**
     * 获取所有计划管理
     * @param pageable
     * @return
     * @throws Exception
     */
    public Page<PlanManage> getAllPlanManage(Pageable pageable) throws Exception{
        Page<PlanManage> all = planManageRepository.findAll(pageable);
        return all;
    }

    /**
     * 根据计划派工id返回对应的计划派工信息
     * @param planManageId
     * @return
     * @throws Exception
     */
    public PlanManage getPlanManageById(Long planManageId) throws Exception {
        PlanManage one = planManageRepository.findOne(planManageId);
        if (one != null) {
            // 获取计划管理ID
            Long id = one.getId();
            Set<PlanDetail> planDetailsTab = one.getPlanDetailsTab();
            Integer total = 0;
            for (PlanDetail planDetail : planDetailsTab) {
              //  TaskTwo byPlanNumAndMaterialId = taskTwoRepository.findByPlanNumAndMaterialId(id, planDetail.getId());
                TaskTwo byPlanNumAndMaterialId = taskTwoRepository.findByPlanNumAndMaterialId(id, planDetail.getMaterialId());
                if (byPlanNumAndMaterialId != null) {
                    Set<DistributionStationTab1> distributionStationTab1 = byPlanNumAndMaterialId.getDistributionStationTab1();
                    for (DistributionStationTab1 stationTab1 : distributionStationTab1) {
                        total = total + Integer.valueOf(stationTab1.getPlannedQuantity());
                    }
                    planDetail.setTaskNum(total);
                    total = 0;
                }
            }
        }
        return one;
    }

    /**
     * 根据计划管理的Id删除对应的计划管理信息
     * @param planManageId
     * @throws Exception
     */
    @Transactional
    public void deletePlanManageById(Long planManageId) throws Exception {
        planManageRepository.delete(planManageId);
    }

    /**
     * 根据计划管理的派工单号查询 该计划管理下的所有计划详情
     * @param planManageId
     * @return
     * @throws Exception
     */
    public Set<PlanDetail> getPlanDetailList(Long planManageId) throws Exception {
        PlanManage one = planManageRepository.findOne(planManageId);
        if (one != null) {
            PlanManage byPlanNum = planManageRepository.findByPlanNum(one.getPlanNum());

            if (byPlanNum == null) {
                return null;
            }
            Set<PlanDetail> planDetailsTab = byPlanNum.getPlanDetailsTab();
            for (PlanDetail planDetail : planDetailsTab) {
                String materialCode = planDetail.getMaterialCode();
                if (materialCode != null) {
                    Map<String, String> maps = resourcesServiceApi.getMaterialByMaterialCode(materialCode);
                    if (maps != null) {
                        planDetail.setMaterialName(maps.get("materialName"));
                        planDetail.setMaterialModel(maps.get("materialModel"));
                        planDetail.setUnitName(maps.get("unitName"));
                    }
                }
            }
            return planDetailsTab;
        }

        return null;
    }

    /**
     * 多条件查询
     * @param conditionList
     * @return
     * @throws Exception
     */
    public List<PlanManage> searchPlanManage(List<SearchCondition> conditionList) throws Exception {
        if (conditionList.size() == 0){
            return null;
        }
        Iterator<SearchCondition> iterator = conditionList.iterator();
        while (iterator.hasNext()) {
            SearchCondition next = iterator.next();
            if (next.getTablename().equals("Mes_PlanManagement_Mes_PlanManagementsStore")) {
                next.setFieldname("m." + next.getFieldname());
                continue;
            }
            if (next.getTablename().equals("Mes_PlanManagement_planDetailsTabsStore")) {
                if (next.getFieldname().equals("donePercent")) {
                    iterator.remove();
                    continue;
                }
                next.setFieldname("d." + next.getFieldname());
                continue;
            }
        }

        String aNew = " ";
        for (int i = 0; i < conditionList.size(); i++) {
            aNew += SearchFarmat.getNew(conditionList.get(i));
            if (i<conditionList.size() - 1){
                aNew += " and " ;
            }
        }

        if (aNew.contains("planYear")) {
            aNew = aNew.replace("planYear","plan_year");
        }
        if (aNew.contains("planMonth")) {
            aNew = aNew.replace("planMonth","plan_month");
        }
        if (aNew.contains("planStartTime")) {
            aNew = aNew.replace("planStartTime","plan_start_time");
        }
        if (aNew.contains("planEndTime")) {
            aNew = aNew.replace("planEndTime","plan_end_time");
        }
        if (aNew.contains("totalDonePercent")) {
            aNew = aNew.replace("totalDonePercent","total_done_percent");
        }
        if (aNew.contains("materialName")) {
            aNew = aNew.replace("materialName","material_name");
        }
        if (aNew.contains("materialModel")) {
            aNew = aNew.replace("materialModel","material_model");
        }
        if (aNew.contains("unitName")) {
            aNew = aNew.replace("unitName","unit_name");
        }
        if (aNew.contains("planStartTime")) {
            aNew = aNew.replace("planStartTime","plan_start_time");
        }
        if (aNew.contains("planEndTime")) {
            aNew = aNew.replace("planEndTime","plan_end_time");
        }
        if (aNew.contains("startTime")) {
            aNew = aNew.replace("startTime","start_time");
        }
        if (aNew.contains("endTime")) {
            aNew = aNew.replace("endTime","end_time");
        }
        if (aNew.contains("doneQuantity")) {
            aNew = aNew.replace("doneQuantity","done_quantity");
        }
        if (aNew.contains("totalDoneQuantity")) {
            aNew = aNew.replace("totalDoneQuantity","total_done_quantity");
        }
        if (aNew.contains("planNum")) {
            aNew = aNew.replace("planNum", "plan_num");
        }
        System.out.println("aNew:"+aNew);
        String sql = "select m.id,m.end_time,m.plan_end_time,m.plan_month,m.plan_start_time,m.plan_year,m.start_time,m.total_done_percent from plan_manage as m,plan_detail as d where m.id = d.plan_manage_id and " + aNew + " group by m.id order by m.update_time";
        System.out.println("sql:"+sql);
        List<PlanManage> query = jdbcTemplate.query(sql, new RowMapper<PlanManage>() {
            @Override
            public PlanManage mapRow(ResultSet resultSet, int i) throws SQLException {
                PlanManage planManage = new PlanManage();
                planManage.setId(resultSet.getLong("id"));
                planManage.setEndTime(resultSet.getDate("end_time"));
                planManage.setPlanMonth(resultSet.getString("plan_month"));
                planManage.setPlanStartTime(resultSet.getDate("plan_start_time"));
                planManage.setPlanEndTime(resultSet.getDate("plan_end_time"));
                planManage.setPlanYear(resultSet.getString("plan_year"));
                planManage.setStartTime(resultSet.getDate("start_time"));
                planManage.setTotalDonePercent(resultSet.getDouble("total_done_percent"));
                planManage.setPlanNum(resultSet.getString("plan_num"));
                return planManage;
            }
        });
        System.out.println("query:"+query);
        return query;
    }

    /**
     *
     * @param planManageList
     * @param planManageId
     * @return
     * @throws Exception
     */
    @Transactional
    public Boolean modifyPlanManage(List<PlanManage> planManageList, Long planManageId) throws Exception{

        // 获得添加的计划管理及子表信息
        PlanManage addPlanManage = planManageList.get(0);
        Boolean addnewBoolean = addNewPlanManage(addPlanManage, planManageId);

        // 获得删除的计划管理及子表信息
        PlanManage deletePlanManage = planManageList.get(1);
        Boolean deleteBoolean = deletePlanManage(deletePlanManage, planManageId);

        // 获得修改的计划管理及子表信息
        PlanManage updatePlanManage = planManageList.get(2);
        Boolean updateBoolean = updatePlanManage(updatePlanManage, planManageId);

        if (addnewBoolean == true && deleteBoolean == true && updateBoolean == true) {
            return true;
        }
        return false;
    }

    /**
     * 修改计划管理的addNew
     * @param addPlanManage
     * @param planManageId
     * @return
     * @throws Exception
     */
    private Boolean addNewPlanManage(PlanManage addPlanManage, Long planManageId) throws Exception {
        PlanManage planManage = null;
        try {
            planManage = getPlanManageById(planManageId);
            System.out.println("添加前:"+planManage);
            // 如果查询到为null 则不进行处理
            if (planManage == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        boolean addAllFieldNull = ObjectIsNull.isAllFieldNull(addPlanManage);

        if (addAllFieldNull == false) {
            planManage.setUpdateTime(addPlanManage.getUpdateTime());
            // 获取数据库取出的计划管理对象的计划详细信息集合
            Set<PlanDetail> planDetailsTab = planManage.getPlanDetailsTab();
            // 获取json字符串取出的计划管理对象的计划详细信息集合
            Set<PlanDetail> addPlanDetailsTab = addPlanManage.getPlanDetailsTab();
            if (addPlanDetailsTab.size() != 0) {
                for (PlanDetail planDetail : addPlanDetailsTab) {
                    Long materialId = planDetail.getMaterialId();
                    if (materialId != null) {
                        Map<String, String> map = resourcesServiceApi.getMaterialNameAndMaterialModelById(materialId);
                        if (map == null) {
                            return false;
                        }
                    }
                    Long unitId = planDetail.getUnitId();
                    if (unitId != null) {
                        String unitName = resourcesServiceApi.getMeasurementUnitNameById(unitId);
                        if (unitName == null) {
                            return false;
                        }
                    }
                    planDetail.setPlanManage(planManage);
                    planDetailsTab.add(planDetail);
                }
            }
        }

        PlanManage save = planManageRepository.save(planManage);
        return save != null ? true : false;
    }

    /**
     * 修改计划管理的remove
     * @param deletePlanManage
     * @param planManageId
     * @return
     * @throws Exception
     */
    private Boolean deletePlanManage(PlanManage deletePlanManage, Long planManageId) throws Exception {
        PlanManage planManage = null;
        try {
            planManage = getPlanManageById(planManageId);
            System.out.println("删除前:"+planManage);
            // 如果查询到为null 则不进行处理
            if (planManage == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        boolean deleteAllFieldNull = false;
        try {
            deleteAllFieldNull = ObjectIsNull.isAllFieldNull(deletePlanManage);
        } catch (Exception e) {
            return false;
        }
        if (deleteAllFieldNull == false) {
            planManage.setUpdateTime(deletePlanManage.getUpdateTime());
            Set<PlanDetail> deletePlanDetailsTab = deletePlanManage.getPlanDetailsTab();
            if (deletePlanDetailsTab.size() != 0) {
                Set<PlanDetail> planDetailsTab = planManage.getPlanDetailsTab();
                for (PlanDetail planDetail : deletePlanDetailsTab) {
                    try {
                        Iterator<PlanDetail> iterator = planDetailsTab.iterator();
                        while (iterator.hasNext()) {
                            PlanDetail next = iterator.next();
                            if (planDetail.getId().longValue() == next.getId().longValue()) {
                                iterator.remove();
                                break;
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("删除计划详细信息失败");
                        return false;
                    }

                }
            }
        }

        PlanManage save = planManageRepository.save(planManage);
        return save != null ? true : false;
    }

    /**
     * 修改计划管理的update
     * @param updatePlanManage
     * @param planManageId
     * @return
     * @throws Exception
     */
    private Boolean updatePlanManage(PlanManage updatePlanManage, Long planManageId) throws Exception {
        PlanManage planManage = null;
        try {
            planManage = getPlanManageById(planManageId);
            System.out.println("更新前:"+planManage);
            // 如果查询到为null 则不进行处理
            if (planManage == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        Set<PlanDetail> updatePlanDetailsTab = updatePlanManage.getPlanDetailsTab();

        boolean updateAllFieldNull = false;
        try {
            updateAllFieldNull = ObjectIsNull.isAllFieldNull(updatePlanManage);
        } catch (Exception e) {
            return false;
        }

        if (updateAllFieldNull == false) {
            if (updatePlanManage.getPlanYear() != null) {
                planManage.setPlanYear(updatePlanManage.getPlanYear());
            }
            if (updatePlanManage.getPlanMonth() != null) {
                planManage.setPlanMonth(updatePlanManage.getPlanMonth());
            }
            if (updatePlanManage.getStartTime() != null) {
                planManage.setStartTime(updatePlanManage.getStartTime());
            }
            if (updatePlanManage.getEndTime() != null) {
                planManage.setEndTime(updatePlanManage.getEndTime());
            }
            if (updatePlanManage.getPlanStartTime() != null) {
                planManage.setPlanStartTime(updatePlanManage.getPlanStartTime());
            }
            if (updatePlanManage.getPlanEndTime() != null) {
                planManage.setPlanEndTime(updatePlanManage.getPlanEndTime());
            }
            if (updatePlanManage.getTotalDonePercent() != null) {
                planManage.setTotalDonePercent(updatePlanManage.getTotalDonePercent());
            }
            if (updatePlanManage.getUpdateTime() != null) {
                planManage.setUpdateTime(updatePlanManage.getUpdateTime());
            }
            if (updatePlanManage.getPlanNum() != null) {
                planManage.setPlanNum(updatePlanManage.getPlanNum());
            }
            if (updatePlanDetailsTab.size() != 0) {
                Set<PlanDetail> planDetailsTab = planManage.getPlanDetailsTab();
                for (PlanDetail planDetail : updatePlanDetailsTab) {
                    for (PlanDetail detail : planDetailsTab) {
                        if (planDetail.getId().longValue() == detail.getId().longValue()) {
                            if (planDetail.getMaterialId() != null) {
                                Map<String, String> map = resourcesServiceApi.getMaterialNameAndMaterialModelById(planDetail.getMaterialId());
                                if (map == null) {
                                    return false;
                                }
                            }
                            if (planDetail.getUnitId() != null) {
                                String unitName = resourcesServiceApi.getMeasurementUnitNameById(planDetail.getUnitId());
                                if (unitName == null) {
                                    return false;
                                }
                            }
                            if (planDetail.getStartTime() != null) {
                                detail.setStartTime(planDetail.getStartTime());
                            }
                            if (planDetail.getEndTime() != null) {
                                detail.setEndTime(planDetail.getEndTime());
                            }
                            if (planDetail.getPlanStartTime() != null) {
                                detail.setPlanStartTime(planDetail.getPlanStartTime());
                            }
                            if (planDetail.getPlanEndTime() != null) {
                                detail.setPlanEndTime(planDetail.getPlanEndTime());
                            }
                            if (planDetail.getUnitName() != null) {
                                detail.setUnitName(planDetail.getUnitName());
                            }
                            if (planDetail.getDoneQuantity() != null) {
                                detail.setDoneQuantity(planDetail.getDoneQuantity());
                            }
                            if (planDetail.getMaterialModel() != null) {
                                detail.setMaterialModel(planDetail.getMaterialModel());
                            }
                            if (planDetail.getTotalDoneQuantity() != null) {
                                detail.setTotalDoneQuantity(planDetail.getTotalDoneQuantity());
                            }
                            break;
                        }
                    }
                }

            }
            System.out.println("更新后:"+planManage);
            try {
                PlanManage savePlanManage = addPlanManage(planManage);
                if (savePlanManage != null) {
                    return true;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }

        return false;
    }
}
