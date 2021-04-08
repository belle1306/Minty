package com.example.mintycards.ui.nodes;

import androidx.dynamicanimation.animation.FloatPropertyCompat;

import com.google.ar.sceneform.Node;

public abstract class CardProperty extends FloatPropertyCompat<Node> {
    public CardProperty(String name) {
        super(name);
    }
}
