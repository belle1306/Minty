package com.example.mintycards.ui.collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mintycards.Constants;
import com.example.mintycards.R;
import com.example.mintycards.network.model.CardMosaic;
import com.example.mintycards.network.model.CardTest;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardPreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SearchAdapter";

    static final int HEADER = 0;
    static final int GRID_ITEM = 1;


    private Context             context;
    private List<CardMosaic>      list;
    private CardPreviewCallback callback;


    public CardPreviewAdapter(Context context, List<CardMosaic> mList) {
        this.context = context;
        this.list = mList;

        try {
            callback = (CardPreviewCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback. " + e);
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == GRID_ITEM){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_card_preview, parent,false);
            return new GridHolder(view);
        }else{
            //Header
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.viewholder_header, parent,false);
            return new HeaderHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).isHeader()){
            return HEADER;
        }
        return GRID_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        if(holder.getItemViewType() == GRID_ITEM) {

            GridHolder gridHolder = (GridHolder) holder;

            String id = "Mosaic id: \n" + list.get(i).getCard_id();
            gridHolder.cardCost.setText(id);
            gridHolder.cardName.setText(list.get(i).getCard_title());

            Glide.with(context)
                    .load(list.get(i).getCard_preview_ipfs_hash())
                    .apply(RequestOptions.centerInsideTransform())
                    .into(gridHolder.cardPreview);
        }else{
            //Set number cards, etd
            HeaderHolder headerHolder = (HeaderHolder) holder;

            if(list.size() > 0){
                String numCards = list.size()-1 + " collectable cards";
                headerHolder.numCards.setText(numCards);
            }else{
                String numCards = "0 collectable cards";
                headerHolder.numCards.setText(numCards);
            }

        }


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GridHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_card_pic)
        ImageView cardPreview;

        @BindView(R.id.tv_card_name)
        TextView cardName;

        @BindView(R.id.tv_card_id)
        TextView cardCost;

        int position;

        GridHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            position = getAdapterPosition();
            callback.onCardClick(list.get(position));
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_num_cards)
        TextView numCards;

        private HeaderHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface CardPreviewCallback{
        void onCardClick(CardMosaic cardMosaic);
    }
}
