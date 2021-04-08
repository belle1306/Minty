package com.example.mintycards.ui.nodes;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;

import java.util.concurrent.CompletableFuture;

public class CardNode extends Node {

    private static final String TAG = "CardNode";

    private static final Vector3    NODE_STARTING_LOCAL_POSITION = new Vector3(0f, -3f, 0f);
    private static final String     NODE_ASSET_NAME              = "giftcard5.sfb";
    private static final String     BASE_COLOR                   = "baseColor";
    private              Context    context;
    private              Texture    textureHolder;
    private              Renderable renderableHolder;
    private              String     devicePathToTexture;

    public CardNode(Context context, String devicePathToTexture) {
        this.context = context;
        this.devicePathToTexture = devicePathToTexture;
    }

    @Override
    public void onActivate() {
        super.onActivate();

        CompletableFuture<Texture> futureTexture = Texture.builder()
                .setSource(context, Uri.parse(devicePathToTexture))
                .build();

        ModelRenderable.builder()
                .setSource(context, Uri.parse(NODE_ASSET_NAME))
                .build()
                .thenAcceptBoth(futureTexture, (renderable, texture) -> {

                    this.renderableHolder = renderable;
                    this.textureHolder = texture;
                    this.setRenderable(renderable);
                    this.setWorldPosition(NODE_STARTING_LOCAL_POSITION);

                    renderableHolder.getMaterial().setTexture(BASE_COLOR, textureHolder);

                }).exceptionally(throwable -> {
            Log.d(TAG, "onActivate: " + throwable);
            return null;
        });

    }

}
