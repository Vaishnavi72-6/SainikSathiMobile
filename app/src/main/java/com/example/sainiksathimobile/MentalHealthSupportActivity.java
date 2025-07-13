package com.example.sainiksathimobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MentalHealthSupportActivity extends AppCompatActivity {

    Button btnCallHelpline, btnOpenMeditation, btnViewArticles, btnCheckSymptoms, btnTalkToCounselor, btnEmergencySOS;
    TextView tvTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental_health_support);

        btnCallHelpline = findViewById(R.id.btnCallHelpline);
        btnOpenMeditation = findViewById(R.id.btnOpenMeditation);
        btnViewArticles = findViewById(R.id.btnViewArticles);
        btnCheckSymptoms = findViewById(R.id.btnCheckSymptoms);
        btnTalkToCounselor = findViewById(R.id.btnTalkToCounselor);
        btnEmergencySOS = findViewById(R.id.btnEmergencySOS);
        tvTips = findViewById(R.id.tvTips);

        tvTips.setText("Tips:\n• Talk to someone\n• Take deep breaths\n• Practice gratitude\n• Go for a walk\n• Try meditation apps");

        btnCallHelpline.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:18005990019"));
            startActivity(intent);
        });

        btnOpenMeditation.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=meditation+for+veterans"));
            startActivity(intent);
        });

        btnViewArticles.setOnClickListener(v -> {
            Intent intent = new Intent(this, WellnessTipsActivity.class);
            startActivity(intent);
        });

        btnCheckSymptoms.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelfAssessmentActivity.class);
            startActivity(intent);
        });
        btnTalkToCounselor.setOnClickListener(v -> {
            Intent intent = new Intent(this, AIChatBotActivity.class);
            startActivity(intent);
        });

        btnTalkToCounselor.setOnClickListener(v -> {
            String[] options = {"Call Helpline", "Chat via WhatsApp", "Visit Website"};

            new AlertDialog.Builder(MentalHealthSupportActivity.this)
                    .setTitle("Talk to a Counselor")
                    .setItems(options, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:18005990019"));
                                startActivity(callIntent);
                                break;

                            case 1:
                                String phoneNumber = "+919999999999";
                                String message = "Hello, I need mental health support.";
                                try {
                                    Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                                    whatsappIntent.setData(Uri.parse("https://wa.me/" + phoneNumber + "?text=" + Uri.encode(message)));
                                    startActivity(whatsappIntent);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case 2:
                                String url = "https://www.mindpeers.co";
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);
                                break;
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        btnEmergencySOS.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:112"));
            startActivity(intent);
        });
    }
}
