package com.example.mintycards.ui.collection;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mintycards.Constants;
import com.example.mintycards.R;
import com.example.mintycards.network.model.CardMosaic;
import com.example.mintycards.network.model.CardTest;
import com.example.mintycards.ui.MainActivity;
import com.example.mintycards.util.GridAdapterItemDecoration;
import com.example.mintycards.util.GridAdapterSpacingUtils;
import com.example.mintycards.util.StreamUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.nem.symbol.sdk.api.MetadataPaginationStreamer;
import io.nem.symbol.sdk.api.MetadataRepository;
import io.nem.symbol.sdk.api.MetadataSearchCriteria;
import io.nem.symbol.sdk.api.RepositoryFactory;
import io.nem.symbol.sdk.infrastructure.okhttp.JsonHelperGson;
import io.nem.symbol.sdk.infrastructure.okhttp.RepositoryFactoryOkHttpImpl;
import io.nem.symbol.sdk.model.account.Address;
import io.nem.symbol.sdk.model.metadata.Metadata;
import io.nem.symbol.sdk.model.transaction.JsonHelper;

public class CollectionFragment extends Fragment {

    private static final String TAG = "CollectionFragment";

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private static final int GRID_VIEW_SPACING      = 8;
    private static final int GRID_VIEW_COLUMN_COUNT = 2;

    private Context            context;
    private CardPreviewAdapter cardAdapter;
    private List<CardMosaic>     listCardTests = new ArrayList<>();

    public static CollectionFragment newInstance() {
        Log.d(TAG, "newInstance: Creating new collection fragment");
        return new CollectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        cardAdapter = new CardPreviewAdapter(context, listCardTests);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        ButterKnife.bind(this, view);

        listCardTests.add(CardMosaic.createHeader());

        //String jsonStr = StreamUtil.inputStreamToString(this.getResources().openRawResource(R.raw.sample_card_data));
        //try {
        //    Gson gson = new Gson();
        //    CardTest[] name = gson.fromJson(jsonStr, CardTest[].class);
        //    listCardTests.addAll(Arrays.asList(name));
        //} catch (Exception e) {
        //    Log.d(TAG, "onCreateView: Exception: " + e);
        //}

        int spacing = GridAdapterSpacingUtils.convertIntToDP(context, GRID_VIEW_SPACING);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, GRID_VIEW_COLUMN_COUNT);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (cardAdapter.getItemViewType(position)) {
                    case CardPreviewAdapter.HEADER:
                        return GRID_VIEW_COLUMN_COUNT;

                    case CardPreviewAdapter.GRID_ITEM:
                        return 1; //number of columns of the grid

                    default:
                        return -1;
                }

            }
        });

        recyclerView.addItemDecoration(new GridAdapterItemDecoration(GRID_VIEW_COLUMN_COUNT, spacing, true));
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(cardAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh: Called.");
                listCardTests.clear();
                listCardTests.add(CardMosaic.createHeader());
                getUserCollection();
            }
        });

        getUserCollection();

        return view;
    }

    private void getUserCollection(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getMetadataAssociatedWithAccount();
            }
        });
        thread.start();
    }

    //Need to call setup with bouncycastle in mainactivity otherwise call will not work.
    private void getMetadataAssociatedWithAccount(){
        // replace with node endpoint
        try (final RepositoryFactory repositoryFactory = new RepositoryFactoryOkHttpImpl(
                "http://api-01.us-east-1.testnet.symboldev.network:3000")) {
            final MetadataRepository metadataRepository = repositoryFactory
                    .createMetadataRepository();

            // replace with address
            final String rawAddress = Constants.RAW_ADDRESS;
            final Address address = Address.createFromRawAddress(rawAddress);

            MetadataPaginationStreamer streamer = new MetadataPaginationStreamer(
                    metadataRepository);
            MetadataSearchCriteria criteria = new MetadataSearchCriteria().targetAddress(address);

            final List<Metadata> list = streamer
                    .search(criteria).toList().toFuture()
                    .get();


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    loadCardsIntoRecycler(list);
                }
            });


            JsonHelper helper = new JsonHelperGson();
            StreamUtil.printLongString(TAG, helper.prettyPrint(list));
        }catch (Exception e){
            Log.d(TAG, "getMetadataAssociatedWithAccount: Error: " + e);
        }
    }

    private void loadCardsIntoRecycler(List<Metadata> list){
        swipeRefreshLayout.setRefreshing(false);
        List<CardMosaic> myCardsList = new ArrayList<>();
        Gson gson = new Gson();
        for(int i = 0; i < list.size(); i++){
            try {
                CardMosaic mosaic = gson.fromJson(list.get(i).getValue(), CardMosaic.class);
                //Search through metadata to make sure mosaic is a minty card.
                if(mosaic.isMinty_card()){
                    String imagePreviewUrl = Constants.PINATA_BASE_URL + mosaic.getCard_preview_ipfs_hash();
                    String imageTextureUrl = Constants.PINATA_BASE_URL + mosaic.getCard_texture_ipfs_hash();
                    mosaic.setCard_preview_ipfs_hash(imagePreviewUrl);
                    mosaic.setCard_texture_ipfs_hash(imageTextureUrl);

                    myCardsList.add(mosaic);
                }

            }catch (Exception e){
                Log.d(TAG, "Invalid Minty json structure: " + e);
            }
        }

        Log.d(TAG, "loadCardsIntoRecycler: New list size: " + listCardTests.size());

        listCardTests.addAll(myCardsList);
        cardAdapter.notifyDataSetChanged();
    }


}
