package com.example.sainiksathimobile.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.sainiksathimobile.DashboardActivity;

import com.example.sainiksathimobile.database.AppDatabase;
import com.example.sainiksathimobile.databinding.ActivityLoginBinding;
import com.example.sainiksathimobile.model.User;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Load view binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getInstance(getApplicationContext());

        // ✅ Handle login click
        binding.buttonLogin.setOnClickListener(view -> {
            String email = binding.usernameEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();


            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.loading.setVisibility(View.VISIBLE);

            new Thread(() -> {
                User user = db.userDao().getUserByEmail(email);
                runOnUiThread(() -> {
                    binding.loading.setVisibility(View.GONE);

                    if (user != null && user.getPassword().equals(password)) {
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        intent.putExtra("userId", user.getId());
                        intent.putExtra("currentUser", user); // If needed
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }
}
