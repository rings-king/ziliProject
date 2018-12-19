package com.jux.mtqiushui.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wangyunfei on 2017/6/9.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SysUser extends AbstractAuditingEntity{
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @NotNull
    @Size(min = 1, max = 60)
    @Column(length = 60)
    @JsonIgnore
    private String password;

    @NotNull
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String realName;

    @NotNull
    @Size
    @Column(length = 11, unique = true, nullable = false)
    private String phoneNum;

    @Size(max = 50)
    @Column(length = 100)
    private String departmentId;

    @Email
    @Size(min = 5, max = 100)
    @Column(length = 100, unique = true)
    private String email;

    @Transient
    private String userDepartName;

    @Transient
    private List<String> roleList;

    @Column(name = "updateTime")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updateTime;

    @JsonIgnore
    @ManyToMany(targetEntity = SysRole.class, fetch = FetchType.EAGER,cascade = {CascadeType.MERGE})
    @BatchSize(size = 20)
    private Set<SysRole> roles = new HashSet<>();


    @Transient
    private Set<GrantedAuthority> authorities = new HashSet<>();


    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> userAuthotities = new HashSet<>();
        for(SysRole role : this.roles){
            for(SysAuthority authority : role.getAuthorities()){
                userAuthotities.add(new SimpleGrantedAuthority(authority.getValue()));
            }
        }

        return userAuthotities;
    }


}
