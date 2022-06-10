package com.speckpro.salonwiz.afterlogin.UI.homefrag_utilitydeals;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class allfrag_model  {
    private CardView card;
    private Boolean isSelected;
    private String drawable;
    private String text;
    private ArrayList<String> spinner;

    public ArrayList<String> getSpinner() {
        return spinner;
    }

    public void setSpinner(ArrayList<String> spinner) {
        this.spinner = spinner;
    }

    public allfrag_model(String drawable, String text,ArrayList<String> spinner) {
        this.drawable = drawable;
        this.text = text;
        this.spinner=spinner;
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
