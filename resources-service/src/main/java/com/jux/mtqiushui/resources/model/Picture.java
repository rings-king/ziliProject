package com.jux.mtqiushui.resources.model;

import javax.persistence.*;

@Entity
@Table(name = "picture")
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "picture_name")
    private String pictureName;

    @Column(name = "picture_width")
    private String pictureWidth;

    @Column(name = "picture_height")
    private String pictureHeight;

    @Column(name = "picture_url")
    private String pictureUrl;

    public Picture() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getPictureWidth() {
        return pictureWidth;
    }

    public void setPictureWidth(String pictureWidth) {
        this.pictureWidth = pictureWidth;
    }

    public String getPictureHeight() {
        return pictureHeight;
    }

    public void setPictureHeight(String pictureHeight) {
        this.pictureHeight = pictureHeight;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "id=" + id +
                ", pictureName='" + pictureName + '\'' +
                ", pictureWidth='" + pictureWidth + '\'' +
                ", pictureHeight='" + pictureHeight + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                '}';
    }
}
