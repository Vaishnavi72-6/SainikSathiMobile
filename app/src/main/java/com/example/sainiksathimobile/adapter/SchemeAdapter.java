package com.example.sainiksathimobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sainiksathimobile.R;
import com.example.sainiksathimobile.models.DependentScheme;

import java.util.List;

public class SchemeAdapter extends RecyclerView.Adapter<SchemeAdapter.SchemeViewHolder> {

    private List<DependentScheme> schemeList;
    private Context context;

    public SchemeAdapter(List<DependentScheme> schemeList) {
        this.schemeList = schemeList;
    }

    @NonNull
    @Override
    public SchemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_scheme_card, parent, false);
        return new SchemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SchemeViewHolder holder, int position) {
        DependentScheme scheme = schemeList.get(position);
        holder.tvTitle.setText(scheme.title);
        holder.tvDescription.setText(scheme.description);
        holder.tvEligibility.setText("Eligibility: " +
                scheme.educationLevel + ", Age ≤ " + scheme.maxAge + ", Income ≤ ₹" + scheme.maxIncome);

        // View Details (for now, show dialog or Toast)
        holder.btnViewDetails.setOnClickListener(v -> {
            String details = "📜 " + scheme.title + "\n\n" +
                    scheme.description + "\n\n" +
                    "Eligibility:\n• " + scheme.educationLevel +
                    "\n• Max Age: " + scheme.maxAge +
                    "\n• Max Income: ₹" + scheme.maxIncome +
                    "\n• Relation: " + scheme.applicableRelation +
                    "\n• State: " + scheme.applicableState;

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("Scheme Details");
            builder.setMessage(details);
            builder.setPositiveButton("OK", null);
            builder.show();
        });

        // Apply Now (open dummy link)
        holder.btnApplyNow.setOnClickListener(v -> {
            // Replace with real URL if available
            if (scheme.applyUrl != null && !scheme.applyUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme.applyUrl));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Apply link not available for this scheme.", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public int getItemCount() {
        return schemeList.size();
    }

    public static class SchemeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvEligibility;
        Button btnViewDetails, btnApplyNow;

        public SchemeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSchemeTitle);
            tvDescription = itemView.findViewById(R.id.tvSchemeDescription);
            tvEligibility = itemView.findViewById(R.id.tvEligibility);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            btnApplyNow = itemView.findViewById(R.id.btnApplyNow);
        }
    }
}
