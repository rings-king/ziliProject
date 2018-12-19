package com.jux.mtqiushui.resources.services;

import com.jux.mtqiushui.resources.model.JuniorMaterial;
import com.jux.mtqiushui.resources.repository.JuniorMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JuniorMaterialService {

    @Autowired
    private JuniorMaterialRepository juniorMaterialRepository;

    /**
     * 添加
     * @param juniorMaterial
     * @return
     */
    public JuniorMaterial addJuniorMaterial(JuniorMaterial juniorMaterial) {
        return juniorMaterialRepository.save(juniorMaterial);
    }
}
