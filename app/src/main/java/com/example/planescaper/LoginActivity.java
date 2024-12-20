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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        emailEditText = findViewById(R.id.loginEmailET);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        Button loginSignUpBtn = findViewById(R.id.loginSignUpBtn);
        Button loginSignInBtn = findViewById(R.id.loginSignInBtn);


        loginSignUpBtn.setOnClickListener(v -> {
            Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(toRegister);
        });

        loginSignInBtn.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (validateInputs(email, password)) {
                checkUserInFirebase(email, password);
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return false;
        }

        return true;
    }

    private void checkUserInFirebase(String email, String password) {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean userFound = false;
                String username = null;

                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String dbEmail = snapshot.child("email").getValue(String.class);
                    String dbPassword = snapshot.child("password").getValue(String.class);
                    String dbName = snapshot.child("name").getValue(String.class);

                    if (dbEmail != null && dbPassword != null && dbEmail.equals(email) && dbPassword.equals(password)) {
                        userFound = true;
                        username = dbName; // Get the name from the database
                        break;
                    }
                }

                if (userFound) {
                    // Save username in SharedPreferences
                    getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                            .edit()
                            .putString("username", username)
                            .apply();

                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(toMain);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
