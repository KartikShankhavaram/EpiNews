package com.kartik.newsreader.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kartik.newsreader.R;
import com.kartik.newsreader.service.FontService;

/**
 * Created by kartik on 7/11/17.
 */

public class NewsViewHolder extends RecyclerView.ViewHolder {

    private TextView titleView;
    private TextView authorView;
    private RelativeLayout relativeLayout;
    private TextView desc;
    private CardView cardView;

    public NewsViewHolder(View v) {
        super(v);

        titleView = v.findViewById(R.id.title);
        authorView = v.findViewById(R.id.author);
        relativeLayout = v.findViewById(R.id.card_relative_layout);
        desc = v.findViewById(R.id.description);
        cardView = v.findViewById(R.id.card_view);

        titleView.setTypeface(FontService.getProductSans(v.getContext()));
        authorView.setTypeface(FontService.getProductSans(v.getContext()));
        desc.setTypeface(FontService.getProductSans(v.getContext()));

        titleView.setBackgroundResource(R.color.trans);
        authorView.setBackgroundResource(R.color.trans);
        desc.setBackgroundResource(R.color.trans);
    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getAuthorView() {
        return authorView;
    }

    public RelativeLayout getRelativeLayout() {
        return relativeLayout;
    }

    public TextView getDesc() {
        return desc;
    }

    public CardView getCardView() {
        return cardView;
    }
}
