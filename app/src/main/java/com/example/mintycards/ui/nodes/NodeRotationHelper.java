package com.example.mintycards.ui.nodes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.dynamicanimation.animation.FlingAnimation;

import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;

public class NodeRotationHelper implements View.OnTouchListener {

    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final float CARD_ROTATION_FRICTION = 1.1f;

    private final GestureDetector mGestureDetector;
    private final FlingAnimation  mFlingAnimation;
    private final float           mScreenDensity;
    private final Node            mNode;

    private Vector3 mAxisToRotateAround = new Vector3(0f, 1f, 0f);
    private float   mCurrentRotatedAngle;

    public NodeRotationHelper(Context context, Node mNode) {
        this.mGestureDetector = new GestureDetector(context, new FlingGestureDetector());
        this.mNode = mNode;

        mScreenDensity = context.getResources().getDisplayMetrics().density;
        mFlingAnimation = new FlingAnimation(mNode, rotationProperty);

    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }


    private class FlingGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float deltaX = -(distanceX / mScreenDensity) / CARD_ROTATION_FRICTION;
            mNode.setLocalRotation(
                    getRotationQuaternion(mCurrentRotatedAngle + deltaX)
            );

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                float deltaVelocity = (velocityX / mScreenDensity) / CARD_ROTATION_FRICTION;
                startAnimation(deltaVelocity);
            }

            return true;
        }
    }


    private Quaternion getRotationQuaternion(float deltaYAxisAngle) {
        mCurrentRotatedAngle = deltaYAxisAngle;
        Quaternion quaternion = new Quaternion();
        double arc = Math.toRadians(deltaYAxisAngle);
        double axis = Math.sin(arc / 2);

        quaternion.x = (float) (mAxisToRotateAround.x * axis);
        quaternion.y = (float) (mAxisToRotateAround.y * axis);
        quaternion.z = (float) (mAxisToRotateAround.z * axis);

        quaternion.w = (float) Math.cos(arc / 2.0);

        quaternion.normalize();
        return quaternion;
    }

    private void startAnimation(float velocity) {
        if (!mFlingAnimation.isRunning()) {
            mFlingAnimation.setStartVelocity(velocity);
            mFlingAnimation.setStartValue(mCurrentRotatedAngle);
            mFlingAnimation.start();
        }
    }

    private CardProperty rotationProperty = new CardProperty("rotation") {
        @Override
        public float getValue(Node card) {
            return card.getLocalRotation().y;
        }

        @Override
        public void setValue(Node card, float value) {
            card.setLocalRotation(getRotationQuaternion(value));
        }
    };
}