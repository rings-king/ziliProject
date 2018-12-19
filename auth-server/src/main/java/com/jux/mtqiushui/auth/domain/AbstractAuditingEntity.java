package com.jux.mtqiushui.auth.domain;

import lombok.Data;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

//    @CreatedBy
//    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
//    @JsonIgnore
//    private String createdBy;

//    @CreatedDate
//    @Column(name = "created_date", nullable = false)
//    @JsonIgnore
//    private Instant createdDate = Instant.now();

//    @LastModifiedBy
//    @Column(name = "last_modified_by", length = 50)
//    @JsonIgnore
//    private String lastModifiedBy;

//    @LastModifiedDate
//    @Column(name = "last_modified_date")
//    @JsonIgnore
//    private Instant lastModifiedDate = Instant.now();


}
