package com.example.sainiksathimobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class DependentStudentDashboardActivity extends AppCompatActivity {

    GridView gridView;
    String[] featureNames = {
            "Scheme Recommender", "Smart Form Helper", "Certificate Generator", "College Finder",
            "Deadline Alerts", "Application Tracker", "Helpdesk Access", "AI Chat Assistant",
            "Resume Builder", "Interview Prep", "Service Timeline", "Secure Vault",
            "Offline Support", "Google Drive Sync", "Voice Input", "Career Guidance"
    };

    int[] featureIcons = {
            R.drawable.ic_scholarship, R.drawable.ic_form, R.drawable.ic_certificate, R.drawable.ic_college,
            R.drawable.ic_alert, R.drawable.ic_tracker, R.drawable.ic_helpline, R.drawable.ic_ai,
            R.drawable.ic_resume, R.drawable.ic_interview, R.drawable.ic_timeline, R.drawable.ic_vault,
            R.drawable.ic_offline, R.drawable.ic_drive, R.drawable.ic_voice, R.drawable.ic_career
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dependent_student_dashboard);

        gridView = findViewById(R.id.gridViewFeatures);
        FeatureAdapter adapter = new FeatureAdapter(this, featureNames, featureIcons);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((adapterView, view, position, l) -> {
            switch (position) {
                case 0:
                    startActivity(new Intent(this, DependentSchemeRecommenderActivity.class));
                    break;
                case 1:
                    startActivity(new Intent(this, SmartFormSahayakActivity.class));
                    break;
                case 2:
                    startActivity(new Intent(this, DependentCertificateActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(this, DefenseCollegeFinderActivity.class));
                    break;
                // Add cases for other features...
            }
        });
    }
}

