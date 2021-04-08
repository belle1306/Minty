package com.example.mintycards.ui.nodes;

import android.content.Context;
import android.net.Uri;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

public class PreviewCardNode extends Node {

    private static final Vector3 NODE_STARTING_LOCAL_POSITION = new Vector3(0f, -3f, 0f);
    private static final String  NODE_ASSET_NAME              = "giftcard5.sfb";
    private              Context context;

    public PreviewCardNode(Context context) {
        this.context = context;
    }

    @Override
    public void onActivate() {
        super.onActivate();

        ModelRenderable.builder()
                .setSource(context, Uri.parse(NODE_ASSET_NAME))
                .build()
                .thenAccept(renderable -> {
                    this.setRenderable(renderable);
                    this.setWorldPosition(NODE_STARTING_LOCAL_POSITION);
                });

    }

}
