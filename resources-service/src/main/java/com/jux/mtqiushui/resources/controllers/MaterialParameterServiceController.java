package com.jux.mtqiushui.resources.controllers;

import com.jux.mtqiushui.resources.model.MaterialParameter;
import com.jux.mtqiushui.resources.repository.AuthServiceApi;
import com.jux.mtqiushui.resources.services.MaterialParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "v1/materialParameter")
public class MaterialParameterServiceController {

    @Autowired
    private MaterialParameterService materialParameterService;

    @Autowired
    private AuthServiceApi userService;

    /**
     * 添加物料参数
     * @param materialParameter
     * @return
     */
    @RequestMapping(value = "addMaterialParameter", method = RequestMethod.POST)
    public Map<String, String> addMaterialParameter(MaterialParameter materialParameter) {
        Map<String, String> map = new HashMap<>();

        if (materialParameter == null) {
            map.put("error", "内容不能为空");
            return map;
        }

        MaterialParameter newMaterialParameter = materialParameterService.addMaterialParameter(materialParameter);
        if (newMaterialParameter == null) {
            map.put("error", "添加物料参数失败");
            return map;
        }
        map.put("success","true");
        return map;
    }

}
