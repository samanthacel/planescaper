package com.example.planescaper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button loginSignupBtn = findViewById(R.id.loginSignUpBtn);
        Button loginSigninBtn = findViewById(R.id.loginSignInBtn);

        loginSignupBtn.setOnClickListener(v -> {
            Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(toRegister);
        });

        loginSigninBtn.setOnClickListener(v -> {
            Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(toMain);
        });

    }
}