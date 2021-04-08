package com.example.mintycards.ui.mintcard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mintycards.Constants;
import com.example.mintycards.R;
import com.example.mintycards.network.model.AddMosaicMetadata;
import com.example.mintycards.network.model.CreateMosaicResponse;
import com.example.mintycards.network.model.Metadata;
import com.example.mintycards.network.model.UploadImageResponse;
import com.example.mintycards.network.repo.MainRepo;
import com.example.mintycards.network.repo.UploadImageCallback;
import com.example.mintycards.ui.card.CardPreviewActivity;
import com.example.mintycards.ui.dialog.MintCardProgressDialog;
import com.example.mintycards.ui.dialog.PermissionsDeniedDialog;
import com.example.mintycards.util.ClipboardUtil;
import com.example.mintycards.util.ReadStorageManager;
import com.example.mintycards.util.SnackbarManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.zhihu.matisse.Matisse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MintCardFragment extends Fragment implements UploadImageCallback {

    private static final String TAG = "MintCardFragment";

    @BindView(R.id.iv_template)
    ImageView ivTemplate;

    @BindView(R.id.iv_preview)
    ImageView ivCardPreview;

    @BindView(R.id.iv_texture)
    ImageView ivCardTexture;

    @BindView(R.id.parent_layout)
    ViewGroup parentLayout;

    @BindView(R.id.et_card_title)
    TextInputEditText etCardTitle;

    @BindView(R.id.et_card_description)
    TextInputEditText etCardDescription;

    @BindView(R.id.et_sign_transaction)
    TextInputEditText etSignTransaction;

    @BindView(R.id.layout_card_title)
    TextInputLayout tilCardTitle;

    @BindView(R.id.layout_card_description)
    TextInputLayout tilCardDescription;

    @BindView(R.id.layout_card_sign)
    TextInputLayout tilSignTransaction;

    @BindView(R.id.et_card_date)
    TextInputEditText etCardDate;

    private Context  context;
    private String   selectedPreviewImagePath = "";
    private String   selectedTextureImagePath = "";
    private MainRepo mainRepo;
    private String   cardTitle, cardDescription, key, previewHash, textureHash;

    public static MintCardFragment newInstance() {
        Log.d(TAG, "newInstance: Creating new Mint Card Fragment");
        return new MintCardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        mainRepo = new MainRepo(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mintcard, container, false);
        ButterKnife.bind(this, view);


        Glide.with(this)
                .load(Constants.TEMPLATE_URL)
                .apply(RequestOptions.centerCropTransform())
                .into(ivTemplate);

        Glide.with(this)
                .load(R.drawable.viewholder_portrait)
                .apply(RequestOptions.centerCropTransform())
                .into(ivCardPreview);

        Glide.with(this)
                .load(R.drawable.viewholder_portrait)
                .apply(RequestOptions.centerCropTransform())
                .into(ivCardTexture);

        tilSignTransaction.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked feather icon.");
                //Query shared preferences and get key
                etSignTransaction.setText(Constants.PRIVATE_KEY);
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN, Locale.US);
        String date = dateFormat.format(Calendar.getInstance().getTime());
        etCardDate.setText(date);


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        switch (requestCode) {
            case Constants.SELECT_CARD_PREVIEW_IMG:
                //Returns array
                selectedPreviewImagePath = Matisse.obtainPathResult(data).get(0);
                Glide.with(MintCardFragment.this)
                        .load(Matisse.obtainPathResult(data).get(0))
                        .apply(RequestOptions.centerCropTransform())
                        .into(ivCardPreview);
                break;

            case Constants.SELECT_CARD_TEXTURE_IMG:
                selectedTextureImagePath = Matisse.obtainPathResult(data).get(0);
                Glide.with(MintCardFragment.this)
                        .load(Matisse.obtainPathResult(data).get(0))
                        .apply(RequestOptions.centerCropTransform())
                        .into(ivCardTexture);
                break;

        }
    }

    @OnClick(R.id.btn_download)
    void onDownloadClick() {
        ClipboardUtil.copyText(context, Constants.TEMPLATE_URL);

        SnackbarManager.downloadTemplate(context, parentLayout).show();
    }

    @OnClick(R.id.btn_preview)
    void onPreviewBtnClick() {
        Intent i = new Intent(context, CardPreviewActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.iv_preview)
    void onPreviewClick() {
        checkStoragePermissions(Constants.SELECT_CARD_PREVIEW_IMG);
    }

    @OnClick(R.id.iv_texture)
    void onTextureClick() {
        checkStoragePermissions(Constants.SELECT_CARD_TEXTURE_IMG);
    }

    @OnClick(R.id.btn_mint_card)
    void onMintCardBtnClick() {
        cardTitle = etCardTitle.getText().toString().trim();
        cardDescription = etCardDescription.getText().toString().trim();
        key = etSignTransaction.getText().toString().trim();

        if (cardTitle.equals("")) {
            createSnackbar("Card title cannot be empty.");
            return;
        }

        if (cardDescription.equals("")) {
            createSnackbar("Card description cannot be empty.");
            return;
        }

        if (selectedPreviewImagePath.equals("")) {
            createSnackbar("Select a preview image for card.");
            return;
        }

        if (selectedTextureImagePath.equals("")) {
            createSnackbar("Select a texture for card.");
            return;
        }

        if (key.length() < 64) {
            createSnackbar("Invalid private key.");
            return;
        }


        DialogFragment incomingTransactionDialog = new MintCardProgressDialog();
        incomingTransactionDialog.show(getParentFragmentManager(), Constants.MINTCARD_FRAGMENT_DIALOG);

        mainRepo.uploadCardPreview(
                new Metadata(key, cardTitle, cardDescription),
                selectedPreviewImagePath
        );
    }

    private void createSnackbar(String msg) {
        SnackbarManager.createSnackbar(context, msg, parentLayout).show();
    }

    private void checkStoragePermissions(int requestCode) {
        Dexter.withContext(context)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        ReadStorageManager.selectPhoto(MintCardFragment.this, requestCode);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            onPermissionsPermenantlyDenied(response.getPermissionName());
                        } else {
                            SnackbarManager.retryStoragePermissions(context, parentLayout).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void onPermissionsPermenantlyDenied(String s) {
        AlertDialog incomingTransactionDialog = new PermissionsDeniedDialog(context, s);
        incomingTransactionDialog.show();
    }

    private MintCardProgressDialog findProgressDialog() {
        return (MintCardProgressDialog)
                getParentFragmentManager().findFragmentByTag(Constants.MINTCARD_FRAGMENT_DIALOG);
    }

    @Override
    public void onSuccessUploadCardPreview(int responseCode, UploadImageResponse response) {
        Log.d(TAG, "onSuccessUploadCardPreview: " + response.getIpfsHash());
        if (findProgressDialog() != null) {
            if (responseCode == 200) {
                findProgressDialog().displayCheckmarkOne();
                findProgressDialog().addSuccessUploadPreviewMsg(response.getIpfsHash());
                //Make Upload texture to IPFS now
                previewHash = response.getIpfsHash();
                mainRepo.uploadCardTexture(
                        new Metadata(key, cardTitle, cardDescription),
                        selectedTextureImagePath
                );
            } else {
                findProgressDialog().hideProgressBar();
                findProgressDialog().displayFailedIconOne();
                findProgressDialog().displayFailedMsg();
            }
        }
    }

    @Override
    public void onSuccessUploadTexture(int responseCode, UploadImageResponse response) {
        Log.d(TAG, "onSuccessUploadTexture: " + response.getIpfsHash());
        if (findProgressDialog() != null) {
            if (responseCode == 200) {
                findProgressDialog().displayCheckmarkTwo();
                findProgressDialog().addSuccessUploadTextureMsg(response.getIpfsHash());
                //Create Mosaic now
                textureHash = response.getIpfsHash();
                Log.d(TAG, "onSuccessUploadTexture: Creating mosaic...");
                mainRepo.createMosaic(new Metadata(key, cardTitle, cardDescription));

            } else {
                findProgressDialog().hideProgressBar();
                findProgressDialog().displayFailedIconTwo();
                findProgressDialog().displayFailedMsg();
            }
        }
    }

    @Override
    public void onSuccessCreateMosaic(int responseCode, CreateMosaicResponse response) {
        Log.d(TAG, "onSuccessCreateMosaic: " + response.getId());
        if (findProgressDialog() != null) {
            if (responseCode == 200) {
                findProgressDialog().displayCheckmarkThree();
                findProgressDialog().addSuccessCreateMosaicMsg(response.getId());
                //Add metadata to mosaic
                Log.d(TAG, "onSuccessCreateMosaic: Adding metadata to mosaic...");
                AddMosaicMetadata mosaicMetadata = new AddMosaicMetadata(
                        response.getId(),
                        key,
                        cardTitle,
                        cardDescription,
                        previewHash,
                        textureHash
                );
                mainRepo.addMetadata(mosaicMetadata);

            } else {
                findProgressDialog().hideProgressBar();
                findProgressDialog().displayFailedIconThree();
                findProgressDialog().displayFailedMsg();
            }
        }
    }

    @Override
    public void onSuccessAddMetadata(int responseCode) {
        if (findProgressDialog() != null) {
            if (responseCode == 200) {
                findProgressDialog().hideProgressBar();
                findProgressDialog().displayCheckmarkFour();
                findProgressDialog().displaySuccessMsg();
                Log.d(TAG, "onSuccessAddMetadata: Success adding meta data");

            } else {
                findProgressDialog().hideProgressBar();
                findProgressDialog().displayFailedIconFour();
                findProgressDialog().displayFailedMsg();
            }
        }
    }


    @Override
    public void onNetworkCallFailed() {
        SnackbarManager.createSnackbar(context, "Whoops something went wrong.", parentLayout).show();
    }
}
