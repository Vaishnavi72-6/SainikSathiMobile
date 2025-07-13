package com.example.sainiksathimobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {
    Button btnExServiceman, btnCivilian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button btnExServiceman = findViewById(R.id.btnExServiceman);
        Button btnCivilian = findViewById(R.id.btnCivilian);

        btnExServiceman.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ExServicemanDashboardActivity.class);
            startActivity(intent);
        });

        btnCivilian.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, CivilianDashboardActivity.class);
            startActivity(intent);
        });
    }
}
