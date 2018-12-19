package com.jux.mtqiushui.auth.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "quarter")
public class Quarter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //id 主键
    private Long id;
    //岗位名称
    @Column(name = "quartername")
    private String quarterName;
    //部门id
    @Column(name = "deptid")
    private Long deptId;



}
