package com.example.sainiksathimobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FeatureAdapter extends BaseAdapter {

    Context context;
    String[] titles;
    int[] icons;
    LayoutInflater inflater;

    public FeatureAdapter(Context context, String[] titles, int[] icons) {
        this.context = context;
        this.titles = titles;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int i) {
        return titles[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (inflater == null) inflater = LayoutInflater.from(context);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.feature_card_item, viewGroup, false);

        ImageView icon = convertView.findViewById(R.id.imgIcon);
        TextView name = convertView.findViewById(R.id.tvFeatureName);

        icon.setImageResource(icons[position]);
        name.setText(titles[position]);

        return convertView;
    }
}
