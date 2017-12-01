package com.kartik.newsreader.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;

import com.kartik.newsreader.R;
import com.kartik.newsreader.data.PublicationInfo;
import com.kartik.newsreader.service.FontService;
import com.kartik.newsreader.viewholder.PublicationViewHolder;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by kartik on Fri, 8/11/17
 */

public class ListviewAdapter extends ArrayAdapter<PublicationInfo> {

    private ArrayList<PublicationInfo> list;
    private LayoutInflater inflater;
    private HashMap<String, Boolean> pref;

    public ListviewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PublicationInfo> objects, int size) {
        super(context, resource, objects);
        list = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pref = new HashMap<>();
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

        holder.title.setTypeface(FontService.getProductSansBold(convertView.getContext()));
        holder.desc.setTypeface(FontService.getProductSans(convertView.getContext()));

        holder.title.setText(list.get(position).name);
        holder.desc.setText(list.get(position).desc);
        holder.enSwitch.setChecked((pref.containsKey(list.get(position).id))?pref.get(list.get(position).id):false);
        holder.enSwitch.setTag(position);

        holder.enSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch s = (Switch) v;
                int p = (int) s.getTag();
                String key = list.get(p).id;
                pref.put(key, !pref.get(key));
            }
        });

        return convertView;
    }

    public HashMap<String, Boolean> getPreferences() {
        return pref;
    }

    public void setPreferences(HashMap<String, Boolean> pref) {
        this.pref = (pref == null)?new HashMap<String, Boolean>():pref;
    }


}
