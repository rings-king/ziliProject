package com.jux.mtqiushui.auth.controller;

import com.jux.mtqiushui.auth.domain.Organization;
import com.jux.mtqiushui.auth.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping(value="v1/organizations")
public class OrganizationServiceController {
    @Autowired
    private OrganizationService orgService;


    private static final Logger logger = LoggerFactory.getLogger(OrganizationServiceController.class);

    /**
     * 创建组织
     * @param orgName
     * @param user
     * @return
     */
    @RequestMapping(value = "/registerOrg/{organizationName}", method = RequestMethod.POST)
    public ResponseEntity registerOrg(@PathVariable("organizationName") String orgName, Principal user) {
        System.out.println("*******************"+orgName);
        Organization organization = orgService.registerOrg(orgName, user.getName());
        return organization != null ? new ResponseEntity<>(organization.getId(),HttpStatus.OK) : new ResponseEntity<>("创建组织失败",HttpStatus.NO_CONTENT);
    }

    /**
     * 修改组织名
     * @param map
     * @return
     */
    @RequestMapping(value = "/modifyOrgName", method = RequestMethod.POST)
    public ResponseEntity modifyOrgName(@RequestBody Map<String,String> map) {
        String uuid = map.get("uuid");
        String newOrgName = map.get("newOrgName");
        return orgService.modifyOrgName(uuid, newOrgName) != null ? new ResponseEntity<>("修改组织名成功",HttpStatus.OK) : new ResponseEntity<>("修改组织名失败",HttpStatus.NO_CONTENT);
    }

    /**
     * 根据用户Id返回对应的组织信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Long findOrgIdByUserId(@PathVariable("userId") Long userId) {
        return orgService.findOrgIdByUserId(userId);

    }
//    @RequestMapping(value="/{organizationId}",method = RequestMethod.GET)
//    public Organization getOrganization(@PathVariable("organizationId") String organizationId) {
//        logger.debug(String.format("Looking up data for org {}", organizationId));
//
//        Organization org = orgService.getOrg(organizationId);
//        org.setContactName("OLD::" + org.getContactName());
//        return org;
//    }
//
//    @RequestMapping(value="/{organizationId}",method = RequestMethod.PUT)
//    public void updateOrganization( @PathVariable("organizationId") String orgId, @RequestBody Organization org) {
//        orgService.updateOrg( org );
//    }
//
//    @RequestMapping(value="/{organizationId}",method = RequestMethod.POST)
//    public void saveOrganization(@RequestBody Organization org) {
//       orgService.saveOrg( org );
//    }
//
//    @RequestMapping(value="/{organizationId}",method = RequestMethod.DELETE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteOrganization( @PathVariable("orgId") String orgId,  @RequestBody Organization org) {
//        orgService.deleteOrg( org );
//    }
}
