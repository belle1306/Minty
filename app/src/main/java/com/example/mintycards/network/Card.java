package com.example.mintycards.network;

public class Card {
    private boolean isHeader;
    private String card_preview;
    private String card_price;
    private String card_name;

    private Card(boolean isHeader){
        this.isHeader = isHeader;
    }

    public static Card createHeader(){
        return new Card(true);
    }

    public boolean isHeader() {
        return isHeader;
    }

    public String getCard_preview() {
        return card_preview;
    }

    public String getCard_price() {
        return card_price;
    }

    public String getCard_name() {
        return card_name;
    }
}
