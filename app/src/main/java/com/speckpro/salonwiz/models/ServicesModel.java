package com.speckpro.salonwiz.models;

public class ServicesModel {

    String title, status, amount, duration;

    public ServicesModel(String title, String status, String amount, String duration) {
        this.title = title;
        this.status = status;
        this.amount = amount;
        this.duration = duration;
    }

    public ServicesModel(String title, String amount, String duration) {
        this.title = title;
        this.amount = amount;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
