package com.jux.mtqiushui.dispatching.Quartz;

import com.jux.mtqiushui.dispatching.model.DeviceDefine;
import com.jux.mtqiushui.dispatching.repository.DeviceDefineRepository;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;

public class QuartzDemo implements Job {
    @Autowired
    private DeviceDefineRepository deviceDefineRepository;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("Execute*******"+new Date());

        List<DeviceDefine> mess = deviceDefineRepository.findAll();


        redisTemplate.opsForValue().set("tt",mess);

        Object tt = redisTemplate.opsForValue().get("tt").toString();
        System.out.println("list"+tt);


        System.out.println("all"+mess);

    }
}
