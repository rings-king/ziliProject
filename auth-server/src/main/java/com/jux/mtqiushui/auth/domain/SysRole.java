package com.jux.mtqiushui.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jux.mtqiushui.auth.domain.Vo.XtgnbVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wangyunfei on 2017/6/9.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRole extends AbstractAuditingEntity{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String name;
    private String value;
    private String rolePermission;
    @Transient
    private List<XtgnbVo> xtgnbList;
    @Transient
    private String xtgnbText;

    @Column(name = "updateTime",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updateTime;

    @JsonIgnore
    @ManyToMany(targetEntity = SysAuthority.class,fetch = FetchType.EAGER,cascade={CascadeType.REMOVE })
    @BatchSize(size = 20)
    private Set<SysAuthority> authorities = new HashSet<>();


}
