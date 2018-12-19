package com.jux.mtqiushui.dispatching.repository;

import com.jux.mtqiushui.dispatching.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station,Long> {
}
