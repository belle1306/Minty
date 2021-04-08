package com.example.mintycards.network.model;

public class UploadImageResponse {
    private String IpfsHash;
    private int PinSize;
    private String Timestamp;

    public String getIpfsHash() {
        return IpfsHash;
    }

    public int getPinSize() {
        return PinSize;
    }

    public String getTimestamp() {
        return Timestamp;
    }
}
