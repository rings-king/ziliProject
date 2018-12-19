package com.jux.mtqiushui.dispatching.Quartz;

import com.jux.mtqiushui.dispatching.services.ZiLiDataPlanServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Auther: fp
 * @Date: 2018/11/30 14:16
 * @Description:
 */
@Component
public class ZiliSchedule {

    @Autowired
    private ZiLiDataPlanServer ziLiDataTypeService;

    @Scheduled(cron = "0/10 * * * * *")
    public void ziliZuRu()  {
        System.out.println("*******"+ziLiDataTypeService);
        try {
            ziLiDataTypeService.addPlan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
