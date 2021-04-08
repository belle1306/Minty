package com.example.mintycards.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.mintycards.Constants;
import com.example.mintycards.R;
import com.example.mintycards.network.model.CardMosaic;
import com.example.mintycards.network.model.CardTest;
import com.example.mintycards.ui.card.CardActivity;
import com.example.mintycards.ui.collection.CardPreviewAdapter;
import com.google.android.material.tabs.TabLayout;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nem.symbol.sdk.api.AccountRepository;
import io.nem.symbol.sdk.api.MetadataPaginationStreamer;
import io.nem.symbol.sdk.api.MetadataRepository;
import io.nem.symbol.sdk.api.MetadataSearchCriteria;
import io.nem.symbol.sdk.api.RepositoryFactory;
import io.nem.symbol.sdk.api.TransactionRepository;
import io.nem.symbol.sdk.infrastructure.okhttp.JsonHelperGson;
import io.nem.symbol.sdk.infrastructure.okhttp.RepositoryFactoryOkHttpImpl;
import io.nem.symbol.sdk.model.account.Account;
import io.nem.symbol.sdk.model.account.AccountInfo;
import io.nem.symbol.sdk.model.account.Address;
import io.nem.symbol.sdk.model.account.UnresolvedAddress;
import io.nem.symbol.sdk.model.blockchain.BlockDuration;
import io.nem.symbol.sdk.model.message.PlainMessage;
import io.nem.symbol.sdk.model.metadata.Metadata;
import io.nem.symbol.sdk.model.mosaic.Mosaic;
import io.nem.symbol.sdk.model.mosaic.MosaicFlags;
import io.nem.symbol.sdk.model.mosaic.MosaicId;
import io.nem.symbol.sdk.model.mosaic.MosaicNonce;
import io.nem.symbol.sdk.model.mosaic.MosaicSupplyChangeActionType;
import io.nem.symbol.sdk.model.network.NetworkType;
import io.nem.symbol.sdk.model.transaction.AggregateTransaction;
import io.nem.symbol.sdk.model.transaction.AggregateTransactionFactory;
import io.nem.symbol.sdk.model.transaction.Deadline;
import io.nem.symbol.sdk.model.transaction.JsonHelper;
import io.nem.symbol.sdk.model.transaction.MosaicDefinitionTransaction;
import io.nem.symbol.sdk.model.transaction.MosaicDefinitionTransactionFactory;
import io.nem.symbol.sdk.model.transaction.MosaicSupplyChangeTransaction;
import io.nem.symbol.sdk.model.transaction.MosaicSupplyChangeTransactionFactory;
import io.nem.symbol.sdk.model.transaction.SignedTransaction;
import io.nem.symbol.sdk.model.transaction.TransferTransaction;
import io.nem.symbol.sdk.model.transaction.TransferTransactionFactory;

public class MainActivity extends AppCompatActivity
        implements CardPreviewAdapter.CardPreviewCallback {

    private static final String TAG = "MainActivity";

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.btn_wallet)
    ImageButton btnWallet;

    private MainPagerAdapter pagerAdapter;
    private boolean isBtnSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupBouncyCastle();

        initPagerAdapter();
    }

    public void initPagerAdapter(){
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.btn_wallet)
    void onWalletBtnClick(){
        if(isBtnSelected){
            btnWallet.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_green_plus_wallet));
            isBtnSelected = false;

        }else{
            btnWallet.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_white_wallet));
            isBtnSelected = true;
        }

    }

    private void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }
    @Override
    public void onCardClick(CardMosaic cardMosaic) {
        Intent i = new Intent(this, CardActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(Constants.SERIALIZABLE_CARD_MOSAIC_KEY, cardMosaic);
        i.putExtra(Constants.BUNDLE_CARD_MOSAIC_KEY, args);
        startActivity(i);
    }
}
