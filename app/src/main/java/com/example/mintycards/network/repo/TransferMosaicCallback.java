package com.example.mintycards.network.repo;

public interface TransferMosaicCallback {
    void onSuccessTransferMosaic(int responseCode);

    void onNetworkCallFailed();
}
