package com.speckpro.salonwiz.afterlogin.user;

public class SocialModel {

    String email, title, url;

    public SocialModel(String email, String title, String url) {
        this.email = email;
        this.title = title;
        this.url = url;
    }

    public SocialModel(String email, String title) {
        this.email = email;
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
