package com.example.mintycards.network.model;

public class AddMosaicMetadata {
    private String mosaic_id;
    private String key;
    private String card_title;
    private String description;
    private String card_preview_ipfs_hash;
    private String card_texture_ipfs_hash;

    public AddMosaicMetadata(String mosaic_id, String key, String card_title, String description,
                             String card_preview_ipfs_hash, String card_texture_ipfs_hash) {
        this.mosaic_id = mosaic_id;
        this.key = key;
        this.card_title = card_title;
        this.description = description;
        this.card_preview_ipfs_hash = card_preview_ipfs_hash;
        this.card_texture_ipfs_hash = card_texture_ipfs_hash;
    }

    public String getMosaic_id() {
        return mosaic_id;
    }

    public String getKey() {
        return key;
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
}
