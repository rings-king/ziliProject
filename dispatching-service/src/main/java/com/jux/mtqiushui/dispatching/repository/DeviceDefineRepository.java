package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.DeviceDefine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceDefineRepository extends JpaRepository<DeviceDefine, Long>, PagingAndSortingRepository<DeviceDefine, Long> {

    @Query(value = "select id,device_address,device_code,device_ip,device_model,device_name,production_line,production_line_id,station_id,station_name,update_time from device_define where id in (:ids) order by update_time", nativeQuery = true)
    public List<DeviceDefine> findDeviceDefineListByIds(@Param("ids") List<Long> longs);
}
