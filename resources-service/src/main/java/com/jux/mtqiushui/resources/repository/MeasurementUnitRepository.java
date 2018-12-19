package com.jux.mtqiushui.resources.repository;

import com.jux.mtqiushui.resources.model.MeasurementUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementUnitRepository extends JpaRepository<MeasurementUnit, Long>, PagingAndSortingRepository<MeasurementUnit, Long> {
    /**
     * 根据计量单位编码查询对应的计量单位对象
     * @param measureUnitCode
     * @return
     */
    public MeasurementUnit findByMeasureUnitCode(String measureUnitCode);

    /**
     * 根据计量单位名称查询对应的计量单位对象
     * @param unitName
     * @return
     */
    public MeasurementUnit findByMeasureUnitName(String unitName);

}
