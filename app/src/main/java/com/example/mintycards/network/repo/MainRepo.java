package com.example.mintycards.network.repo;

import android.util.Log;

import com.example.mintycards.network.RetrofitClient;
import com.example.mintycards.network.model.AddMosaicMetadata;
import com.example.mintycards.network.model.CreateMosaicResponse;
import com.example.mintycards.network.model.Metadata;
import com.example.mintycards.network.model.TransferMosaic;
import com.example.mintycards.network.model.UploadImageResponse;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepo {

    private static final String TAG = "MainRepo";

    private MainService         mainService;
    private UploadImageCallback callback;
    private TransferMosaicCallback transferMosaicCallback;

    public MainRepo(UploadImageCallback callback) {
        this.callback = callback;
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        mainService = retrofitClient.getRetrofit().create(MainService.class);
    }

    public MainRepo(TransferMosaicCallback callback) {
        this.transferMosaicCallback = callback;
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        mainService = retrofitClient.getRetrofit().create(MainService.class);
    }

    public void uploadCardPreview(Metadata metadata, String imagePath) {
        mainService.uploadImage(metadata, createMultipartBody(imagePath)).enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(@NotNull Call<UploadImageResponse> call, @NotNull Response<UploadImageResponse> response) {
                callback.onSuccessUploadCardPreview(response.code(), response.body());
            }

            @Override
            public void onFailure(@NotNull Call<UploadImageResponse> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                callback.onNetworkCallFailed();
            }
        });
    }

    public void uploadCardTexture(Metadata metadata, String imagePath) {
        mainService.uploadImage(metadata, createMultipartBody(imagePath)).enqueue(new Callback<UploadImageResponse>() {
            @Override
            public void onResponse(@NotNull Call<UploadImageResponse> call, @NotNull Response<UploadImageResponse> response) {
                callback.onSuccessUploadTexture(response.code(), response.body());
            }

            @Override
            public void onFailure(@NotNull Call<UploadImageResponse> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: " + t);
                callback.onNetworkCallFailed();
            }
        });
    }

    public void createMosaic(Metadata metadata) {
        mainService.createMosaic(metadata).enqueue(new Callback<CreateMosaicResponse>() {
            @Override
            public void onResponse(@NotNull Call<CreateMosaicResponse> call, @NotNull Response<CreateMosaicResponse> response) {
                callback.onSuccessCreateMosaic(response.code(), response.body());
            }

            @Override
            public void onFailure(@NotNull Call<CreateMosaicResponse> call, @NotNull Throwable t) {
                callback.onNetworkCallFailed();
            }
        });
    }

    public void addMetadata(AddMosaicMetadata mosaicMetadata){
        mainService.addMetaData(mosaicMetadata).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                callback.onSuccessAddMetadata(response.code());
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                callback.onNetworkCallFailed();
            }
        });
    }

    public void transferMosaic(TransferMosaic transferMosaic){
        mainService.transferMosaic(transferMosaic).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                transferMosaicCallback.onSuccessTransferMosaic(response.code());
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                transferMosaicCallback.onNetworkCallFailed();
            }
        });
    }

    private MultipartBody.Part createMultipartBody(String path) {
        File file = new File(path);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // Avatar has to be the same on server as well
        // router.post('/', upload.single('avatar'), function (req, res, next)
        return MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
    }
}
