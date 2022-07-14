package com.speckpro.salonwiz.newmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DealsModel implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("deal")
    @Expose
    private String deal;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("UtilId")
    @Expose
    private Integer utilId;
    @SerializedName("IsAccepted")
    @Expose
    private Integer isAccepted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    private final static long serialVersionUID = 6976347425143834069L;

    public DealsModel(Integer id, String deal, String desc, Integer utilId, Integer isAccepted, String createdAt, String updatedAt) {
        this.id = id;
        this.deal = deal;
        this.desc = desc;
        this.utilId = utilId;
        this.isAccepted = isAccepted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getUtilId() {
        return utilId;
    }

    public void setUtilId(Integer utilId) {
        this.utilId = utilId;
    }

    public Integer getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Integer isAccepted) {
        this.isAccepted = isAccepted;
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
