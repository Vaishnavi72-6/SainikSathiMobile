package com.example.sainiksathimobile;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {

    private List<Hospital> hospitalList;

    public HospitalAdapter(List<Hospital> hospitalList) {
        this.hospitalList = hospitalList;
    }

    public void updateData(List<Hospital> newList) {
        this.hospitalList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, coordinates;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.hospitalName);
            coordinates = itemView.findViewById(R.id.hospitalAddress);
        }
    }

    @Override
    public HospitalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hospital_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HospitalAdapter.ViewHolder holder, int position) {
        Hospital h = hospitalList.get(position);
        holder.name.setText(h.name);
        holder.coordinates.setText(h.latitude + ", " + h.longitude);

        holder.itemView.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(h.latitude + "," + h.longitude + " (" + h.name + ")"));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            v.getContext().startActivity(mapIntent);
        });
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }
}
