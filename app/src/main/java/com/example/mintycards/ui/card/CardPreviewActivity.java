package com.example.mintycards.ui.card;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mintycards.R;
import com.example.mintycards.ui.nodes.CardNode;
import com.example.mintycards.ui.nodes.NodeRotationHelper;
import com.example.mintycards.ui.nodes.PreviewCardNode;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardPreviewActivity extends AppCompatActivity {

    private static final String TAG = "CardActivity";

    @BindView(R.id.sceneview)
    SceneView sceneView;

    private static final Vector3         startingCameraPosition = new Vector3(0f, 6f, 18f);
    private              PreviewCardNode cardNode;
    private              Camera          camera;
    private              Vector3         currentCameraPosition;
    private              boolean  isRotatedSideways = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_preview);
        ButterKnife.bind(this);

        initSceneView();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initSceneView() {
        Scene scene = sceneView.getScene();
        cardNode = new PreviewCardNode(this);

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

    @OnClick(R.id.btn_rotate)
    void onRotateBtnClick() {
        //rotate
        if(!isRotatedSideways){
            isRotatedSideways = true;
            camera.setWorldRotation(Quaternion.axisAngle(new Vector3(0f, 0f, 1f), 90f));
        }else{
            isRotatedSideways = false;
            camera.setWorldRotation(Quaternion.axisAngle(startingCameraPosition, 0f));
        }
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

}
