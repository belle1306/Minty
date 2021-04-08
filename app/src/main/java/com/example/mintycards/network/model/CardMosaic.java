package com.example.mintycards.network.model;

import java.io.Serializable;

public class CardMosaic implements Serializable {
    private boolean isHeader;
    private boolean minty_card;
    private String  card_title;
    private String  description;
    private String  card_preview_ipfs_hash;
    private String  card_texture_ipfs_hash;
    private String  card_id;

    private CardMosaic(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public static CardMosaic createHeader() {
        return new CardMosaic(true);
    }

    public boolean isHeader() {
        return isHeader;
    }

    public boolean isMinty_card() {
        return minty_card;
    }

    public String getCard_title() {
        return card_title;
    }

    public String getDescription() {
        return description;
    }

    public String getCard_preview_ipfs_hash() {
        return card_preview_ipfs_hash;
    }

    public String getCard_texture_ipfs_hash() {
        return card_texture_ipfs_hash;
    }

    public void setCard_preview_ipfs_hash(String card_preview_ipfs_hash) {
        this.card_preview_ipfs_hash = card_preview_ipfs_hash;
    }

    public void setCard_texture_ipfs_hash(String card_texture_ipfs_hash) {
        this.card_texture_ipfs_hash = card_texture_ipfs_hash;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    @Override
    public String toString() {
        return "CardMosaic{" +
                "minty_card=" + minty_card +
                ", card_title='" + card_title + '\'' +
                ", description='" + description + '\'' +
                ", card_preview_ipfs_hash='" + card_preview_ipfs_hash + '\'' +
                ", card_texture_ipfs_hash='" + card_texture_ipfs_hash + '\'' +
                '}';
    }
}
