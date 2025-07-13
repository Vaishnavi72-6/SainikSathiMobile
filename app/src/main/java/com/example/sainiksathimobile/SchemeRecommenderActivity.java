package com.example.sainiksathimobile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sainiksathimobile.adapter.SchemeAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sainiksathimobile.models.DependentScheme;

import java.util.ArrayList;
import java.util.List;

public class SchemeRecommenderActivity extends AppCompatActivity {
    RecyclerView recyclerMatchedSchemes;

    EditText etAge, etIncome;
    Spinner spinnerGender, spinnerEducation, spinnerRelation, spinnerState;
    Button btnFindSchemes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_recommender);

        // Initialize views
        etAge = findViewById(R.id.etAge);
        etIncome = findViewById(R.id.etIncome);
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerEducation = findViewById(R.id.spinnerEducation);
        spinnerRelation = findViewById(R.id.spinnerRelation);
        spinnerState = findViewById(R.id.spinnerState);
        btnFindSchemes = findViewById(R.id.btnFindSchemes);
        recyclerMatchedSchemes = findViewById(R.id.recyclerMatchedSchemes);
        recyclerMatchedSchemes.setLayoutManager(new LinearLayoutManager(this));

        // Setup Spinner values
        setupSpinners();

        // Handle button click
        btnFindSchemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFormSubmission();
            }
        });
    }

    private void setupSpinners() {
        String[] genders = {"Male", "Female", "Other"};
        String[] educationLevels = {"10th", "12th", "Undergraduate", "Postgraduate", "Other"};
        String[] relations = {"Son", "Daughter", "Widow", "Spouse", "Grandchild"};
        String[] states = {"Andhra Pradesh", "Telangana", "Maharashtra", "Uttar Pradesh", "All India"};

        spinnerGender.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders));
        spinnerEducation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, educationLevels));
        spinnerRelation.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, relations));
        spinnerState.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, states));
    }

    private void handleFormSubmission() {
        String ageStr = etAge.getText().toString().trim();
        String incomeStr = etIncome.getText().toString().trim();

        if (ageStr.isEmpty() || incomeStr.isEmpty()) {
            Toast.makeText(this, "Please enter both age and income", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        int income = Integer.parseInt(incomeStr);

        String gender = spinnerGender.getSelectedItem().toString();
        String education = spinnerEducation.getSelectedItem().toString();
        String relation = spinnerRelation.getSelectedItem().toString();
        String state = spinnerState.getSelectedItem().toString();

        // Get mock schemes and filter
        List<DependentScheme> allSchemes = getMockSchemes();
        List<DependentScheme> matchedSchemes = new ArrayList<>();

        for (DependentScheme scheme : allSchemes) {
            if (age <= scheme.maxAge &&
                    income <= scheme.maxIncome &&
                    education.equalsIgnoreCase(scheme.educationLevel) &&
                    relation.equalsIgnoreCase(scheme.applicableRelation) &&
                    (scheme.applicableState.equalsIgnoreCase("All India") || state.equalsIgnoreCase(scheme.applicableState))
            ) {
                matchedSchemes.add(scheme);
            }
        }

        if (matchedSchemes.isEmpty()) {
            recyclerMatchedSchemes.setVisibility(View.GONE);
            Toast.makeText(this, "No matching schemes found.", Toast.LENGTH_LONG).show();
        } else {
            recyclerMatchedSchemes.setVisibility(View.VISIBLE);
            SchemeAdapter adapter = new SchemeAdapter(matchedSchemes);
            recyclerMatchedSchemes.setAdapter(adapter);
        }

    }

    private List<DependentScheme> getMockSchemes() {
        List<DependentScheme> list = new ArrayList<>();

        list.add(new DependentScheme(
                "PM Scholarship Scheme",
                "₹30,000 per year for UG/PG dependents of ex-servicemen.",
                "Undergraduate", 25, 800000,
                "Daughter", "All India",
                "https://ksb.gov.in/pmss.htm"));

        list.add(new DependentScheme(
                "Telangana Soldiers’ Children Fund",
                "Scholarship for 10th/12th pass children of ex-servicemen in Telangana.",
                "12th", 22, 600000,
                "Son", "Telangana",
                "https://sainikwelfare.telangana.gov.in"));

        list.add(new DependentScheme(
                "Widow Support UG Scheme",
                "Support for widows of ex-servicemen pursuing UG courses.",
                "Undergraduate", 40, 900000,
                "Widow", "All India",
                "https://ksb.gov.in/widow-scheme.htm"));

        return list;
    }
}