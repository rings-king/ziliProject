package com.jux.mtqiushui.auth.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "organizations")
public class Organization {
    // 组织ID（UUID)
    @Id
    @Column(name = "organization_id", nullable = false)
    Long id;
    // 组织名
    @Column(name = "name", nullable = false)
    String name;
    // 联系人ID
    @Column(name = "contact_id", nullable = false)
    Long contactId;

    public Organization() {
    }

    public Organization(Long id, String name, Long contactId) {
        this.id = id;
        this.name = name;
        this.contactId = contactId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactId=" + contactId +
                '}';
    }
}
