package com.example.sainiksathimobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ApplyForBenefitsActivity extends AppCompatActivity {

    private EditText etRank, etServiceYears;
    private Switch switchDisability;
    private Spinner spinnerState;
    private Button btnFetchJobs;
    private RecyclerView recyclerJobs;
    private TextView tvJobsLabel;
    private Button btnShowSchemes;
    private TextView tvSchemesLabel;
    private LinearLayout layoutSchemes;
    private Button btnShowCanteens;
    private TextView tvCanteensLabel;
    private LinearLayout layoutCanteens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_for_benefits);

        // Link UI elements to XML
        etRank = findViewById(R.id.etRank);
        etServiceYears = findViewById(R.id.etServiceYears);
        switchDisability = findViewById(R.id.switchDisability);
        spinnerState = findViewById(R.id.spinnerState);
        btnFetchJobs = findViewById(R.id.btnFetchJobs);
        recyclerJobs = findViewById(R.id.recyclerJobs);
        tvJobsLabel = findViewById(R.id.tvJobsLabel);
        btnShowSchemes = findViewById(R.id.btnShowSchemes);
        tvSchemesLabel = findViewById(R.id.tvSchemesLabel);
        layoutSchemes = findViewById(R.id.layoutSchemes);
        btnShowCanteens = findViewById(R.id.btnShowCanteens);
        tvCanteensLabel = findViewById(R.id.tvCanteensLabel);
        layoutCanteens = findViewById(R.id.layoutCanteens);

        // Button click listeners
        btnShowSchemes.setOnClickListener(v -> {
            tvSchemesLabel.setVisibility(View.VISIBLE);
            layoutSchemes.setVisibility(View.VISIBLE);
            populateSchemesFromJson(ApplyForBenefitsActivity.this);
        });
        btnShowCanteens.setOnClickListener(v -> {
            tvCanteensLabel.setVisibility(View.VISIBLE);
            layoutCanteens.setVisibility(View.VISIBLE);
            populateCanteensFromJson(ApplyForBenefitsActivity.this);
        });
        btnFetchJobs.setOnClickListener(v -> loadFilteredJobs());

        setupStateSpinner();
    }
    private void setupStateSpinner() {
        List<String> states = Arrays.asList(
                "Select State", "Andhra Pradesh", "Tamil Nadu", "Telangana", "Maharashtra",
                "Karnataka", "Uttar Pradesh", "Punjab", "Delhi", "Kerala"
        );
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerState.setAdapter(adapter);
    }

    private void loadFilteredJobs() {
        String rank = etRank.getText().toString().trim().toLowerCase();
        String serviceStr = etServiceYears.getText().toString().trim();
        boolean isDisabled = switchDisability.isChecked();

        if (rank.isEmpty() || serviceStr.isEmpty()) {
            Toast.makeText(this, "Please enter all required details", Toast.LENGTH_SHORT).show();
            return;
        }

        int serviceYears;
        try {
            serviceYears = Integer.parseInt(serviceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid service years", Toast.LENGTH_SHORT).show();
            return;
        }

        List<JobModel> allJobs = loadJobsFromAssets(this);
        List<JobModel> filteredJobs = new ArrayList<>();

        for (JobModel job : allJobs) {
            boolean rankOK = job.rankRequired.contains("any") || job.rankRequired.contains(rank);
            boolean serviceOK = serviceYears >= job.minService;
            boolean disabilityOK = !job.disabledOnly || isDisabled;

            if (rankOK && serviceOK && disabilityOK) {
                filteredJobs.add(job);
            }
        }

        if (filteredJobs.isEmpty()) {
            Toast.makeText(this, "No matching jobs found", Toast.LENGTH_SHORT).show();
            recyclerJobs.setVisibility(RecyclerView.GONE);
            tvJobsLabel.setVisibility(TextView.GONE);
        } else {
            recyclerJobs.setLayoutManager(new LinearLayoutManager(this));
            recyclerJobs.setAdapter(new JobCardAdapter(this, filteredJobs, rank, serviceYears, isDisabled));
            recyclerJobs.setVisibility(RecyclerView.VISIBLE);
            tvJobsLabel.setVisibility(TextView.VISIBLE);
        }
    }

    private List<JobModel> loadJobsFromAssets(Context context) {
        List<JobModel> jobList = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("jobs_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray jobArray = new JSONArray(json);
            for (int i = 0; i < jobArray.length(); i++) {
                JSONObject obj = jobArray.getJSONObject(i);

                JobModel job = new JobModel();
                job.title = obj.getString("title");
                job.salary = obj.getString("salary");
                job.location = obj.getString("location");
                job.mapsUrl = obj.getString("mapsUrl"); // ✅ match JSON field
                job.phone = obj.getString("phone");
                job.applyUrl = obj.getString("applyUrl");
                job.minService = obj.getInt("minService");
                job.disabledOnly = obj.getBoolean("disabledOnly");

                JSONArray ranks = obj.getJSONArray("rankRequired");
                List<String> rankList = new ArrayList<>();
                for (int j = 0; j < ranks.length(); j++) {
                    rankList.add(ranks.getString(j).toLowerCase());
                }
                job.rankRequired = rankList;

                jobList.add(job);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobList;
    }

    private void populateSchemesFromJson(Context context) {
        String selectedState = spinnerState.getSelectedItem().toString();
        List<SchemeModel> schemes = loadSchemesFromAssets(context);
        layoutSchemes.removeAllViews();

        for (SchemeModel scheme : schemes) {
            // Show if Central OR matches selected state
            if (scheme.targetState.equalsIgnoreCase("All") ||
                    scheme.targetState.equalsIgnoreCase(selectedState)) {

                LinearLayout card = new LinearLayout(context);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(16, 16, 16, 16);
                card.setBackgroundResource(R.drawable.card_bg);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 24);
                card.setLayoutParams(lp);
                card.setElevation(6);

                TextView title = new TextView(context);
                title.setText("📝 " + scheme.name);
                title.setTextSize(16);
                title.setTypeface(null, Typeface.BOLD);

                TextView type = new TextView(context);
                type.setText("🏷️ Type: " + (scheme.type.equalsIgnoreCase("Central") ? "Central Govt" : "State Govt"));

                Button btnEligibility = new Button(context);
                btnEligibility.setText("📄 View Eligibility");
                btnEligibility.setOnClickListener(v -> showEligibilityDialog(scheme.eligibility));

                Button btnApply = new Button(context);
                btnApply.setText("🔗 Apply Now");
                btnApply.setOnClickListener(v -> {
                    if (scheme.applyUrl != null && !scheme.applyUrl.trim().isEmpty() && scheme.applyUrl.startsWith("http")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme.applyUrl));
                        startActivity(intent);
                    } else {
                        Toast.makeText(context, "⚠️ Apply URL not available or invalid", Toast.LENGTH_SHORT).show();
                    }
                });


                Button btnBrochure = new Button(context);
                btnBrochure.setText("📥 Download Brochure");

                btnBrochure.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PdfViewerActivity.class);
                    intent.putExtra(PdfViewerActivity.EXTRA_PDF_FILENAME, scheme.brochureFile);

                    startActivity(intent);
                });



                    card.addView(title);
                card.addView(type);
                card.addView(btnEligibility);
                card.addView(btnApply);
                card.addView(btnBrochure);

                layoutSchemes.addView(card);
            }
        }

        if (layoutSchemes.getChildCount() == 0) {
            Toast.makeText(context, "No schemes found for selected state.", Toast.LENGTH_SHORT).show();
        }
    }

    // ✅ Move this OUTSIDE the previous method
    private List<SchemeModel> loadSchemesFromAssets(Context context) {
        List<SchemeModel> list = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("schemes_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                SchemeModel scheme = new SchemeModel();
                scheme.name = obj.getString("name");
                scheme.type = obj.getString("type");
                scheme.targetState = obj.getString("target_state");
                scheme.eligibility = obj.getString("eligibility");
                scheme.applyUrl = obj.getString("applyUrl");
                scheme.brochureFile = obj.getString("brochureFile");

                list.add(scheme);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    private void populateCanteensFromJson(Context context) {
        layoutCanteens.removeAllViews();
        List<CsdCanteenModel> canteens = loadCanteensFromAssets(context);

        for (CsdCanteenModel canteen : canteens) {
            LinearLayout card = new LinearLayout(context);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(16, 16, 16, 16);
            card.setBackgroundResource(R.drawable.card_bg);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 24);
            card.setLayoutParams(lp);
            card.setElevation(6);

            TextView name = new TextView(context);
            name.setText("\uD83C\uDFEC " + canteen.name);
            name.setTextSize(16);
            name.setTypeface(null, Typeface.BOLD);

            TextView address = new TextView(context);
            address.setText("\uD83D\uDCCD " + canteen.address);

            TextView phone = new TextView(context);
            phone.setText("\uD83D\uDCDE " + canteen.phone);

            Button btnMap = new Button(context);
            btnMap.setText("\uD83D\uDCCD Open in Maps");
            btnMap.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(canteen.mapsUrl))));

            card.addView(name);
            card.addView(address);
            card.addView(phone);
            card.addView(btnMap);

            layoutCanteens.addView(card);
        }

        if (layoutCanteens.getChildCount() == 0) {
            Toast.makeText(context, "No canteens found!", Toast.LENGTH_SHORT).show();
        }
    }

    private List<CsdCanteenModel> loadCanteensFromAssets(Context context) {
        List<CsdCanteenModel> list = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("csd_canteens.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                CsdCanteenModel c = new CsdCanteenModel();
                c.name = obj.getString("name");
                c.address = obj.getString("address");
                c.phone = obj.getString("phone");
                c.mapsUrl = obj.getString("mapsUrl");
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    private void showEligibilityDialog(String message) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("📄 Scheme Eligibility")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
