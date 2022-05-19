
package com.example.salonwiz.loginauth.loginresponsepojo;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User {

    @SerializedName("InitLogin")
    @Expose
    private Boolean initLogin;
    @SerializedName("Role")
    @Expose
    private String role;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("BusinessAddress")
    @Expose
    private String businessAddress;
    @SerializedName("BusinessName")
    @Expose
    private String businessName;
    @SerializedName("ContactNumber")
    @Expose
    private String contactNumber;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("FirstName")
    @Expose
    private String firstName;
    @SerializedName("LastName")
    @Expose
    private String lastName;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("PostCode")
    @Expose
    private String postCode;
    @SerializedName("SocialMedia")
    @Expose
    private List<Object> socialMedia = null;
    @SerializedName("Filling")
    @Expose
    private List<Object> filling = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public Boolean getInitLogin() {
        return initLogin;
    }

    public void setInitLogin(Boolean initLogin) {
        this.initLogin = initLogin;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public List<Object> getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(List<Object> socialMedia) {
        this.socialMedia = socialMedia;
    }

    public List<Object> getFilling() {
        return filling;
    }

    public void setFilling(List<Object> filling) {
        this.filling = filling;
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

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
