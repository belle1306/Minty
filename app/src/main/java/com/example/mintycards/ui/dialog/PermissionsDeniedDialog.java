package com.example.mintycards.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mintycards.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PermissionsDeniedDialog extends AlertDialog {


    @BindView(R.id.imageview)
    ImageView imageView;

    @BindView(R.id.tv_ipfs_preview)
    TextView tvDeniedText;

    private String  deniedText;
    private Context context;

    public PermissionsDeniedDialog(@NonNull Context context, String s) {
        super(context);
        this.context = context;
        this.deniedText = s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_denied_permissions);
        ButterKnife.bind(this);

        Glide.with(context)
                .load(R.drawable.permissions)
                .apply(RequestOptions.fitCenterTransform())
                .into(imageView);

        tvDeniedText.setText(deniedText);

    }

    @OnClick(R.id.btn_dismiss)
    void onBtnDismiss() {
        this.dismiss();
    }


}
