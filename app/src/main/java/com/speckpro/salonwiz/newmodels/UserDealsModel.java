package com.speckpro.salonwiz.newmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserDealsModel implements Serializable {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("active")
    @Expose
    private List<Active> active = null;
    @SerializedName("userUtilities")
    @Expose
    private List<UserUtility> userUtilities = null;
    @SerializedName("expired")
    @Expose
    private List<Expired> expired = null;
    private final static long serialVersionUID = -6448829720292701153L;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Active> getActive() {
        return active;
    }

    public void setActive(List<Active> active) {
        this.active = active;
    }

    public List<UserUtility> getUserUtilities() {
        return userUtilities;
    }

    public void setUserUtilities(List<UserUtility> userUtilities) {
        this.userUtilities = userUtilities;
    }

    public List<Expired> getExpired() {
        return expired;
    }

    public void setExpired(List<Expired> expired) {
        this.expired = expired;
    }

    public class Active implements Serializable {

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
        private final static long serialVersionUID = -9170641844564955122L;

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

    }

    public class Expired implements Serializable {

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
        private final static long serialVersionUID = 3842838996456946406L;

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

    }

    public class UserUtility implements Serializable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private Object title;
        @SerializedName("image")
        @Expose
        private Object image;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("Supplier")
        @Expose
        private List<String> supplier = null;
        private final static long serialVersionUID = -6674503502588799920L;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public Object getImage() {
            return image;
        }

        public void setImage(Object image) {
            this.image = image;
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

        public List<String> getSupplier() {
            return supplier;
        }

        public void setSupplier(List<String> supplier) {
            this.supplier = supplier;
        }

    }

}
