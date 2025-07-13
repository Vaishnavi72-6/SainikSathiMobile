package com.example.sainiksathimobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SelfAssessmentActivity extends AppCompatActivity {

    CheckBox cb1, cb2, cb3, cb4, cb5;
    Button btnSubmit;
    TextView tvResult;
TextView tvTips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_assessment);

        cb1 = findViewById(R.id.cbQuestion1);
        cb2 = findViewById(R.id.cbQuestion2);
        cb3 = findViewById(R.id.cbQuestion3);
        cb4 = findViewById(R.id.cbQuestion4);
        cb5 = findViewById(R.id.cbQuestion5);

        btnSubmit = findViewById(R.id.btnSubmitAssessment);
        tvResult = findViewById(R.id.tvAssessmentResult);
        tvTips = findViewById(R.id.tvAssessmentTips);

        btnSubmit.setOnClickListener(v -> {
            int score = 0;
            if (cb1.isChecked()) score++;
            if (cb2.isChecked()) score++;
            if (cb3.isChecked()) score++;
            if (cb4.isChecked()) score++;
            if (cb5.isChecked()) score++;
            String resultMessage, tips;

            if (score >= 4) {
                resultMessage = "⚠️ High signs of stress or anxiety.";
                tips = "• Talk to a counselor\n• Practice guided meditation\n• Try journaling daily\n• Contact helpline if overwhelmed";
            } else if (score >= 2) {
                resultMessage = "🟠 Mild to moderate stress symptoms.";
                tips = "• Take short walks\n• Listen to calming music\n• Limit social media\n• Practice gratitude daily";
            } else {
                resultMessage = "🟢 You're doing well!";
                tips = "• Keep a healthy routine\n• Stay socially connected\n• Sleep and eat well\n• Keep doing self-checks regularly";
            }


            tvResult.setText(resultMessage);
            tvTips.setText("Tips:\n" + tips);
            tvResult.setVisibility(View.VISIBLE);
            tvTips.setVisibility(View.VISIBLE);
        });
    }
}