package com.example.mintycards.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mintycards.Constants;
import com.example.mintycards.R;
import com.example.mintycards.network.model.TransferMosaic;
import com.example.mintycards.network.repo.MainRepo;
import com.example.mintycards.network.repo.TransferMosaicCallback;
import com.example.mintycards.ui.card.CardActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SendMosaicDialog extends DialogFragment implements TransferMosaicCallback {

    private static final String TAG = "SendMosaicDialog";

    @BindView(R.id.title)
    TextView tvDialogTitle;

    @BindView(R.id.layout_user_address)
    TextInputLayout layoutUserAddress;

    @BindView(R.id.et_user_address)
    TextInputEditText etUserAddress;

    @BindView(R.id.layout_sign_transaction)
    TextInputLayout layoutSignTransaction;

    @BindView(R.id.et_sign_transaction)
    TextInputEditText etSignTransaction;

    private Context context;
    private String  mosaicId;
    private MainRepo mainRepo;

    private SendMosaicDialog(String mosaicId) {
        this.mosaicId = mosaicId;
    }

    public static SendMosaicDialog newInstance(String mosaicId) {
        return new SendMosaicDialog(mosaicId);
    }

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
        mainRepo = new MainRepo(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_send_mosaic, container, false);
        ButterKnife.bind(this, view);

        String title = "Transfer Mosaic: " + mosaicId;
        tvDialogTitle.setText(title);

        layoutSignTransaction.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSignTransaction.setText(Constants.PRIVATE_KEY);
            }
        });

        return view;
    }

    @OnClick(R.id.btn_dismiss)
    void onBtnDismiss() {
        this.dismiss();
    }

    @OnClick(R.id.btn_send)
    void onSendBtnClick() {
        String sendToAddress = etUserAddress.getText().toString().trim();
        String privateKey = etSignTransaction.getText().toString().trim();

        if (sendToAddress.length() < 45) {
            layoutUserAddress.setError("Invalid address");
            return;
        }else{
            layoutUserAddress.setErrorEnabled(false);
        }

        if (privateKey.length() < 64) {
            layoutSignTransaction.setError("Invalid private key");
            return;
        }else{
            layoutSignTransaction.setErrorEnabled(false);
        }

        mainRepo.transferMosaic(new TransferMosaic(privateKey, sendToAddress, mosaicId));

    }


    @Override
    public void onSuccessTransferMosaic(int responseCode) {
        Log.d(TAG, "onSuccessTransferMosaic: Success Transfering mosaic. " + responseCode);
        CardActivity cardActivity = (CardActivity) context;
        if(responseCode == 200){
            cardActivity.displaySuccessSnackbar("Success Transferring mosaic!");
            this.dismiss();
        }else{
            cardActivity.displaySuccessSnackbar("Failed to transfer mosaic");
            this.dismiss();
        }

    }

    @Override
    public void onNetworkCallFailed() {
        Log.d(TAG, "onNetworkCallFailed: Network failed.");
    }
}
