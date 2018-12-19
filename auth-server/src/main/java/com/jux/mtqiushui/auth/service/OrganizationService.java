package com.jux.mtqiushui.auth.service;

import com.jux.mtqiushui.auth.domain.Organization;
import com.jux.mtqiushui.auth.repository.OrganizationRepository;
import com.jux.mtqiushui.auth.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository orgRepository;
    @Autowired
    private SysUserRepository userRepository;

    public Organization getOrg(String organizationId) {
        return orgRepository.findById(organizationId);
    }

    /**
     * 创建组织
     * @param orgName
     * @param userName
     * @return
     */
    @Transactional
    public Organization registerOrg(String orgName, String userName) {
        Organization org  = new Organization();
        org.setName(orgName);
        org.setContactId(userRepository.findByUsername(userName).getId());
        return orgRepository.save(org);
    }

    /**
     * 修改组织名
     * @param uuid
     * @param newOrgName
     * @return
     */
    @Transactional
    public Organization modifyOrgName(String uuid, String newOrgName) {
        Organization org = orgRepository.findById(uuid);
        org.setName(newOrgName);
        return  orgRepository.save(org);
    }

    /**
     * 根据联系人Id返回对应的组织信息
     * @param userId
     * @return
     */
    public Long findOrgIdByUserId(Long userId) {
        Organization byContactId = orgRepository.findByContactId(userId);
        return byContactId != null ? byContactId.getId() : null;
    }
//    public void saveOrg(Organization org){
//        org.setId( UUID.randomUUID().toString());
//
//        orgRepository.save(org);
//
//    }

    public void updateOrg(Organization org){
        orgRepository.save(org);
    }

    public void deleteOrg(Organization org){
        orgRepository.delete( org.getId());
    }
}
