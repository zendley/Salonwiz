package com.speckpro.salonwiz.afterlogin.UI.homefrag_utilitydeals;

public class model_getdeals {
    private String title;
    private String description;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public model_getdeals(String title, String description, String id) {
        this.title = title;
        this.description = description;
        this.id = id;
    }
    public model_getdeals(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
