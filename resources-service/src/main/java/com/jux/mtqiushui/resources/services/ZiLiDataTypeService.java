package com.jux.mtqiushui.resources.services;

import com.jux.mtqiushui.resources.model.KanbanType;
import com.jux.mtqiushui.resources.model.MaterialDefine;
import com.jux.mtqiushui.resources.model.MaterialType;
import com.jux.mtqiushui.resources.model.MeasurementUnit;
import com.jux.mtqiushui.resources.repository.MaterialDefineRepository;
import com.jux.mtqiushui.resources.repository.MaterialTypeRepository;
import com.jux.mtqiushui.resources.repository.MeasurementUnitRepository;
import com.jux.mtqiushui.resources.repository.ZiLiDataFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: fp
 * @Date: 2018/11/30 11:13
 * @Description:
 */
@Service
public class ZiLiDataTypeService {

    @Autowired
    private MaterialDefineRepository materialDefineRepository;

    @Autowired
    private MaterialTypeRepository materialTypeRepository;
    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    @Autowired
    private ZiLiDataFeign ziLiDataFeign;

    @Transactional
    public void addMaterial() throws Exception{
        List<KanbanType> kanBanTypes = ziLiDataFeign.getAllType();
        if (kanBanTypes.size() != 0) {
            addMaterialDefine(kanBanTypes);
        }
    }


    public void addMaterialDefine(List<KanbanType> kanBanTypes) throws Exception{
        List<MeasurementUnit> all = measurementUnitRepository.findAll();
        List<MaterialType> alls = materialTypeRepository.findAll();
        List<MaterialDefine> materialDefines = new ArrayList<>();
        for (KanbanType kanbanType : kanBanTypes) {
            MaterialDefine byMaterialCode = materialDefineRepository.findByMaterialCode(kanbanType.getId());
            if (byMaterialCode == null) {
                MaterialDefine materialDefine = new MaterialDefine();
                // 设置物料编码
                materialDefine.setMaterialCode(kanbanType.getId());
                // 设置物料名称
                materialDefine.setMaterialName(kanbanType.getZhuangxing());
                // 设置创建时间
                materialDefine.setUpdateTime(kanbanType.getCreatTime());
                // 设置备注
                materialDefine.setMaterialComment(kanbanType.getCommens());
                // 设置状态
                materialDefine.setStatus(kanbanType.getStatus());
                // 设置所属分类
                materialDefine.setMaterialModel(kanbanType.getCaizhi());

                materialDefine.setUnitId(all.get(0).getId());
                materialDefine.setMaterialTypeId(alls.get(0).getMaterialTypeId());
                materialDefines.add(materialDefine);
            }
        }
        materialDefineRepository.save(materialDefines);
    }

}
