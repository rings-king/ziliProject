package com.jux.mtqiushui.auth.repository;

import com.jux.mtqiushui.auth.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization,Long>, JpaRepository<Organization,Long> {
    public Organization findById(String organizationId);

    /**
     * 根据联系人Id返回对应的组织信息
     * @param contactId
     * @return
     */
    public Organization findByContactId(Long contactId);
}
