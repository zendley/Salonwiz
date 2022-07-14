package com.speckpro.salonwiz.newmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UtilitiesModel implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("IsChecked")
    @Expose
    private String IsChecked;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("Supplier")
    @Expose
    private List<String> supplier = null;
    private final static long serialVersionUID = -8532365124865406782L;

    public UtilitiesModel(Integer id, String title, String image, String createdAt, String updatedAt, List<String> supplier) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.supplier = supplier;
    }

    public UtilitiesModel() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsChecked() {
        return IsChecked;
    }

    public void setIsChecked(String isChecked) {
        IsChecked = isChecked;
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