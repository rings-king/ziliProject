package com.jux.mtqiushui.resources.model;

import javax.persistence.*;
import java.util.Date;

/**
 * @Auther: fp
 * @Date: 2018/11/23 17:16
 * @Description:
 */
@Entity
@Table(name = "notice_manage")
public class NoticeManage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name", nullable = true)
    private String userName;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "content", nullable = true)
    private String content;

    @Column(name = "is_read")
    private Integer isRead = 0;

    @Column(name = "update_time", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public NoticeManage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "NoticeManage{" +
          "id=" + id +
          ", userName='" + userName + '\'' +
          ", title='" + title + '\'' +
          ", content='" + content + '\'' +
          ", isRead=" + isRead +
          ", updateTime=" + updateTime +
          '}';
    }
}
