package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.LocationNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationNumRepository  extends JpaRepository<LocationNum,Long>,PagingAndSortingRepository<LocationNum,Long> {
}
