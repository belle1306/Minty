package com.example.mintycards.network.model;

public class TransferMosaic {
    private String key;
    private String address;
    private String mosaic_id;

    public TransferMosaic(String key, String address, String mosaic_id) {
        this.key = key;
        this.address = address;
        this.mosaic_id = mosaic_id;
    }

    public String getKey() {
        return key;
    }

    public String getAddress() {
        return address;
    }

    public String getMosaic_id() {
        return mosaic_id;
    }

    @Override
    public String toString() {
        return "TransferMosaic{" +
                "key='" + key + '\'' +
                ", address='" + address + '\'' +
                ", mosaic_id='" + mosaic_id + '\'' +
                '}';
    }
}
