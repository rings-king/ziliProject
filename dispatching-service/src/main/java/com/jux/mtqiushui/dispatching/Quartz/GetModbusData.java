package com.jux.mtqiushui.dispatching.Quartz;


import com.jux.mtqiushui.dispatching.controllers.DeviceDefineServiceController;
import com.jux.mtqiushui.dispatching.model.DeviceDefine;
import com.jux.mtqiushui.dispatching.model.DeviceParam;
import com.jux.mtqiushui.dispatching.repository.DeviceDefineRepository;
import com.jux.mtqiushui.dispatching.util.Modbus4jUtils;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GetModbusData implements Job {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private DeviceDefineRepository deviceDefineRepository;

    @Autowired
    private DeviceDefineServiceController controller;

    @Async
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<DeviceDefine> all = controller.getAll();
        if (all == null) {
            controller.setAll(deviceDefineRepository.findAll());
        }

        if (all != null) {
            for (DeviceDefine deviceDefine : all) {

                String device_address = deviceDefine.getDeviceAddress();
                Long station_id = deviceDefine.getStationId();
                String device_ip = deviceDefine.getDeviceIp();
                Set<DeviceParam> deviceParamsTab = deviceDefine.getDeviceParamsTab();
                for (DeviceParam deviceParam : deviceParamsTab) {

                    System.out.println("ttttttt"+deviceParam.getRegisterAddress());
                    int dataType = 0;
                    switch (deviceParam.getDeviceParamsType()){
                        case "1":
                            dataType = DataType.FOUR_BYTE_INT_SIGNED_SWAPPED;
                            break;
                        case "2":
                            dataType = DataType.FOUR_BYTE_FLOAT_SWAPPED;
                            break;
                        case "3" :
                            dataType = DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED;
                            break;
                        default: dataType = DataType.FOUR_BYTE_INT_SIGNED_SWAPPED; break;
                    }
                    try {
                        Number number = Modbus4jUtils.readHoldingRegister(Integer.parseInt(device_address), Integer.parseInt(deviceParam.getRegisterAddress()), dataType, device_ip, 502);
                        ValueOperations<String, String> stringStringValueOperations = stringRedisTemplate.opsForValue();
                        stringStringValueOperations.set(station_id.toString()+deviceParam.getDeviceParamsName(), number.toString());
                        System.out.println("numner:" + number);
                    } catch (ModbusTransportException e) {
                        System.out.println("连接异常，请检查modbus IP地址");
                    } catch (ErrorResponseException e) {
                        System.out.println("查询结果有误");
                    } catch (ModbusInitException e) {
                        System.out.println("modbus初始化错误");
                    }
                }

            }
        }

    }

}