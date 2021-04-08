package com.example.mintycards.network.model;

public class CardTest {
    private boolean isHeader;
    private String card_preview;
    private String card_price;
    private String card_name;

    private CardTest(boolean isHeader){
        this.isHeader = isHeader;
    }

    public static CardTest createHeader(){
        return new CardTest(true);
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
