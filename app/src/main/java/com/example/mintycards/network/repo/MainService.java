package com.example.mintycards.network.repo;

import com.example.mintycards.network.model.AddMosaicMetadata;
import com.example.mintycards.network.model.CreateMosaicResponse;
import com.example.mintycards.network.model.Metadata;
import com.example.mintycards.network.model.TransferMosaic;
import com.example.mintycards.network.model.UploadImageResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MainService {

    @Multipart
    @POST("upload")
    Call<UploadImageResponse> uploadImage(
            @Part("card_data") Metadata body,
            @Part MultipartBody.Part image
    );

    @POST("create-mosaic")
    Call<CreateMosaicResponse> createMosaic(@Body Metadata metadata);

    @POST("add-metadata")
    Call<Void> addMetaData(@Body AddMosaicMetadata mosaicMetadata);

    @POST("transfer-mosaic")
    Call<Void> transferMosaic(@Body TransferMosaic transferMosaic);
}
