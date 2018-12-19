package com.jux.mtqiushui.resources.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: fp
 * @Date: 2018/11/26 09:15
 * @Description:
 */
@Entity
@Table(name = "document_manage")
public class DocumentManage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "suffix")
    private String suffix;
    @JsonIgnore
    @Column(name = "user_name")
    private String userName;
    @Column(name = "update_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updateTime;

    @Transient
    private String url;

    public DocumentManage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "DocumentManage{" +
          "id=" + id +
          ", fileName='" + fileName + '\'' +
          ", suffix='" + suffix + '\'' +
          ", userName='" + userName + '\'' +
          ", updateTime=" + updateTime +
          ", url='" + url + '\'' +
          '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
