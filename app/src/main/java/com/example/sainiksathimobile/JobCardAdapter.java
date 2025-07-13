package com.example.sainiksathimobile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobCardAdapter extends RecyclerView.Adapter<JobCardAdapter.JobViewHolder> {

    private final Context context;
    private final List<JobModel> jobs;
    private final String userRank;
    private final int userService;
    private final boolean userDisabled;

    public JobCardAdapter(Context context, List<JobModel> jobs, String rank, int service, boolean disabled) {
        this.context = context;
        this.jobs = jobs;
        this.userRank = rank;
        this.userService = service;
        this.userDisabled = disabled;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_job_card, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobModel job = jobs.get(position);

        holder.tvTitle.setText("🧳 " + job.title);
        holder.tvSalary.setText("💰 " + job.salary);
        holder.tvLocation.setText("📍 " + job.location);

        // View Eligibility
        holder.btnEligibility.setOnClickListener(v -> {
            boolean eligible = checkEligibility(job);
            String message = eligible
                    ? "✅ You are eligible for this job."
                    : "❌ You are NOT eligible.\nRequirements:\n• Min Service: " + job.minService +
                    "\n• Rank: " + job.rankRequired +
                    (job.disabledOnly ? "\n• Disabled only" : "");
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        });

        // Open in Google Maps
        holder.btnMaps.setOnClickListener(v -> {
            if (job.mapsUrl != null && !job.mapsUrl.trim().isEmpty()) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(job.mapsUrl));
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "Couldn't open map location", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Map link not available", Toast.LENGTH_SHORT).show();
            }
        });


        // Call HR
        holder.btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + job.phone));
            context.startActivity(intent);
        });

        // Apply Now
        holder.btnApply.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(job.applyUrl));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSalary, tvLocation;
        Button btnMaps, btnCall, btnApply, btnEligibility;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvJobTitle);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            btnMaps = itemView.findViewById(R.id.btnMaps);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnApply = itemView.findViewById(R.id.btnApply);
            btnEligibility = itemView.findViewById(R.id.btnEligibility);
        }
    }

    private boolean checkEligibility(JobModel job) {
        boolean rankOK = job.rankRequired.contains("any") || job.rankRequired.contains(userRank);
        boolean serviceOK = userService >= job.minService;
        boolean disabilityOK = !job.disabledOnly || userDisabled;
        return rankOK && serviceOK && disabilityOK;
    }
}
