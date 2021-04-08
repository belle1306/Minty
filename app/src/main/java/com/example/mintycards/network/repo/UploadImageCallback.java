package com.example.mintycards.network.repo;

import com.example.mintycards.network.model.CreateMosaicResponse;
import com.example.mintycards.network.model.UploadImageResponse;

public interface UploadImageCallback{
    void onSuccessUploadCardPreview(int responseCode, UploadImageResponse response);
    void onSuccessUploadTexture(int responseCode, UploadImageResponse response);
    void onSuccessCreateMosaic(int responseCode, CreateMosaicResponse response);
    void onSuccessAddMetadata(int responseCode);
    void onNetworkCallFailed();
}
