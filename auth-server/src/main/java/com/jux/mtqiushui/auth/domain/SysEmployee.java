package com.jux.mtqiushui.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "employee")
@Entity
@Data
public class SysEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String empNumber;
    private String empName;
    private Long quarterId;
    private Long departmentId;
    @Column(name = "updateTime",nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updateTime;

}
