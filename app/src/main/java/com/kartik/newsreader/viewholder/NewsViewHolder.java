package com.kartik.newsreader.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kartik.newsreader.R;
import com.kartik.newsreader.service.FontService;

/**
 * Created by kartik on 7/11/17.
 */

public class NewsViewHolder extends RecyclerView.ViewHolder {

    private TextView titleView;
    private TextView authorView;
    private Button visitSite;
    private ImageView thumbNail;

    public NewsViewHolder(View v) {
        super(v);

        titleView = v.findViewById(R.id.title);
        authorView = v.findViewById(R.id.author);
        visitSite = v.findViewById(R.id.visit_page);
        thumbNail = v.findViewById(R.id.thumbnail);

        //titleView.setTypeface(FontService.getProductSans(v.getContext()));
        //authorView.setTypeface(FontService.getProductSans(v.getContext()));
        //visitSite.setTypeface(FontService.getProductSans(v.getContext()));

    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getAuthorView() {
        return authorView;
    }

    public Button getVisitSite() {
        return visitSite;
    }

    public ImageView getThumbNail() {
        return thumbNail;
    }

}
