package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.DeviceParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceParamRepository extends JpaRepository<DeviceParam, Long>, PagingAndSortingRepository<DeviceParam, Long> {
}
