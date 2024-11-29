package com.example.planescaper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button registerSigninBtn = findViewById(R.id.registerSignInBtn);
        Button registerSignupBtn = findViewById(R.id.registerSignUpBtn);

        registerSigninBtn.setOnClickListener(v -> {
            Intent toLogin = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(toLogin);
        });

        registerSignupBtn.setOnClickListener(v -> {
            Intent toMain = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(toMain);
        });

    }
}