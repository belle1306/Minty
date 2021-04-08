package com.example.mintycards.ui.card;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mintycards.Constants;
import com.example.mintycards.R;
import com.example.mintycards.network.model.CardMosaic;
import com.example.mintycards.ui.dialog.MintCardProgressDialog;
import com.example.mintycards.ui.dialog.SendMosaicDialog;
import com.example.mintycards.ui.nodes.CardNode;
import com.example.mintycards.ui.nodes.NodeRotationHelper;
import com.example.mintycards.util.SnackbarManager;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardActivity extends AppCompatActivity {

    private static final String TAG = "CardActivity";

    @BindView(R.id.sceneview)
    SceneView sceneView;

    @BindView(R.id.tv_card_name)
    TextView tvCardTitle;

    @BindView(R.id.tv_card_id)
    TextView tvCardId;

    @BindView(R.id.tv_card_description)
    TextView tvCardDescription;

    @BindView(R.id.btn_rotate)
    View btnRotate;

    private static final Vector3    startingCameraPosition = new Vector3(0f, 6f, 18f);
    private              CardNode   cardNode;
    private              Camera     camera;
    private              Vector3    currentCameraPosition;
    private              boolean    isRotatedSideways      = false;
    private              CardMosaic cardMosaic;
    private              String     textureUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.bind(this);

        Bundle b = getIntent().getBundleExtra(Constants.BUNDLE_CARD_MOSAIC_KEY);
        if (b != null) {
            cardMosaic = (CardMosaic) b.get(Constants.SERIALIZABLE_CARD_MOSAIC_KEY);
            if (cardMosaic != null) {
                Log.d(TAG, "onCreate: " + cardMosaic.getCard_title());
                tvCardTitle.setText(cardMosaic.getCard_title());
                String cardId = "Mosaic Id: " + cardMosaic.getCard_id();
                tvCardId.setText(cardId);
                textureUrl = cardMosaic.getCard_texture_ipfs_hash();
                tvCardDescription.setText(cardMosaic.getDescription());
            }

        }


        //Download image from ipfs and store inside app directory to load texture
        Glide.with(this)
                .asBitmap()
                .load(textureUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        checkStoragePermissions(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

    }

    //Need to display snackbar anchored to rotate btn. Doesn't work with parent viewgroup layout
    public void displaySuccessSnackbar(String msg){
        SnackbarManager.createAnchoredSnackbar(btnRotate, msg).show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initSceneView(String pathToTexture) {
        Scene scene = sceneView.getScene();
        cardNode = new CardNode(this, pathToTexture);

        scene.addChild(cardNode);
        NodeRotationHelper nodeRotationHelper = new NodeRotationHelper(this, cardNode);
        sceneView.setOnTouchListener(nodeRotationHelper);

        camera = scene.getCamera();
        camera.setWorldPosition(startingCameraPosition);
        currentCameraPosition = camera.getWorldPosition();
    }


    @Override
    public void onPause() {
        super.onPause();
        sceneView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            sceneView.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn_back)
    void onBackBtnClick() {
        Log.d(TAG, "onBackBtnClick: Business CardTest Activity back btn called");
        this.finish();
    }

    @OnClick(R.id.tv_card_id)
    void onMoreInfoClick(){
        Log.d(TAG, "onMoreInfoClick: " + cardMosaic.getDescription());
        tvCardDescription.setVisibility(View.VISIBLE);
        tvCardId.setCompoundDrawablesWithIntrinsicBounds(null, null, null ,null);
    }

    @OnClick(R.id.btn_rotate)
    void onRotateBtnClick() {
        //Rotate
        if (!isRotatedSideways) {
            isRotatedSideways = true;
            camera.setWorldRotation(Quaternion.axisAngle(new Vector3(0f, 0f, 1f), 90f));
        } else {
            isRotatedSideways = false;
            camera.setWorldRotation(Quaternion.axisAngle(startingCameraPosition, 0f));
        }

    }

    @OnClick(R.id.btn_send)
    void onBtnSendClick(){
        DialogFragment sendMosaicDialog = SendMosaicDialog.newInstance(cardMosaic.getCard_id());
        sendMosaicDialog.show(getSupportFragmentManager(), Constants.SEND_MOSAIC_DIALOG);
    }

    @OnClick(R.id.zoom_in)
    public void zoomIn() {
        currentCameraPosition.z -= 1.5;
        camera.setWorldPosition(currentCameraPosition);
    }

    @OnClick(R.id.zoom_out)
    public void zoomOut() {
        currentCameraPosition.z += 1.5;
        camera.setWorldPosition(currentCameraPosition);
    }

    private void checkStoragePermissions(Bitmap bitmap) {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Log.d(TAG, "onPermissionGranted: Granted.");
                        saveImage(bitmap, cardMosaic.getCard_id());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            Log.d(TAG, "onPermissionDenied: Permenant denied");
                        } else {
                            Log.d(TAG, "onPermissionDenied: Denied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void saveImage(Bitmap finalBitmap, String mosaicId) {
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/minty_app");
        if (!myDir.exists()) {
            Log.d(TAG, "createDir: Directory does not exist");
            boolean makeDir = myDir.mkdir();
            Log.d(TAG, "createDir: is successful making dir: " + makeDir);
        }

        File file = new File(myDir, mosaicId);

        if (file.exists()) {
            Log.d(TAG, "createDir: File already exists.");
            initSceneView(file.toString());

        } else {
            Log.d(TAG, "createDir: File does not exist.");
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                Log.d(TAG, "saveImage: Successfully written image.");
                initSceneView(file.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
