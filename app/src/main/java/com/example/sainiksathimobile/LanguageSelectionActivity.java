package com.example.sainiksathimobile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LanguageSelectionActivity extends AppCompatActivity {

    Button btnSelectLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LanguageUtil.applySavedLocale(this); // ✅ Apply saved locale

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selection);

        btnSelectLanguage = findViewById(R.id.btnSelectLanguage);

        btnSelectLanguage.setOnClickListener(view -> showLanguageDialog());
    }

    private void showLanguageDialog() {
        final String[] langNames = {"English", "हिन्दी", "తెలుగు", "தமிழ்"};
        final String[] langCodes = {"en", "hi", "te", "ta"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Language");

        builder.setSingleChoiceItems(langNames, -1, (dialog, which) -> {
            // ✅ Save selected language
            LanguageUtil.setLocale(LanguageSelectionActivity.this, langCodes[which]);

            // ✅ Restart dashboard in selected language
            Intent intent = new Intent(LanguageSelectionActivity.this, ExServicemanDashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            dialog.dismiss();
        });

        builder.show();
    }
}
