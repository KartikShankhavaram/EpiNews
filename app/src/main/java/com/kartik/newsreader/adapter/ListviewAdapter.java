package com.kartik.newsreader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kartik.newsreader.R;
import com.kartik.newsreader.api.PublicationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kartik on 8/11/17.
 */

public class ListviewAdapter extends ArrayAdapter {

    ArrayList<PublicationInfo> list;

    public ListviewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PublicationInfo> objects) {
        super(context, resource, objects);
        list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(convertView.getContext());

        return inflater.inflate(R.layout.listview, parent, false);
    }
}
