package com.jux.mtqiushui.auth.domain;

import lombok.Data;

import javax.persistence.*;

@Table(name = "default_search_condition")
@Entity
@Data
public class DefaultSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long searchId;
    @Column(name = "userName")
    private String userName;
    @Column(name = "tableName")
    private String tableName;
    @Column(name = "searchConding")
    private String searchConding;

}
