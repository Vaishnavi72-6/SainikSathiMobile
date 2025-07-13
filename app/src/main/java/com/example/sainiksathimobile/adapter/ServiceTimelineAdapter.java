package com.example.sainiksathimobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sainiksathimobile.R;
import com.example.sainiksathimobile.data.model.ServiceTimeline;

import java.util.List;

public class ServiceTimelineAdapter extends RecyclerView.Adapter<ServiceTimelineAdapter.ViewHolder> {

    private final List<ServiceTimeline> timelineList;

    public ServiceTimelineAdapter(List<ServiceTimeline> timelineList) {
        this.timelineList = timelineList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textYear, textPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            textYear = itemView.findViewById(R.id.textYear);
            textPosition = itemView.findViewById(R.id.textRank);

        }
    }

    @NonNull
    @Override
    public ServiceTimelineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_timeline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceTimelineAdapter.ViewHolder holder, int position) {
        ServiceTimeline entry = timelineList.get(position);
        holder.textYear.setText("Year: " + entry.year);
        holder.textPosition.setText("Rank: " + entry.position);
    }

    @Override
    public int getItemCount() {
        return timelineList.size();

    }

}
