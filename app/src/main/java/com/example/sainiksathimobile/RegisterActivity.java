package com.example.sainiksathimobile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sainiksathimobile.database.AppDatabase;
import com.example.sainiksathimobile.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextRank, editTextEmail, editTextPassword;
    private Button buttonRegister;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = AppDatabase.getInstance(getApplicationContext());

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextRank = findViewById(R.id.editTextRank);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(view -> {
            String fullName = editTextFullName.getText().toString().trim();
            String rank = editTextRank.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (fullName.isEmpty() || rank.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User();
            user.setFullName(fullName);
            user.setRank(rank);
            user.setEmail(email);
            user.setPassword(password); // Hash later if needed

            new Thread(() -> {
                db.userDao().insertUser(user);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to login
                });
            }).start();
        });
    }
}
