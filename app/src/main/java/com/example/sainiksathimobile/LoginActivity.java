package com.example.sainiksathimobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sainiksathimobile.database.AppDatabase;
import com.example.sainiksathimobile.model.User;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button buttonLogin;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = AppDatabase.getInstance(getApplicationContext());

        inputEmail = findViewById(R.id.usernameEditText); // ✅ Matches the ID in your XML

        inputPassword = findViewById(R.id.passwordEditText);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter both fields", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(() -> {
            User existing = db.userDao().getUserByEmail("admin@sainik.in");
            if (existing == null) {
                User u = new User();
                u.setFullName("Admin Sainik");
                u.setEmail("admin@sainik.in");
                u.setPassword("admin123"); // Plaintext for now
                db.userDao().insertUser(u);
            }
        }).start();


        Executors.newSingleThreadExecutor().execute(() -> {
            User user = AppDatabase.getInstance(this).userDao().getUserByEmail(email);
            if (user != null && user.getPassword().equals(password)) {
                // ✅ Successful login
                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(i);
                finish(); // Optional: close LoginActivity
            } else {
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Invalid login", Toast.LENGTH_SHORT).show());
            }
        });
    }
}