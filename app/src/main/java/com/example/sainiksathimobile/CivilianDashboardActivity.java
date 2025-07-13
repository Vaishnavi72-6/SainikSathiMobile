package com.example.sainiksathimobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class CivilianDashboardActivity extends AppCompatActivity {

    CardView cardExServicemanStudent, cardGeneralSupport, cardNccPrep, cardDefenseJobs;
    // Add more card references if needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civilian_dashboard);

        // Initialize the Ex-Serviceman Student Support card
        cardExServicemanStudent = findViewById(R.id.cardExServicemanStudent);

        // Optional: Other cards
        cardGeneralSupport = findViewById(R.id.cardGeneralSupport);
        cardNccPrep = findViewById(R.id.cardNccPrep);
        cardDefenseJobs = findViewById(R.id.cardDefenseJobs);

        // Set click listeners
        cardExServicemanStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the Dependent Student Dashboard
                Intent intent = new Intent(CivilianDashboardActivity.this, DependentStudentDashboardActivity.class);
                startActivity(intent);
            }
        });

        // Example: Add click actions for other cards if needed
        cardGeneralSupport.setOnClickListener(v -> {
            // Add logic or intent
        });

        cardNccPrep.setOnClickListener(v -> {
            // Add logic or intent
        });

        cardDefenseJobs.setOnClickListener(v -> {
            // Add logic or intent
        });
    }
}
