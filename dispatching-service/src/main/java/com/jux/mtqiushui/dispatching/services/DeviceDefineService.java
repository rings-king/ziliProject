package com.jux.mtqiushui.dispatching.services;

import com.jux.mtqiushui.dispatching.model.DeviceDefine;
import com.jux.mtqiushui.dispatching.model.DeviceParam;
import com.jux.mtqiushui.dispatching.model.SearchCondition;
import com.jux.mtqiushui.dispatching.repository.DeviceDefineRepository;
import com.jux.mtqiushui.dispatching.repository.DeviceParamRepository;
import com.jux.mtqiushui.dispatching.util.ObjectIsNull;
import com.jux.mtqiushui.dispatching.util.SearchFarmat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class DeviceDefineService {

    @Autowired
    private DeviceDefineRepository deviceDefineRepository;

    @Autowired
    private DeviceParamRepository deviceParamRepository;

    @Autowired
    private ProductionLineService productionLineService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加设备定义
     * @param deviceDefine
     * @return
     * @throws Exception
     */
    @Transactional
    public DeviceDefine addDeviceDefine(DeviceDefine deviceDefine) throws Exception {
        Set<DeviceParam> deviceParamsTab = deviceDefine.getDeviceParamsTab();
        for (DeviceParam deviceParam : deviceParamsTab) {
            deviceParam.setDeviceDefine(deviceDefine);
        }
        DeviceDefine save = deviceDefineRepository.save(deviceDefine);
        return save;
    }

    public List<DeviceDefine> getAllDeviceDefine() {
        List<DeviceDefine> all = deviceDefineRepository.findAll();
        return all;
    }

    /**
     * 获取所有的设备定义
     * @param pageable
     * @return
     * @throws Exception
     */
    public Page<DeviceDefine> getAllDeviceDefine(Pageable pageable) throws Exception{
        Page<DeviceDefine> all = deviceDefineRepository.findAll(pageable);
        return all;
    }

    /**
     * 据计设备定义ID返回对应的设备定义信息
     * @param deviceDefineId
     * @return
     * @throws Exception
     */
    public DeviceDefine getDeviceDefineById(Long deviceDefineId) throws Exception {
        DeviceDefine one = deviceDefineRepository.findOne(deviceDefineId);
        return one;
    }

    /**
     * 据计设备定义ID删除对应的设备定义信息
     * @param deviceDefineId
     * @throws Exception
     */
    @Transactional
    public void deleteDeviceDefineById(Long deviceDefineId) throws Exception {
        deviceDefineRepository.delete(deviceDefineId);
    }

    /**
     * 修改设备定义
     * @param listDeviceDefine
     * @param deviceDefineId
     * @return
     */
    @Transactional
    public Boolean modifyDeviceDefine(List<DeviceDefine> listDeviceDefine, Long deviceDefineId) throws Exception{
        // 获得添加的设备定义及子表信息
        DeviceDefine addDeviceDefine= listDeviceDefine.get(0);
        Boolean addnewBoolean = addNewDeviceDefine(addDeviceDefine, deviceDefineId);

        // 获得删除的设备定义及子表信息
        DeviceDefine deleteDeviceDefine = listDeviceDefine.get(1);
        Boolean deleteBoolean = deleteDeviceDefine(deleteDeviceDefine, deviceDefineId);

        // 获得更新的设备定义及子表信息
        DeviceDefine updateDeviceDefine = listDeviceDefine.get(2);
        Boolean updateBoolean = updateDeviceDefine(updateDeviceDefine, deviceDefineId);

        if (addnewBoolean == true && deleteBoolean == true && updateBoolean == true) {
            return true;
        }
        return false;
    }

    /**
     * 修改设备定义中的addnew
     * @param addDeviceDefine
     * @param deviceDefineId
     * @return
     */
    private Boolean addNewDeviceDefine(DeviceDefine addDeviceDefine, Long deviceDefineId) throws Exception{

        DeviceDefine deviceDefine = null;
        try {
            deviceDefine = getDeviceDefineById(deviceDefineId);
            System.out.println("添加前:"+deviceDefine);
            if (deviceDefine == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        boolean addAllFieldNull = ObjectIsNull.isAllFieldNull(addDeviceDefine);
        if (addAllFieldNull == false) {
            deviceDefine.setUpdateTime(addDeviceDefine.getUpdateTime());
            Set<DeviceParam> deviceParamsTab = addDeviceDefine.getDeviceParamsTab();
            if (deviceParamsTab.size() != 0) {
                Set<DeviceParam> deviceParamsTabs = deviceDefine.getDeviceParamsTab();
                for (DeviceParam deviceParam : deviceParamsTab) {
                    deviceParam.setDeviceDefine(deviceDefine);
                    deviceParamsTabs.add(deviceParam);
                }
            }
        }

        DeviceDefine save = deviceDefineRepository.save(deviceDefine);
        return save != null ? true : false;
    }

    /**
     * 修改设备定义中的remove
     * @param deleteDeviceDefine
     * @param deviceDefineId
     * @return
     */
    private Boolean deleteDeviceDefine(DeviceDefine deleteDeviceDefine, Long deviceDefineId) throws Exception{

        DeviceDefine deviceDefine = null;
        try {
            deviceDefine = getDeviceDefineById(deviceDefineId);
            System.out.println("删除前:"+deviceDefine);
            if (deviceDefine == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        boolean addAllFieldNull = ObjectIsNull.isAllFieldNull(deleteDeviceDefine);
        if (addAllFieldNull == false) {
            deviceDefine.setUpdateTime(deleteDeviceDefine.getUpdateTime());
            Set<DeviceParam> deviceParamsTab = deleteDeviceDefine.getDeviceParamsTab();
            if (deviceParamsTab.size() != 0) {
                Set<DeviceParam> deviceParamsTabs = deviceDefine.getDeviceParamsTab();
                Iterator<DeviceParam> iterator = deviceParamsTabs.iterator();
                for (DeviceParam deviceParam : deviceParamsTab) {
                    while (iterator.hasNext()) {
                        DeviceParam next = iterator.next();
                        if (deviceParam.getId().longValue() == next.getId().longValue()) {
                            iterator.remove();
                            break;
                        }
                    }
                }

            }
        }

        DeviceDefine save = deviceDefineRepository.save(deviceDefine);
        return save != null ? true : false;
    }

    /**
     * 修改设备定义中的update
     * @param updateDeviceDefine
     * @param deviceDefineId
     * @return
     */
    private Boolean updateDeviceDefine(DeviceDefine updateDeviceDefine, Long deviceDefineId) throws Exception{

        DeviceDefine deviceDefine = null;
        try {
            deviceDefine = getDeviceDefineById(deviceDefineId);
            System.out.println("更新前:"+deviceDefine);
            if (deviceDefine == null) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        boolean addAllFieldNull = ObjectIsNull.isAllFieldNull(updateDeviceDefine);
        if (addAllFieldNull == false) {
            if (updateDeviceDefine.getDeviceCode() != null) {
                deviceDefine.setDeviceCode(updateDeviceDefine.getDeviceCode());
            }
            if (updateDeviceDefine.getDeviceName() != null) {
                deviceDefine.setDeviceName(updateDeviceDefine.getDeviceName());
            }
            if (updateDeviceDefine.getDeviceModel() != null) {
                deviceDefine.setDeviceModel(updateDeviceDefine.getDeviceModel());
            }
            if (updateDeviceDefine.getDeviceIp() != null) {
                deviceDefine.setDeviceIp(updateDeviceDefine.getDeviceIp());
            }
            if (updateDeviceDefine.getDeviceAddress() != null) {
                deviceDefine.setDeviceAddress(updateDeviceDefine.getDeviceAddress());
            }
            if (updateDeviceDefine.getUpdateTime() != null) {
                deviceDefine.setUpdateTime(updateDeviceDefine.getUpdateTime());
            }
            if (updateDeviceDefine.getProductionLineId() != null || updateDeviceDefine.getStationId() != null) {
                Map<String, String> map = productionLineService.getProductionLineNameAndStationName(updateDeviceDefine.getProductionLineId(), updateDeviceDefine.getStationId());
                if (map == null) {
                    return false;
                }
                deviceDefine.setProductionLineId(updateDeviceDefine.getProductionLineId());
                deviceDefine.setStationId(updateDeviceDefine.getStationId());
                deviceDefine.setProductionLine(map.get("productionLineName"));
                deviceDefine.setStationName(map.get("stationName"));
            }

            deviceDefine.setUpdateTime(updateDeviceDefine.getUpdateTime());
            Set<DeviceParam> deviceParamsTab = updateDeviceDefine.getDeviceParamsTab();
            if (deviceParamsTab.size() != 0) {
                Set<DeviceParam> deviceParamsTabs = deviceDefine.getDeviceParamsTab();
                for (DeviceParam deviceParam : deviceParamsTab) {
                    for (DeviceParam paramsTab : deviceParamsTabs) {
                        if (paramsTab.getId().longValue() == deviceParam.getId().longValue()) {
                            if (deviceParam.getDeviceParamsName() != null) {
                                paramsTab.setDeviceParamsName(deviceParam.getDeviceParamsName());
                            }
                            if (deviceParam.getRegisterAddress() != null) {
                                paramsTab.setRegisterAddress(deviceParam.getRegisterAddress());
                            }
                            if (deviceParam.getDeviceParamsType() != null) {
                                paramsTab.setDeviceParamsType(deviceParam.getDeviceParamsType());
                            }
                            break;
                        }
                    }
                }
            }
        }
        System.out.println("更新后:"+deviceDefine);
        try {
            DeviceDefine saveDeviceDefine = addDeviceDefine(deviceDefine);
            if (saveDeviceDefine != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 查询所有设备定义
     * @return
     */
    public List<DeviceDefine> selectAll() {
        List<DeviceDefine> all = deviceDefineRepository.findAll();
        return  all;
    }

    /**
     * 多条件查询
     * @param conditionList
     * @return
     */
    public List<DeviceDefine> searchDeviceDefine(List<SearchCondition> conditionList) throws Exception{

        if (conditionList.size() == 0){
            return null;
        }

        List<SearchCondition> deviceDefineList = new ArrayList<>();
        List<SearchCondition> deviceParamList = new ArrayList<>();
        for (SearchCondition searchCondition : conditionList) {
            if (searchCondition.getTablename().equals("Mes_DeviceDefine_Mes_DeviceDefinesStore")) {
                deviceDefineList.add(searchCondition);
                continue;
            }

            if (searchCondition.getTablename().equals("Mes_DeviceDefine_deviceParamsTabsStore")) {
                deviceParamList.add(searchCondition);
                continue;
            }
        }

        if (deviceDefineList.size() > 0) {
            String deviceDefineSql = " ";
            for (int i = 0; i < deviceDefineList.size(); i++) {

                deviceDefineSql += SearchFarmat.getNew(conditionList.get(i));

                if (i < deviceDefineList.size() - 1){
                    deviceDefineSql += " and " ;
                }

            }
            if (deviceDefineSql.contains("deviceCode")) {
                deviceDefineSql = deviceDefineSql.replace("deviceCode", "device_code");
            }
            if (deviceDefineSql.contains("deviceName")) {
                deviceDefineSql = deviceDefineSql.replace("deviceName","device_name");
            }
            if (deviceDefineSql.contains("deviceModel")) {
                deviceDefineSql = deviceDefineSql.replace("deviceModel","device_model");
            }
            if (deviceDefineSql.contains("deviceIp")) {
                deviceDefineSql = deviceDefineSql.replace("deviceIp","device_ip");
            }
            if (deviceDefineSql.contains("deviceAddress")) {
                deviceDefineSql = deviceDefineSql.replace("deviceAddress","device_address");
            }
            if (deviceDefineSql.contains("productionLineId")) {
                deviceDefineSql = deviceDefineSql.replace("productionLineId","production_line_id");
            }
            if (deviceDefineSql.contains("stationId")) {
                deviceDefineSql = deviceDefineSql.replace("stationId","station_id");
            }

            String defineSql = "select id,device_address,device_code,device_ip,device_model,device_name,production_line,production_line_id,station_id,station_name from device_define where " + deviceDefineSql + " order by update_time desc";
            List<DeviceDefine> query = jdbcTemplate.query(defineSql, new RowMapper<DeviceDefine>() {
                @Override
                public DeviceDefine mapRow(ResultSet resultSet, int i) throws SQLException {
                    DeviceDefine deviceDefine = new DeviceDefine();
                    deviceDefine.setId(resultSet.getLong("id"));
                    deviceDefine.setDeviceAddress(resultSet.getString("device_address"));
                    deviceDefine.setDeviceCode(resultSet.getString("device_code"));
                    deviceDefine.setDeviceIp(resultSet.getString("device_ip"));
                    deviceDefine.setDeviceModel(resultSet.getString("device_model"));
                    deviceDefine.setDeviceName(resultSet.getString("device_name"));
                    deviceDefine.setProductionLine(resultSet.getString("production_line"));
                    deviceDefine.setProductionLineId(resultSet.getLong("production_line_id"));
                    deviceDefine.setStationId(resultSet.getLong("station_id"));
                    deviceDefine.setStationName(resultSet.getString("station_name"));
                    return deviceDefine;
                }
            });
            System.out.println("defineSql:"+defineSql);
            return query;
        }

        String deviceParamSql = " ";
        for (int i = 0; i < deviceParamList.size(); i++) {

            deviceParamSql += SearchFarmat.getNew(conditionList.get(i));

            if (i < deviceParamList.size() - 1){
                deviceParamSql += " and " ;
            }

        }
        if (deviceParamSql.contains("deviceParamsType")) {
            deviceParamSql = deviceParamSql.replace("deviceParamsType", "device_params_type");
        }
        if (deviceParamSql.contains("deviceParamsName")) {
            deviceParamSql = deviceParamSql.replace("deviceParamsName","device_params_name");
        }
        if (deviceParamSql.contains("registerAddress")) {
            deviceParamSql = deviceParamSql.replace("registerAddress","register_address");
        }
//        id,device_params_name,device_params_type,register_address
        String paramSql = "select device_define_id from device_param where" + deviceParamSql;
        System.out.println("paramSql:" + paramSql);
        List<Long> longs = jdbcTemplate.queryForList(paramSql, Long.class);
        System.out.println("logs:"+longs);
        if (longs.size() > 0) {
            String newSql = "select id,device_address,device_code,device_ip,device_model,device_name,production_line,production_line_id,station_id,station_name from device_define where id in (";
            for (int i = 0; i < longs.size(); i++) {
                newSql = newSql + longs.get(i);
                if (i < longs.size() - 1) {
                    newSql = newSql + ",";
                }
            }
            newSql = newSql + ") order by update_time";
            System.out.println("newSql:"+newSql);
            List<DeviceDefine> query = jdbcTemplate.query(newSql, new RowMapper<DeviceDefine>() {
                @Override
                public DeviceDefine mapRow(ResultSet resultSet, int i) throws SQLException {
                    DeviceDefine deviceDefine = new DeviceDefine();
                    deviceDefine.setId(resultSet.getLong("id"));
                    deviceDefine.setDeviceAddress(resultSet.getString("device_address"));
                    deviceDefine.setDeviceCode(resultSet.getString("device_code"));
                    deviceDefine.setDeviceIp(resultSet.getString("device_ip"));
                    deviceDefine.setDeviceModel(resultSet.getString("device_model"));
                    deviceDefine.setDeviceName(resultSet.getString("device_name"));
                    deviceDefine.setProductionLine(resultSet.getString("production_line"));
                    deviceDefine.setProductionLineId(resultSet.getLong("production_line_id"));
                    deviceDefine.setStationId(resultSet.getLong("station_id"));
                    deviceDefine.setStationName(resultSet.getString("station_name"));
                    return deviceDefine;
                }
            });
            System.out.println("myquery:"+query);
            return query;
        }

//        System.out.println("longs:"+longs);
//        List<DeviceDefine> deviceDefineListByIds = null;
//        if (longs.size() > 0) {
//            deviceDefineListByIds = deviceDefineRepository.findDeviceDefineListByIds(longs);
//            if (deviceDefineListByIds == null) {
//                return new ArrayList<DeviceDefine>();
//            }
//        }

        return new ArrayList<DeviceDefine>();
    }

    /**
     * 根据工位id得到设备参数为0的设备名称
     * @param stationId
     * @return
     */
    public String getDeviceParamName(Long stationId) throws  Exception{
        if (stationId==null){
            return  null;
        }
        String sql="SELECT\n" +
                "\tdm.device_params_name\n"+
                "FROM\n" +
                "\tdevice_define AS d,\n" +
                "\tdevice_param AS dm\n" +
                "WHERE\n" +
                "\td.station_id =" + stationId +
                "\tAND dm.register_address =0\n" +
                "AND d.id = dm.device_define_id";
        System.out.println(sql);
        List<DeviceParam> deviceParam = jdbcTemplate.query(sql, new RowMapper<DeviceParam>() {
            @Override
            public DeviceParam mapRow(ResultSet resultSet, int i) throws SQLException {
                DeviceParam deviceParam = new DeviceParam();
                deviceParam.setDeviceParamsName(resultSet.getString("device_params_name"));
                return deviceParam;
            }
        });
    if (deviceParam.size()!=0){
        return  deviceParam.get(0).getDeviceParamsName();
        }
        return "";
    }
}
