package com.jux.mtqiushui.dispatching.configuration;


import com.jux.mtqiushui.dispatching.Quartz.GetModbusData;
import com.jux.mtqiushui.dispatching.util.MyAdaptableFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Quartz 配置类
 */
@Configuration
public class QuartzConfig {

    /**
     * 创建job对象
     */
    @Bean
    public JobDetailFactoryBean jobDetailFactoryBean(){
        JobDetailFactoryBean job = new JobDetailFactoryBean();
        //关联自己的job类
        //factory.setJobClass(QuartzDemo.class);
       job.setJobClass(GetModbusData.class);

        return job;
    }


    /**
     * 创建Trigger对象
     */
    @Bean
    public SimpleTriggerFactoryBean simpleTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean){
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        //关联jobDetail 对象
        trigger.setJobDetail(jobDetailFactoryBean.getObject());
        //该参数表示执行的毫秒数
        trigger.setRepeatInterval(500000);
        //重复次数
        //factory.setRepeatCount(0);

        return trigger;
    }
    /**
     *  创建Scheduler对象
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(SimpleTriggerFactoryBean simpleTriggerFactoryBean, MyAdaptableFactory myAdaptableFactory){
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setJobFactory(myAdaptableFactory);
        scheduler.setStartupDelay(2);
        scheduler.setTriggers(simpleTriggerFactoryBean.getObject());
        return scheduler;
    }


}
