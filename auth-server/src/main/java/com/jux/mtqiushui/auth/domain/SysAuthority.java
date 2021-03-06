package com.jux.mtqiushui.auth.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by wangyunfei on 2017/6/9.
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public  class SysAuthority extends AbstractAuditingEntity{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String value;
}
