package com.example.sainiksathimobile

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Check login status
        sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        if (!isLoggedIn) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // 2. Set layout
        setContentView(R.layout.activity_main)

        // 3. Find all feature buttons
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnPension = findViewById<Button>(R.id.btnPension)
        val btnSchemes = findViewById<Button>(R.id.btnSchemes)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val btnApplyBenefits = findViewById<Button>(R.id.btnApplyBenefits)
        val btnAIChatbot = findViewById<Button>(R.id.btnAIChatbot)
        val btnTimeline = findViewById<Button>(R.id.btnTimeline)
        val btnNearbyHospitals = findViewById<Button>(R.id.btnNearbyHospitals)
        val btnMentalHealth = findViewById<Button>(R.id.btnMentalHealth)
        val btnNotifications = findViewById<Button>(R.id.btnNotifications)
        val btnDependents = findViewById<Button>(R.id.btnDependents)
        val btnDownloadCertificate = findViewById<Button>(R.id.btnDownloadCertificate)

        // 4. Set listeners for main features
        btnAdd.setOnClickListener {
            Toast.makeText(this, "Add Pension clicked", Toast.LENGTH_SHORT).show()
        }

        btnPension.setOnClickListener {
            Toast.makeText(this, "View Pension Details clicked", Toast.LENGTH_SHORT).show()
        }

        btnSchemes.setOnClickListener {
            Toast.makeText(this, "View Govt Schemes clicked", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            sharedPref.edit {
                putBoolean("isLoggedIn", false)
            }
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // 5. Set listeners for new features
        btnApplyBenefits.setOnClickListener {
            Toast.makeText(this, "Apply for Govt Benefit clicked", Toast.LENGTH_SHORT).show()
        }

        btnAIChatbot.setOnClickListener {
            Toast.makeText(this, "AI Assistant clicked", Toast.LENGTH_SHORT).show()
        }

        btnTimeline.setOnClickListener {
            Toast.makeText(this, "Service Timeline clicked", Toast.LENGTH_SHORT).show()
        }

        btnNearbyHospitals.setOnClickListener {
            Toast.makeText(this, "Nearby Military Hospitals clicked", Toast.LENGTH_SHORT).show()
        }

        btnMentalHealth.setOnClickListener {
            Toast.makeText(this, "Mental Health Support clicked", Toast.LENGTH_SHORT).show()
        }

        btnNotifications.setOnClickListener {
            Toast.makeText(this, "Latest Govt Notifications clicked", Toast.LENGTH_SHORT).show()
        }

        btnDependents.setOnClickListener {
            Toast.makeText(this, "Dependent Management clicked", Toast.LENGTH_SHORT).show()
        }

        btnDownloadCertificate.setOnClickListener {
            Toast.makeText(this, "Download Pension Certificate clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
