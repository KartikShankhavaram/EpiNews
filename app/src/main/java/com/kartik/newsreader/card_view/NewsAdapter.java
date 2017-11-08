package com.kartik.newsreader.card_view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kartik.newsreader.R;
import com.kartik.newsreader.activity.WebViewActivity;

import com.bumptech.glide.Glide;
import com.kartik.newsreader.glide.GlideApp;


import java.util.ArrayList;

/**
 * Created by kartik on 7/11/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    public ArrayList<NewsInfo> newsList;
    public Context mContext;

    public NewsAdapter(ArrayList<NewsInfo> newsList, Context mContext) {
        this.newsList = newsList;
        this.mContext = mContext;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.card_layout, parent, false);

        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        final NewsInfo info = newsList.get(position);
        holder.authorView.setText(NewsInfo.AUTHOR_PREFIX + info.getAuthor());
        holder.titleView.setText(info.getTitle());
        holder.visitSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WebViewActivity.class);
                intent.putExtra("url", info.getUrl());
                v.getContext().startActivity(intent);
            }
        });
        GlideApp.with(mContext)
                .load(info.getThumbNailURL())
                .placeholder(R.drawable.loading_spinner)
                .fitCenter()
                .into(holder.thumbNail);

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }
}
