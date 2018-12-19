package com.jux.mtqiushui.resources.services;

import com.jux.mtqiushui.resources.model.Material;
import com.jux.mtqiushui.resources.repository.MaterialRepository;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> getMaterialsByOrgId(Long organizationId) {
        return materialRepository.findByOrganizationId(organizationId);
    }

    public Material getMaterial(Long materialId) throws Exception {
        return materialRepository.findByMaterialId(materialId);
    }

    public Page<Material> getMaterialByOrganizationId(Long organizationId, Pageable pageable) throws Exception {
        Page<Material> materials = materialRepository.findByOrganizationId(organizationId, pageable);
        return materials;
    }

    public Page<Material> getAllMaterial(Pageable pageable) throws Exception {
        Page<Material> materials = materialRepository.findAll(pageable);
        return materials;
    }

    public void saveMaterial(Material material) throws Exception {
        if (material.getMaterialName() == null || "".equals(material.getMaterialName())) {
            throw new Exception("物料名称（materialName）必须填写。");
        }
        if (material.getOrganizationId() == null) {
            throw new Exception("组织ID（organizationId）必须编写。");
        }
        materialRepository.save(material);
    }

    @Transactional
    public void bulkSaveMaterialByOrganizationId(Long organizationId, ArrayList<Material> materials) throws Exception {
        if (organizationId == null) {
            throw new Exception("组织ID（organizationId）必须编写。");
        }
        for (Material material : materials) {
            if (material.getMaterialName() == null || "".equals(material.getMaterialName())) {
                throw new Exception("物料名称（materialName）必须填写。");
            }
            material.setOrganizationId(organizationId);
        }
        materialRepository.save(materials);
    }

    @Transactional
    public void bulkSaveMaterial(ArrayList<Material> materials) throws Exception {
        for (Material material : materials) {
            if (material.getMaterialName() == null || "".equals(material.getMaterialName())) {
                throw new Exception("物料名称（materialName）必须填写。");
            }
            if (material.getOrganizationId() == null) {
                throw new Exception("组织ID（organizationId）必须编写。");
            }
        }
        materialRepository.save(materials);
    }

    @Transactional
    public Integer deleteMaterialByOrganizationId(Long organizationId) throws Exception {
        if (organizationId == null) {
            throw new Exception("组织ID（organizationId）必须编写。");
        }
        return materialRepository.deleteMaterialByOrganizationId(organizationId);
    }

    public void deleteMaterial(Material material) throws Exception {
        if (getMaterial(material.getMaterialId()) == null) {
            throw new Exception(String.format("要删除的记录不存在, ID号 {%s}", material.getMaterialId()));
        }
        materialRepository.delete(material);
    }

    public void updateMaterial(Material material) throws Exception {
        if (getMaterial(material.getMaterialId()) == null) {
            throw new Exception(String.format("要修改的记录不存在, ID号 {%s}", material.getMaterialId()));
        }
        materialRepository.save(material);
    }

    public Integer countByOrganizationId(Long organizationId) {
        return materialRepository.countByOrganizationId(organizationId);
    }
}
