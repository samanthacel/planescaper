package com.example.planescaper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        nameEditText = findViewById(R.id.registerNameET);
        emailEditText = findViewById(R.id.registerEmailET);
        passwordEditText = findViewById(R.id.registerPasswordET);
        Button registerSigninBtn = findViewById(R.id.registerSignInBtn);
        Button registerSignupBtn = findViewById(R.id.registerSignUpBtn);

        registerSigninBtn.setOnClickListener(v -> {
            Intent toLogin = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(toLogin);
        });

        registerSignupBtn.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateInputs(name, email, password)) {
                saveUserToFirebase(name, email, password);
            }
        });
    }

    private boolean validateInputs(String name, String email, String password) {
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return false;
        }

        if (!email.endsWith("@gmail.com")) {
            emailEditText.setError("Email must end with @gmail.com");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    private void saveUserToFirebase(String name, String email, String password) {

        String userId = databaseReference.push().getKey();

        User user = new User(userId, name, email, password);

        if (userId != null) {
            databaseReference.child(userId).setValue(user)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                        Intent toLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(toLogin);
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }

    // User class
    public static class User {
        public String id;
        public String name;
        public String email;
        public String password;

        public User() {
            
        }

        public User(String id, String name, String email, String password) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.password = password;
        }
    }
}
