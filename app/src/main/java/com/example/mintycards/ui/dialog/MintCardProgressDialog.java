package com.example.mintycards.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.mintycards.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MintCardProgressDialog extends DialogFragment {

    @BindView(R.id.tv_ipfs_preview)
    TextView tvCardPreview;

    @BindView(R.id.tv_ipfs_texture)
    TextView tvCardTexture;

    @BindView(R.id.tv_creating_mosaic)
    TextView tvCreatingMosaic;

    @BindView(R.id.checkmark_1)
    ImageView checkmarkOne;

    @BindView(R.id.checkmark_2)
    ImageView checkmarkTwo;

    @BindView(R.id.checkmark_3)
    ImageView checkmarkThree;

    @BindView(R.id.checkmark_4)
    ImageView checkmarkFour;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.tv_success_msg)
    TextView tvSuccessMsg;

    private Context context;


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_mintcard_progress, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @OnClick(R.id.btn_dismiss)
    void onBtnDismiss() {
        this.dismiss();
    }

    public void displayCheckmarkOne() {
        checkmarkOne.setVisibility(View.VISIBLE);
    }

    public void displayCheckmarkTwo() {
        checkmarkTwo.setVisibility(View.VISIBLE);
    }

    public void displayCheckmarkThree() {
        checkmarkThree.setVisibility(View.VISIBLE);
    }

    public void displayCheckmarkFour() {
        checkmarkFour.setVisibility(View.VISIBLE);
    }

    //Failed

    public void displayFailedIconOne() {
        checkmarkOne.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_red_cancel));
        checkmarkOne.setVisibility(View.VISIBLE);
    }

    public void displayFailedIconTwo() {
        checkmarkTwo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_red_cancel));
        checkmarkTwo.setVisibility(View.VISIBLE);
    }

    public void displayFailedIconThree() {
        checkmarkThree.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_red_cancel));
        checkmarkThree.setVisibility(View.VISIBLE);
    }

    public void displayFailedIconFour() {
        checkmarkFour.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_red_cancel));
        checkmarkFour.setVisibility(View.VISIBLE);
    }

    //Change text

    public void addSuccessUploadPreviewMsg(String hash) {
        tvCardPreview.setText(context.getString(R.string.card_preview) + "\n\nHash: " + hash);
    }

    public void addSuccessUploadTextureMsg(String hash) {
        tvCardTexture.setText(context.getString(R.string.card_texture) + "\n\nHash: " + hash);
    }

    public void addSuccessCreateMosaicMsg(String mosaicdId) {
        tvCreatingMosaic.setText(context.getString(R.string.card_mosaic) + "\n\nMosaic Id: " + mosaicdId);
    }

    //Progress bar

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void displaySuccessMsg() {
        tvSuccessMsg.setVisibility(View.VISIBLE);
    }

    public void displayFailedMsg() {
        tvSuccessMsg.setVisibility(View.VISIBLE);
        tvSuccessMsg.setText("Failed to mint card.");
        tvSuccessMsg.setTextColor(ContextCompat.getColor(context, R.color.triRedColor));
    }

}
