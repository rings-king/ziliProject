package com.jux.mtqiushui.resources.controllers;

import com.jux.mtqiushui.resources.model.Material;
import com.jux.mtqiushui.resources.repository.AuthServiceApi;
import com.jux.mtqiushui.resources.services.MaterialService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(value = "v1/materials")
public class MaterialsServiceController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private AuthServiceApi userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialsServiceController.class);

    /**
     * 根据访问的用户获取该用户所属组织对应的所有物料清单
     * @param user
     * @return
     */
    @RequestMapping(value = "/getMaterialsByOrgId", method = RequestMethod.GET)
    public List<Material> getMaterialsByOrgId(Principal user) {
        Long userId = userService.getUserIdByUsername(user.getName());
        Long orgId = userService.getOrgIdByUserId(userId);
        List<Material> listMaterial = materialService.getMaterialsByOrgId(orgId);
        return listMaterial;
    }

    /**
     * 所有物料清单
     *
     * @param pageable
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('query-demo')")
    public ResponseEntity getAllMaterial(Pageable pageable) {
        LOGGER.info(String.format("materials-service getAllMaterial() 被调用: {%s}", materialService.getClass().getName()));
        Page<Material> materials;
        try {
            materials = materialService.getAllMaterial(pageable);
        } catch (Exception ex) {
            String errorMsg = String.format("调用异常：getMaterial(),错误信息：{%s}", ex.toString());
            LOGGER.error(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return materials != null ? new ResponseEntity<>(materials, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 获取指定ID的物料
     *
     * @param materialId
     * @return
     */
    @RequestMapping(value = "/{materialId}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('query-demo')")
    public ResponseEntity getMaterial(@PathVariable("materialId") Long materialId) {
        LOGGER.info(String.format("materials-service getMaterial() 被调用: {%s} 参数 {%s}", materialService.getClass().getName(), materialId));

        Material material;
        try {
            material = materialService.getMaterial(materialId);
        } catch (Exception ex) {
            LOGGER.error(String.format("调用异常：getMaterial(),错误信息：{%s}", ex.toString()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //material.setMaterialComment("OLD::" + material.getMaterialComment());
        return material != null ? new ResponseEntity<>(material, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/getbyorganizationid/{organizationId}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('query-demo')")
    public ResponseEntity<Page<Material>> getMaterialByOrganizationId(@PathVariable("organizationId") Long organizationId, Pageable pageable) {
        LOGGER.info(String.format("materials-service getMaterialByOrganizationId() 被调用: {%s} 参数 {%s}", materialService.getClass().getName(), organizationId));

        Page<Material> materials;
        try {
            materials = materialService.getMaterialByOrganizationId(organizationId, pageable);
        } catch (Exception ex) {
            LOGGER.error(String.format("调用异常：getMaterialByOrganizationId(),错误信息：{%s}", ex.toString()));
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //material.setMaterialComment("OLD::" + material.getMaterialComment());
        return materials != null ? new ResponseEntity<>(materials, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 添加具有指定信息的物料
     *
     * @param material
     * @return
     *
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('query-demo')")
    public ResponseEntity saveMaterial(@RequestBody Material material) {
        LOGGER.info(String.format("materials-service saveMaterial() 被调用: {%s} 参数 {%s}", materialService.getClass().getName(), material.getMaterialName()));
        try {
            materialService.saveMaterial(material);
        } catch (Exception ex) {
            String errorMsg = String.format("调用异常：saveMaterial(),错误信息：{%s}", ex.toString());
            LOGGER.error(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(material, HttpStatus.CREATED);
    }

    /**
     * 为某个组织批量添加物料
     *
     * @param organizationId
     * @param materials
     * @return
     */
    @RequestMapping(value = "/bulksavebyorganizationid/{organizationId}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('query-demo')")
    public ResponseEntity<Material> bulkSaveMaterialbyOrganizationId(@PathVariable("organizationId") Long organizationId, @RequestBody ArrayList<Material> materials) {
        LOGGER.info(String.format("materials-service bulkSaveMaterialbyOrganizationId() 被调用: {%s} 参数 {%s}", materialService.getClass().getName(), materials.getClass().getName()));
        //先删除所有该组织的物料，再添加这一批提交的物料
        try {
            materialService.deleteMaterialByOrganizationId(organizationId);
        } catch (Exception ex) {
            LOGGER.error(String.format("调用异常：deleteMaterialByOrganizationId(),错误信息：{%s}", ex.toString()));
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            materialService.bulkSaveMaterialByOrganizationId(organizationId, materials);
        } catch (Exception ex) {
            LOGGER.error(String.format("调用异常：bulkSaveMaterialbyOrganizationId(),错误信息：{%s}", ex.toString()));
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/bulksave/organizationid/{organizationId}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('query-demo')")
    public ResponseEntity<Material> bulkSaveMaterialbyOrganizationIdWithoutDelete(@PathVariable("organizationId") Long organizationId, @RequestBody ArrayList<Material> materials) {
        LOGGER.info(String.format("materials-service bulkSaveMaterialbyOrganizationIdWithoutDelete() 被调用: {%s} 参数 {%s}", materialService.getClass().getName(), materials.getClass().getName()));
        try {
            materialService.bulkSaveMaterialByOrganizationId(organizationId, materials);
        } catch (Exception ex) {
            LOGGER.error(String.format("调用异常：bulkSaveMaterialbyOrganizationId(),错误信息：{%s}", ex.toString()));
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/bulksave", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('query-demo')")
    public ResponseEntity<Material> bulkSaveMaterialWithoutDelete(@RequestBody ArrayList<Material> materials) {
        LOGGER.info(String.format("materials-service bulkSaveMaterialbyOrganizationIdWithoutDelete() 被调用: {%s} 参数 {%s}", materialService.getClass().getName(), materials.getClass().getName()));
        try {
            materialService.bulkSaveMaterial(materials);
        } catch (Exception ex) {
            LOGGER.error(String.format("调用异常：bulkSaveMaterialbyOrganizationId(),错误信息：{%s}", ex.toString()));
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     *
     * @param organizationId
     * @return
     */
    @RequestMapping(value = "/organizationid/{organizationId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('query-demo')")
    public ResponseEntity bulkDeleteMaterialbyOrganizationId(@PathVariable("organizationId") Long organizationId) {
        LOGGER.info("materials-service bulkDeleteMaterialbyOrganizationId() 被调用: {} ", materialService.getClass().getName());
        Integer rowsFind = materialService.countByOrganizationId(organizationId);

        if (rowsFind == 0) {
            return new ResponseEntity("该组织没有物料.", HttpStatus.UNPROCESSABLE_ENTITY);
        } else {

            Integer rowsDeleted;
            try {
                rowsDeleted = materialService.deleteMaterialByOrganizationId(organizationId);
            } catch (Exception ex) {
                String errorMsg = String.format("调用异常：deleteMaterialByOrganizationId(),错误信息：{%s}", ex.toString());
                LOGGER.error(errorMsg);
                return new ResponseEntity<>(errorMsg, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            String message = String.format("成功:对组织ID号为{%s}的物料批量删除,删除了{%s}条记录", organizationId, rowsDeleted);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    /**
     * 根据ID号删除指定物料
     *
     * @param materialId
     * @return
     */
    @RequestMapping(value = "/{materialId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('query-demo')")
    public ResponseEntity deleteMaterial(@PathVariable("materialId") Long materialId) {
        Material material;
        try {
            LOGGER.info(String.format("deleteMaterial()启动,开始查询ID号为 {%s} 的记录", materialId));
            material = materialService.getMaterial(materialId);
        } catch (Exception ex) {
            String errorMsg = String.format("调用异常：getMaterial(),错误信息：{%s}", ex.toString());
            LOGGER.error(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (material != null) {
            try {
                materialService.deleteMaterial(material);
            } catch (Exception ex) {
                String errorMsg = String.format("调用异常：deleteMaterial(),错误信息：{%s}", ex.toString());
                LOGGER.error(errorMsg);
                return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(material, HttpStatus.OK);
        } else {
            String errorMsg = String.format("错误: 想要删除的material记录并不存在, 记录号{%s}", materialId);
            LOGGER.warn(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * 根据ID号修改一个物料
     *
     * @param materialId
     * @param materialVO
     * @return
     */
    @RequestMapping(value = "/{materialId}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('query-demo')")
    public ResponseEntity updateMaterial(@PathVariable("materialId") Long materialId, @RequestBody Material materialVO) {
        Material material;

        if (materialVO.getMaterialId() != null) {
            if (!Objects.equals(materialVO.getMaterialId(), materialId)) {
                String errorMsg = String.format("输入有错误：updateMaterial(), 对{%s}的update请求体中包含有不同的materialId ：{%s}", materialId, materialVO.getMaterialId());
                LOGGER.error(errorMsg);
                return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        try {
            LOGGER.info(String.format("updateMaterial()启动,开始查询ID号为 {%s} 的记录", materialId));
            material = materialService.getMaterial(materialId);
        } catch (Exception ex) {
            String errorMsg = String.format("调用异常：getMaterial(),错误信息：{%s}", ex.toString());
            LOGGER.error(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (material != null) {
            try {
                material.copyNotNullFields(materialVO);
                materialService.updateMaterial(material);
            } catch (Exception ex) {
                String errorMsg = String.format("调用异常：updateMaterial()在执行saveMaterial时,错误信息：{%s}", ex.toString());
                LOGGER.error(errorMsg);
                return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(material, HttpStatus.OK);
        } else {
            String errorMsg = String.format("错误: 想要更新的material记录并不存在, 记录号{%s}", materialId);
            LOGGER.warn(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * 一个增加10万条示例记录的接口
     *
     *
     * @param lines
     * @param organizationId
     * @return
     */
    @RequestMapping(value = "/testinsert/{lines}/{organizationId}", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('query-demo')")
    public ResponseEntity insertBulkMaterial(@PathVariable("lines") int lines, @PathVariable("organizationId") Long organizationId) {
        ArrayList<Material> materials = new ArrayList();
        for (int i = 0; i < lines; i++) {
            Material material = new Material();
            material.setMaterialCode(String.format("Hcode%s", i));
            material.setMaterialComment(String.format("物料备注的字段%s", i));
            material.setMaterialModel(String.format("物料型号SDX%s", i));
            material.setMaterialName(String.format("名字%s", i));
            materials.add(material);
        }

        try {
            materialService.bulkSaveMaterialByOrganizationId(organizationId, materials);
        } catch (Exception ex) {
            String errorMsg = String.format("调用异常：insertBulkMaterial(),错误信息：{%s}", ex.toString());
            LOGGER.error(errorMsg);
            return new ResponseEntity<>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
