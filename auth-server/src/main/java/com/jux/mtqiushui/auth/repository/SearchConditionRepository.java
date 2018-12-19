package com.jux.mtqiushui.auth.repository;

import com.jux.mtqiushui.auth.domain.DefaultSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchConditionRepository extends JpaRepository<DefaultSearch,Long> {

    public DefaultSearch findByUserNameAndTableName(String userName,String tableName);
}
