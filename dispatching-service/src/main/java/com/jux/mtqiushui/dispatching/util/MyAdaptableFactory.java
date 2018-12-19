package com.jux.mtqiushui.dispatching.util;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

@Component("myAdaptableFactory")
public class MyAdaptableFactory extends AdaptableJobFactory {

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;


    /**
     * 该方法将实例化的对象手动添加到springIoc 容器中 并完成对象的注入
     * @param bundle
     * @return
     * @throws Exception
     */
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        Object obj = super.createJobInstance(bundle);

        //将obj对象添加到ioc容器中
        this.autowireCapableBeanFactory.autowireBean(obj);
        return obj;
    }
}
