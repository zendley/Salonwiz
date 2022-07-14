package com.speckpro.salonwiz.models;

import androidx.cardview.widget.CardView;

public class UtilityModel {
    private CardView card;
    private String drawable;
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public UtilityModel(String drawable, String text, String id) {
        this.drawable = drawable;
        this.text = text;
        this.id=id;
    }

    public CardView getCard() {
        return card;
    }

    public void setCard(CardView card) {
        this.card = card;
    }

    public String getDrawable() {
        return drawable;
    }

    public void setDrawable(String drawable) {
        this.drawable = drawable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

