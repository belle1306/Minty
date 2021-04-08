package com.example.mintycards.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.example.mintycards.Constants;
import com.example.mintycards.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackbarManager {
    public static Snackbar downloadTemplate(Context context, ViewGroup parentLayout){
        String msg = "Download template\n" + Constants.TEMPLATE_URL;
        return Snackbar.make(parentLayout, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(context.getResources().getColor(R.color.colorAccent));
    }

    public static Snackbar retryStoragePermissions(Context context, ViewGroup parentLayout){
        String msg = "Read storage permissions must be enabled to select from photos.";
        return Snackbar.make(parentLayout, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(context.getResources().getColor(R.color.colorAccent));
    }

    public static Snackbar createSnackbar(Context context, String msg, ViewGroup parentLayout){
        return Snackbar.make(parentLayout, msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setActionTextColor(context.getResources().getColor(R.color.colorAccent));
    }

    public static Snackbar createAnchoredSnackbar(View anchor, String msg){
        Snackbar snackbar = Snackbar.make(anchor, msg, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snackbar.setAnchorView(anchor);
        return snackbar;
    }
}
