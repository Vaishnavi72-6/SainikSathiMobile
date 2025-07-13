package com.example.sainiksathimobile;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;

public class ServiceTimelineActivity extends AppCompatActivity {

    private LinearLayout timelineLayout;
    EditText etRetiredRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_timeline);
        etRetiredRank = findViewById(R.id.etRetiredRank);
        String retiredRank = etRetiredRank.getText().toString().trim();
// Demo rank progression
        List<String> rankProgression = Arrays.asList("Sepoy", "Naik", "Havildar", "Subedar", "Subedar Major", "Honorary Captain");


        timelineLayout = findViewById(R.id.timelineLayout);

        addServiceMilestone("2000", "Joined Indian Army as Sepoy");
        addServiceMilestone("2005", "Promoted to Naik");
        addServiceMilestone("2010", "Promoted to Havildar");
        addServiceMilestone("2015", "Posted at Siachen Glacier");
        addServiceMilestone("2020", "Retired with Honors as Subedar");
    }

    private void addServiceMilestone(String year, String event) {
        TextView tv = new TextView(this);
        tv.setText(year + " - " + event);
        tv.setTextSize(16);
        tv.setPadding(8, 16, 8, 16);
        timelineLayout.addView(tv);
    }
}
