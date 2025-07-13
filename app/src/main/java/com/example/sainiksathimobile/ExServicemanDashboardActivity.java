package com.example.sainiksathimobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import java.util.Locale;
public class ExServicemanDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome, tvName, tvRank, tvAmount, tvDate, tvRemarks;
    CardView cardViewTimeline, cardViewPension, cardScheme, cardHospitals,
            cardMentalHealth, cardAI, cardChangeLanguage, cardTranslate;
    CardView cardManageDependents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LanguageUtil.applySavedLocale(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exserviceman_dashboard);

        // Initialize TextViews
        tvWelcome = findViewById(R.id.tvWelcome);
        tvName = findViewById(R.id.tvName);
        tvRank = findViewById(R.id.tvRank);
        tvAmount = findViewById(R.id.tvAmount);
        tvDate = findViewById(R.id.tvDate);
        tvRemarks = findViewById(R.id.tvRemarks);

        // Initialize Buttons
        cardViewTimeline = findViewById(R.id.cardViewTimeline);
        cardViewPension = findViewById(R.id.cardViewPension);

        CardView cardBenefit = findViewById(R.id.cardBenefit);
        cardHospitals = findViewById(R.id.cardHospitals);
        cardMentalHealth = findViewById(R.id.cardMentalHealth);
        cardAI = findViewById(R.id.cardAI);
        cardChangeLanguage = findViewById(R.id.cardChangeLanguage);
        cardTranslate = findViewById(R.id.cardTranslate);
        CardView cardRetirementEstimator = findViewById(R.id.cardRetirementEstimator);
        CardView cardHeroStory = findViewById(R.id.cardHeroStory);
        cardManageDependents = findViewById(R.id.cardManageDependents);
        CardView cardGovtNotifications = findViewById(R.id.cardGovtNotifications);
        // Set welcome info dynamically if you have user data (example)
        String userName = "Vaishnavi Reddy";  // replace with actual user data
        String userRank = "Captain";           // replace with actual user data
        tvWelcome.setText("Welcome, " + userName + "!");
        tvName.setText("Name: " + userName);
        tvRank.setText("Rank: " + userRank);
        cardChangeLanguage.setOnClickListener(v -> {
            startActivity(new Intent(this, LanguageSelectionActivity.class));
        });
        // Pension details - replace with real data from DB or API
        tvAmount.setText("Amount: ₹25680");
        tvDate.setText("Paid On: 2025-06-30");
        tvRemarks.setText("Remarks: Defence Pension");

        cardViewPension.setOnClickListener(v -> {
            startActivity(new Intent(this, PensionTrackerActivity.class));
        });
        // Button Click Listeners - launch respective activities (replace with your real activity classes)
        cardViewTimeline.setOnClickListener(v -> {
            startActivity(new Intent(this, ServiceTimelineActivity.class));
        });






        cardBenefit.setOnClickListener(v -> {
            Intent intent = new Intent(ExServicemanDashboardActivity.this, ApplyForBenefitsActivity.class);
            startActivity(intent);
        });

        cardHospitals.setOnClickListener(v -> {
            startActivity(new Intent(this, NearbyHospitalsActivity.class));
        });

        cardMentalHealth.setOnClickListener(v -> {
            startActivity(new Intent(this, MentalHealthSupportActivity.class));
        });
        cardAI.setOnClickListener(v -> {
            startActivity(new Intent(this, AIChatBotActivity.class));
        });

        cardChangeLanguage.setOnClickListener(v -> {
            startActivity(new Intent(this, LanguageSelectionActivity.class));
        });

        cardTranslate.setOnClickListener(v -> {
            startActivity(new Intent(this, TranslationActivity.class));
        });
        cardRetirementEstimator.setOnClickListener(v -> {
            Intent intent = new Intent(ExServicemanDashboardActivity.this, RetirementEstimatorActivity.class);
            startActivity(intent);
        });
        cardHeroStory.setOnClickListener(v -> {
            Intent intent = new Intent(ExServicemanDashboardActivity.this, HeroStoryActivity.class);
            startActivity(intent);
        });
        cardManageDependents.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageDependentsActivity.class);
            startActivity(intent);
        });
        cardGovtNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(ExServicemanDashboardActivity.this, GovtNotificationsActivity.class);
            startActivity(intent);
        });
    }
        public static void loadLocale(Context context) {
            SharedPreferences prefs = context.getSharedPreferences("Settings", MODE_PRIVATE);
            String lang = prefs.getString("My_Lang", "en");

            Locale locale = new Locale(lang);
            Locale.setDefault(locale);

            Configuration config = new Configuration();
            config.locale = locale;

            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }


