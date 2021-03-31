package com.example.mintycards.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mintycards.R;
import com.example.mintycards.network.Card;
import com.example.mintycards.util.GridAdapterItemDecoration;
import com.example.mintycards.util.GridAdapterSpacingUtils;
import com.example.mintycards.util.StreamUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionFragment extends Fragment {

    private static final String TAG = "CollectionFragment";

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private static final int GRID_VIEW_SPACING      = 8;
    private static final int GRID_VIEW_COLUMN_COUNT = 2;

    private Context            context;
    private CardPreviewAdapter cardAdapter;
    private List<Card>         listCards = new ArrayList<>();

    public static CollectionFragment newInstance() {
        return new CollectionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        cardAdapter = new CardPreviewAdapter(context, listCards);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        ButterKnife.bind(this, view);

        listCards.add(Card.createHeader());

        String jsonStr = StreamUtil.inputStreamToString(this.getResources().openRawResource(R.raw.sample_card_data));
        try {
            Gson gson = new Gson();
            Card[] name = gson.fromJson(jsonStr, Card[].class);
            listCards.addAll(Arrays.asList(name));
        } catch (Exception e) {
            Log.d(TAG, "onCreateView: Exception: " + e);
        }

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


        return view;
    }


}
