package com.speckpro.salonwiz.newmodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApiResponse implements Serializable
{

    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = 8387144715012011600L;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
