package com.jux.mtqiushui.resources.quartz;

import com.jux.mtqiushui.resources.services.ZiLiDataTypeService;
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
    private ZiLiDataTypeService ziLiDataTypeService;

    @Scheduled(cron = "0/10 * * * * *")
    public void ziliZuRu()  {
        System.out.println("*******"+ziLiDataTypeService);
        try {
            ziLiDataTypeService.addMaterial();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
