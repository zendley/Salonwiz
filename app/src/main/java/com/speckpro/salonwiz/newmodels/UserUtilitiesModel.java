package com.speckpro.salonwiz.newmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserUtilitiesModel implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("utiltity")
    @Expose
    private String utiltity;
    @SerializedName("Supplier")
    @Expose
    private String supplier;
    @SerializedName("expirationdate")
    @Expose
    private String expirationdate;
    @SerializedName("billpaid")
    @Expose
    private Integer billpaid;
    @SerializedName("loa_form")
    @Expose
    private String loaForm;
    @SerializedName("last_bill")
    @Expose
    private String lastBill;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("IsAccepted")
    @Expose
    private Integer isAccepted;
    @SerializedName("dealRequested")
    @Expose
    private Integer dealRequested;
    @SerializedName("isFilled")
    @Expose
    private Integer isFilled;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("image")
    @Expose
    private String image;

    private final static long serialVersionUID = -8054762214271083259L;

    public UserUtilitiesModel(Integer id, String utiltity, String supplier, String expirationdate, Integer billpaid, String loaForm, String lastBill, Integer userId, Integer isAccepted, Integer dealRequested, Integer isFilled, String createdAt, String updatedAt, String image) {
        this.id = id;
        this.utiltity = utiltity;
        this.supplier = supplier;
        this.expirationdate = expirationdate;
        this.billpaid = billpaid;
        this.loaForm = loaForm;
        this.lastBill = lastBill;
        this.userId = userId;
        this.isAccepted = isAccepted;
        this.dealRequested = dealRequested;
        this.isFilled = isFilled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.image = image;
    }

    public UserUtilitiesModel(Integer id, String utiltity, String supplier, String expirationdate, Integer billpaid, String loaForm, String lastBill, Integer userId, Integer isAccepted, Integer dealRequested, Integer isFilled, String createdAt, String updatedAt) {
        this.id = id;
        this.utiltity = utiltity;
        this.supplier = supplier;
        this.expirationdate = expirationdate;
        this.billpaid = billpaid;
        this.loaForm = loaForm;
        this.lastBill = lastBill;
        this.userId = userId;
        this.isAccepted = isAccepted;
        this.dealRequested = dealRequested;
        this.isFilled = isFilled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUtiltity() {
        return utiltity;
    }

    public void setUtiltity(String utiltity) {
        this.utiltity = utiltity;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getExpirationdate() {
        return expirationdate;
    }

    public void setExpirationdate(String expirationdate) {
        this.expirationdate = expirationdate;
    }

    public Integer getBillpaid() {
        return billpaid;
    }

    public void setBillpaid(Integer billpaid) {
        this.billpaid = billpaid;
    }

    public String getLoaForm() {
        return loaForm;
    }

    public void setLoaForm(String loaForm) {
        this.loaForm = loaForm;
    }

    public String getLastBill() {
        return lastBill;
    }

    public void setLastBill(String lastBill) {
        this.lastBill = lastBill;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Integer isAccepted) {
        this.isAccepted = isAccepted;
    }

    public Integer getDealRequested() {
        return dealRequested;
    }

    public void setDealRequested(Integer dealRequested) {
        this.dealRequested = dealRequested;
    }

    public Integer getIsFilled() {
        return isFilled;
    }

    public void setIsFilled(Integer isFilled) {
        this.isFilled = isFilled;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}