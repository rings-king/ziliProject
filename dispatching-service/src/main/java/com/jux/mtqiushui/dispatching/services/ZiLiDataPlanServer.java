package com.jux.mtqiushui.dispatching.services;

import com.jux.mtqiushui.dispatching.model.KanbanPlan;
import com.jux.mtqiushui.dispatching.model.PlanDetail;
import com.jux.mtqiushui.dispatching.model.PlanManage;
import com.jux.mtqiushui.dispatching.repository.PlanManageRepository;
import com.jux.mtqiushui.dispatching.repository.ResourceServiceFeign;
import com.jux.mtqiushui.dispatching.repository.ZiLiDataFeign;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Auther: fp
 * @Date: 2018/11/30 15:11
 * @Description:
 */
@Service
public class ZiLiDataPlanServer {

    @Autowired
    private PlanManageRepository planManageRepository;


    @Autowired
    private ZiLiDataFeign ziLiDataFeign;

    @Autowired
    private ResourceServiceFeign resourceServiceFeign;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void addPlan() throws Exception{
        List<KanbanPlan> allPlan = ziLiDataFeign.getAllPlan();
        List<PlanManage> planManages = new ArrayList<>();
        int count = 0;
        int num = 0;
        if (allPlan.size() != 0) {
            for (KanbanPlan kanbanPlan : allPlan) {
                PlanManage byPlanNum = planManageRepository.findByPlanNum(kanbanPlan.getPlanno());
                if (byPlanNum == null) {
                    for (int i = 0; i < planManages.size(); i++) {
                        PlanManage planManage = planManages.get(i);
                        if (planManage.getPlanNum().equals(kanbanPlan.getPlanno())) {
                            count++;
                            num = i;
                        }

                    }
                    if (count == 0) {
                        PlanManage planManage = new PlanManage();
                        // 设置计划单号
                        planManage.setPlanNum(kanbanPlan.getPlanno());
                        // 设置创建时间
                        planManage.setUpdateTime(kanbanPlan.getCreatetime());
                        // 设置预计完成时间
                        planManage.setPlanEndTime(kanbanPlan.getFinishdate());
                        // 物料编码
                        PlanDetail planDetail = new PlanDetail();
                        String liaohao = kanbanPlan.getLiaohao();
                        if (!StringUtils.isEmpty(liaohao)) {
                            planDetail.setMaterialCode(liaohao);
                            planDetail.setDoneQuantity(Integer.parseInt(kanbanPlan.getAmount()));
                        }

                        planDetail.setPlanManage(planManage);
                        planManage.getPlanDetailsTab().add(planDetail);
                        planManages.add(planManage);
                    } else {
                        PlanManage planManage = planManages.get(num);
                        Set<PlanDetail> planDetailsTab = planManage.getPlanDetailsTab();
                        // 物料编码
                        PlanDetail planDetail = new PlanDetail();
                        String liaohao = kanbanPlan.getLiaohao();
                        if (!StringUtils.isEmpty(liaohao)) {
                            planDetail.setMaterialCode(liaohao);
                            // 计划完成数量
                            planDetail.setDoneQuantity(Integer.parseInt(kanbanPlan.getAmount()));
                        }
                        planDetailsTab.add(planDetail);
                        planDetail.setPlanManage(planManage);
                        count = 0;
                        num = 0;
                    }

                }
            }
            planManageRepository.save(planManages);
        }
    }

}
