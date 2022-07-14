package com.speckpro.salonwiz.models;

import androidx.cardview.widget.CardView;

public class UtilityCardsModel {
    private CardView card;
    private Boolean isSelected;


    public Boolean getIsSelected(){
        return  isSelected;
    }

    private String drawable;
    private String text;


    public UtilityCardsModel(String drawable, String text) {
        this.drawable = drawable;
        this.text = text;
    }
    public UtilityCardsModel(String drawable, String text, Boolean isSelected) {
        this.drawable = drawable;
        this.text = text;
        this.isSelected=isSelected;
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
    public boolean getSelected() {
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
