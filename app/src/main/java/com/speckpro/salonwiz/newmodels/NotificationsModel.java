package com.speckpro.salonwiz.newmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationsModel implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("Link")
    @Expose
    private String link;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("IsRead")
    @Expose
    private Integer isRead;
    @SerializedName("isOf")
    @Expose
    private Integer isOf;
    @SerializedName("isFor")
    @Expose
    private String isFor;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    private final static long serialVersionUID = 4233591853656671265L;

    public NotificationsModel(Integer id, String description, Integer isOf, String isFor, Integer isRead, String createdAt, String updatedAt) {
        this.id = id;
        this.description = description;
        this.isOf = isOf;
        this.isFor = isFor;
        this.isRead = isRead;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public Integer getIsOf() {
        return isOf;
    }

    public void setIsOf(Integer isOf) {
        this.isOf = isOf;
    }

    public String getIsFor() {
        return isFor;
    }

    public void setIsFor(String isFor) {
        this.isFor = isFor;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
