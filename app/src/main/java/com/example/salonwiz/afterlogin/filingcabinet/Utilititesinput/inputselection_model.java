package com.example.salonwiz.afterlogin.filingcabinet.Utilititesinput;

import android.widget.CheckBox;

import androidx.cardview.widget.CardView;

public class inputselection_model {



    private Boolean isSelected;
    private int drawable;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String text;


    public inputselection_model(String url, String text,Boolean isSelected) {
        this.url= url;
        this.text = text;
        this.isSelected=isSelected;
    }



    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
    public boolean getSelected() {
        return isSelected;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
