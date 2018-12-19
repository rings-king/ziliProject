package com.jux.mtqiushui.resources.services;

import com.jux.mtqiushui.resources.model.MeasurementUnit;
import com.jux.mtqiushui.resources.model.SearchCondition;
import com.jux.mtqiushui.resources.repository.MeasurementUnitRepository;
import com.jux.mtqiushui.resources.util.SearchFarmat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class MeasurementUnitService {

    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加一个计量单位
     * @param measurementUnit
     * @return
     * @throws Exception
     */
    public MeasurementUnit addMeasurementUnit(MeasurementUnit measurementUnit) throws Exception {
        MeasurementUnit save = measurementUnitRepository.save(measurementUnit);
        return save;
    }

    /**
     * 根据计量单位编码查询对应的计量单位信息
     * @param measureUnitCode
     * @return
     * @throws Exception
     */
    public MeasurementUnit getMeasurementUnitByCode(String measureUnitCode) throws Exception {
        MeasurementUnit byMeasureUnitCode = measurementUnitRepository.findByMeasureUnitCode(measureUnitCode);
        return byMeasureUnitCode;
    }

    /**
     * 根据计量单位Id返回对应的计量单位对象
     * @param measurementUnitId
     * @return
     * @throws Exception
     */
    public MeasurementUnit getMeasurementUnitById(Long measurementUnitId) throws Exception {
        MeasurementUnit one = measurementUnitRepository.findOne(measurementUnitId);
        return one;
    }

    /**
     * 返回所有计量单位 支持分页
     * @param pageable
     * @return
     * @throws Exception
     */
    public Page<MeasurementUnit> getAllMeasurementUnit(Pageable pageable) throws Exception {
        Page<MeasurementUnit> all = measurementUnitRepository.findAll(pageable);
        return all;
    }

    public void deleteMeasurementUnitById(Long measurementUnitId) throws Exception {
        measurementUnitRepository.delete(measurementUnitId);
    }

    /**
     * 多条件查询
     * @param conditionList
     * @return
     */
    public List<MeasurementUnit> getMeasurementUnit(List<SearchCondition> conditionList) throws Exception{

        if (conditionList.size() == 0){
            return null;
        }

        String aNew = " ";
        for (int i = 0; i < conditionList.size(); i++) {

            aNew += SearchFarmat.getNew(conditionList.get(i));

            if (i<conditionList.size() - 1){
                aNew += " and " ;
            }

        }
        if (aNew.contains("measureUnitCode")) {
            aNew = aNew.replace("measureUnitCode", "measure_unit_code");
        }
        if (aNew.contains("measureUnitName")) {
            aNew = aNew.replace("measureUnitName","measure_unit_name");
        }
        System.out.println("aNew:" + aNew);
        String sql = "select id,measure_unit_code,measure_unit_name from measurement_unit where" + aNew + " order by update_time desc";
        System.out.println("sql:" + sql);
        List<MeasurementUnit> query = jdbcTemplate.query(sql, new RowMapper<MeasurementUnit>() {
            @Override
            public MeasurementUnit mapRow(ResultSet resultSet, int i) throws SQLException {
                MeasurementUnit measurementUnit = new MeasurementUnit();
                measurementUnit.setId(resultSet.getLong("id"));
                measurementUnit.setMeasureUnitCode(resultSet.getString("measure_unit_code"));
                measurementUnit.setMeasureUnitName(resultSet.getString("measure_unit_name"));
                return measurementUnit;
            }
        });

        return query;
    }

    /**
     * 根据计量单位名称查询对应的计量单位对象
     * @param unitName
     * @return
     * @throws Exception
     */
    public Long getMeasurementUnitIdByUnitName(String unitName) throws Exception{
        MeasurementUnit byMeasureUnitName = measurementUnitRepository.findByMeasureUnitName(unitName);
        return byMeasureUnitName == null ? null : byMeasureUnitName.getId();
    }
}
