package com.example.sainiksathimobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TranslationActivity extends AppCompatActivity {

    private EditText inputText;
    private Spinner languageSpinner;
    private Button btnTranslate;
    private TextView outputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);

        inputText = findViewById(R.id.inputText);
        languageSpinner = findViewById(R.id.languageSpinner);
        btnTranslate = findViewById(R.id.btnTranslate);
        outputText = findViewById(R.id.outputText);

        // Language codes
        String[] languages = {"hi", "ta", "te", "ml", "bn", "gu", "mr", "kn", "ur", "en"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        btnTranslate.setOnClickListener(v -> translateText());
    }

    private void translateText() {
        String textToTranslate = inputText.getText().toString().trim();
        String selectedTargetLang = languageSpinner.getSelectedItem().toString().trim();
        String selectedSourceLang = "en";

        if (textToTranslate.isEmpty()) {
            Toast.makeText(this, "Please enter text to translate", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                URL url = new URL("https://libretranslate.com/translate");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Prepare JSON request body
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("q", textToTranslate);
                jsonBody.put("source", selectedSourceLang);
                jsonBody.put("target", selectedTargetLang);
                jsonBody.put("format", "text");

                OutputStream os = conn.getOutputStream();
                os.write(jsonBody.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                String translatedText = jsonResponse.getString("translatedText");

                runOnUiThread(() -> outputText.setText(translatedText));

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(TranslationActivity.this,
                        "Translation failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
