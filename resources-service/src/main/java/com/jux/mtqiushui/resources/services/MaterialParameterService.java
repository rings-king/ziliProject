package com.jux.mtqiushui.resources.services;

import com.jux.mtqiushui.resources.model.MaterialParameter;
import com.jux.mtqiushui.resources.repository.MaterialParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MaterialParameterService {

    @Autowired
    private MaterialParameterRepository materialParameterRepository;

    public MaterialParameter addMaterialParameter(MaterialParameter materialParameter) {
        return materialParameterRepository.save(materialParameter);
    }
}
