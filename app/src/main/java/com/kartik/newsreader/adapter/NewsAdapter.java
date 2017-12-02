package com.kartik.newsreader.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.kartik.newsreader.R;
import com.kartik.newsreader.activity.WebViewActivity;

import com.kartik.newsreader.data.NewsInfo;
import com.kartik.newsreader.viewholder.NewsViewHolder;
import com.kartik.newsreader.glide.GlideApp;


import java.util.ArrayList;

import static com.kartik.newsreader.data.NewsInfo.AUTHOR_PREFIX;
import static com.kartik.newsreader.data.NewsInfo.SOURCE_PREFIX;

/**
 * Created by kartik on 7/11/17.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    public ArrayList<NewsInfo> newsList;
    public Context mContext;
    Point displaySize;
    Size size;
    boolean hasSize = false;

    public NewsAdapter(ArrayList<NewsInfo> newsList, Context mContext, Point displaySize) {
        this.newsList = newsList;
        this.mContext = mContext;
        this.displaySize = displaySize;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.card_layout, parent, false);

        return new NewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {
        final NewsInfo info = newsList.get(position);
        Log.i("data", info.toString());
        String a = String.format("%s %s", AUTHOR_PREFIX, info.getAuthor());
        String b = String.format("%s %s", SOURCE_PREFIX, info.getSource());
        holder.getAuthorView().setText(info.getAuthor().equals("null") || info.getAuthor().equals("") || info.getAuthor().startsWith("http")?b:a);
        //holder.getAuthorView().setVisibility(info.getAuthor().equals("null") || info.getAuthor().equals("") || info.getAuthor().startsWith("http")?View.GONE:View.VISIBLE);
        holder.getTitleView().setText(info.getTitle());
        //holder.getDesc().setText(info.getDesc());
        holder.getRelativeLayout().setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), WebViewActivity.class);
            intent.putExtra("url", info.getUrl());
            v.getContext().startActivity(intent);
        });
        Log.i("View", holder.toString());
        loadImage(holder, position);
        Log.i("View", holder.toString());

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void updateViews(ArrayList<NewsInfo> list) {
        newsList.clear();
        newsList.addAll(list);
        this.notifyDataSetChanged();
    }

    private void loadImage(final NewsViewHolder holder, int position) {
        final NewsInfo info = newsList.get(position);
        GlideApp.with(mContext)
                .asBitmap()
                .placeholder(R.drawable.loading_spinner)
                .error(R.drawable.newspaper)
                .centerCrop()
                .load(info.getThumbNailURL().equals("null")?R.drawable.newspaper:info.getThumbNailURL())
                .into(holder.getImageBG());
    }
}
