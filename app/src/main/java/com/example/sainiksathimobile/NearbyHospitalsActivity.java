package com.example.sainiksathimobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class NearbyHospitalsActivity extends AppCompatActivity {

    Spinner stateSpinner;
    Button searchButton;
    LinearLayout hospitalListLayout;

    String[] indianStates = {
            "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat",
            "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh",
            "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Punjab",
            "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh",
            "Uttarakhand", "West Bengal"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_hospitals); // or similar


        stateSpinner = findViewById(R.id.stateSpinner);
        Button btnFetchHospitals = findViewById(R.id.btnFetchHospitals);
        hospitalListLayout = findViewById(R.id.hospitalListLayout);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, indianStates);
        stateSpinner.setAdapter(adapter);

        btnFetchHospitals.setOnClickListener(v -> {
            String selectedState = stateSpinner.getSelectedItem().toString();
            fetchHospitalsByState(selectedState);
        });
    }

    static class Hospital {
        String name, lat, lon;
        Hospital(String name, String lat, String lon) {
            this.name = name;
            this.lat = lat;
            this.lon = lon;
        }
    }

    private void fetchHospitalsByState(String state) {
        String apiKey = "pk.a317596a86711e183d4b808f9ed08f93"; // Replace with your key
        String query = "military hospital OR army hospital OR navy hospital OR airforce hospital in " + state;
        String url = "https://us1.locationiq.com/v1/autocomplete.php?key=" + apiKey +
                "&q=" + query +
                "&format=json";



        new Thread(() -> {
            try {
                URL apiUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
                conn.setRequestProperty("User-Agent", "SainikSathiApp");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONArray results = new JSONArray(response.toString());
                List<Hospital> hospitals = new ArrayList<>();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    String name = obj.optString("display_name", "Unknown");
                    String lat = obj.optString("lat", "");
                    String lon = obj.optString("lon", "");

                    if (!lat.isEmpty() && !lon.isEmpty()) {
                        hospitals.add(new Hospital(name, lat, lon));
                    }
                }

                runOnUiThread(() -> showHospitals(hospitals));

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Error fetching hospitals", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void showHospitals(List<Hospital> hospitals) {
        hospitalListLayout.removeAllViews();

        if (hospitals.isEmpty()) {
            Toast.makeText(this, "No defence hospitals found", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Hospital h : hospitals) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(30, 30, 30, 30);
            card.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 30);
            card.setLayoutParams(params);

            TextView nameText = new TextView(this);
            nameText.setText("🏥 " + h.name);
            nameText.setTextSize(18);
            card.addView(nameText);

            Button callBtn = new Button(this);
            callBtn.setText("📞 Call Hospital");
            callBtn.setOnClickListener(v -> {
                Intent dial = new Intent(Intent.ACTION_DIAL);
                dial.setData(Uri.parse("tel:100")); // 🔁 Replace with actual numbers if available
                startActivity(dial);
            });
            card.addView(callBtn);

            Button mapBtn = new Button(this);
            mapBtn.setText("🧭 Get Directions");
            mapBtn.setOnClickListener(v -> {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + h.lat + "," + h.lon);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            });
            card.addView(mapBtn);

            hospitalListLayout.addView(card);
        }
    }
}
