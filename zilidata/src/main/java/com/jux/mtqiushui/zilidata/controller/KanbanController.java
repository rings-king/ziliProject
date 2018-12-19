package com.jux.mtqiushui.zilidata.controller;

import com.jux.mtqiushui.zilidata.model.KanbanPlan;
import com.jux.mtqiushui.zilidata.model.KanbanType;
import com.jux.mtqiushui.zilidata.service.KanbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: fp
 * @Date: 2018/11/29 15:57
 * @Description:
 */
@RestController
@RequestMapping(value = "data")
public class KanbanController {
    @Autowired
    private KanbanService kanbanService;

    @RequestMapping(value = "/kanBanType",method = RequestMethod.GET)
    public List<KanbanType> getAllType(){
        List<KanbanType> all = kanbanService.getAllKanbanType();

        System.out.println(all);
        return all;
    }


    @RequestMapping(value = "/kanBanPlan",method = RequestMethod.GET)
    public List<KanbanPlan> getAllPlan(){
        List<KanbanPlan> allKanbanPlan = kanbanService.getAllKanbanPlan();
        System.out.println(allKanbanPlan);
        return allKanbanPlan;
    }
}
