package com.kartik.newsreader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;

import com.kartik.newsreader.R;
import com.kartik.newsreader.data.PublicationInfo;
import com.kartik.newsreader.viewholder.PublicationViewHolder;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by kartik on 8/11/17.
 */

public class ListviewAdapter extends ArrayAdapter<PublicationInfo> {

    ArrayList<PublicationInfo> list;
    LayoutInflater inflater;
    ArrayList<Boolean> selected;

    public ListviewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PublicationInfo> objects, int size) {
        super(context, resource, objects);
        list = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        selected = new ArrayList<>(Collections.nCopies(size, false));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PublicationViewHolder holder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.listview, parent, false);
            holder = new PublicationViewHolder();
            holder.title = convertView.findViewById(R.id.title_publ);
            holder.desc = convertView.findViewById(R.id.desc_publ);
            holder.enSwitch = convertView.findViewById(R.id.select_switch);
            convertView.setTag(holder);
        } else {
            holder = (PublicationViewHolder) convertView.getTag();
            holder.title = convertView.findViewById(R.id.title_publ);
            holder.desc = convertView.findViewById(R.id.desc_publ);
            holder.enSwitch = convertView.findViewById(R.id.select_switch);
        }

        holder.title.setText(list.get(position).name);
        holder.desc.setText(list.get(position).desc);
        holder.enSwitch.setChecked(selected.get(position));
        holder.enSwitch.setTag(position);

        holder.enSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch s = (Switch) v;
                int p = (int) s.getTag();
                selected.set(p, !selected.get(p));
                Log.i("Selected", selected.toString());
            }
        });

        return convertView;
    }

    public ArrayList getSelectedList() {
        return selected;
    }


}
