package com.kartik.newsreader.card_view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kartik.newsreader.R;

/**
 * Created by kartik on 7/11/17.
 */

public class NewsViewHolder extends RecyclerView.ViewHolder {

    protected TextView titleView;
    protected TextView authorView;
    protected Button visitSite;

    public NewsViewHolder(View v) {
        super(v);

        titleView = v.findViewById(R.id.title);
        authorView = v.findViewById(R.id.author);
        visitSite = v.findViewById(R.id.visitSite);

    }
}
