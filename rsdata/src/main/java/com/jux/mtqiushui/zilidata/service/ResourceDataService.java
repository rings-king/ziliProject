package com.jux.mtqiushui.zilidata.service;

import com.jux.mtqiushui.zilidata.model.ResourceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Auther: fp
 * @Date: 2018/12/7 16:19
 * @Description:
 */
@Service
public class ResourceDataService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ResourceData getResourceData(String materialCode) {
        String sql = "select id,material_model,material_name from material_define where material_code = " + "'" + materialCode + "'";
        System.out.println("sql:"+sql);
        List<ResourceData> query = jdbcTemplate.query(sql, new RowMapper<ResourceData>() {
            @Override
            public ResourceData mapRow(ResultSet rs, int rowNum) throws SQLException {
                ResourceData resourceData = new ResourceData();
                resourceData.setId(rs.getLong("id"));
                resourceData.setMaterialModel(rs.getString("material_model"));
                resourceData.setMaterialName(rs.getString("material_name"));
                return resourceData;
            }
        });
        System.out.println("query:"+query);
        return query.get(0);
    }
}
