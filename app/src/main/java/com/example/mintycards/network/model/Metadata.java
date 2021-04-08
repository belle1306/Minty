package com.example.mintycards.network.model;

public class Metadata {
    private String card_title;
    private String description;
    private String key;

    public Metadata(String key, String card_title, String description) {
        this.key = key;
        this.card_title = card_title;
        this.description = description;
    }

    public String getCard_title() {
        return card_title;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "card_title='" + card_title + '\'' +
                ", description='" + description + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
