package com.example.sainiksathimobile;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WellnessTipsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellness_tips);

        TextView articleText = findViewById(R.id.tvArticleText);
        articleText.setText("🧘 Mental Wellness Tips:\n\n" +
                "• Practice daily gratitude.\n" +
                "• Take short walks in nature.\n" +
                "• Avoid negative social media.\n" +
                "• Meditate at least 5 minutes a day.\n" +
                "• Talk to someone if you feel overwhelmed.");
    }
}
