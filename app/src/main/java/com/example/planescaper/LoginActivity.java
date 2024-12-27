package com.example.planescaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText emailET, passwordET;
    private Button loginSignUpBtn, loginSignInBtn;
    private LinearLayout googleBtn;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private GoogleSignInClient client;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == RESULT_OK){
                try {
                    GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(o.getData()).getResult(ApiException.class);
                    firebaseAuthWithGoogle(account);
                }catch (ApiException e){
                    Toast.makeText(LoginActivity.this, "Sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

//        if (isLoggedIn) {
//            String userId = sharedPreferences.getString("userId", null);
//            Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
//            toMain.putExtra("userId", userId); // Pass userId to MainActivity
//            startActivity(toMain);
//            finish();
//            return;
//        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();

        emailET = findViewById(R.id.loginEmailET);
        passwordET = findViewById(R.id.editTextTextPassword);
        loginSignUpBtn = findViewById(R.id.loginSignUpBtn);
        loginSignInBtn = findViewById(R.id.loginSignInBtn);
        googleBtn = findViewById(R.id.loginGoogleBtn);

        loginSignUpBtn.setOnClickListener(v -> {
            Intent toRegister = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(toRegister);
        });

        loginSignInBtn.setOnClickListener(v -> {
            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();

            if (validateInputs(email, password)) {
                checkUserInFirebase(email, password);
            }
        });

        setupGoogleSignIn();

        googleBtn.setOnClickListener(v -> {
            Intent signInIntent = client.getSignInIntent();
            launcher.launch(signInIntent);
        });

    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        client = GoogleSignIn.getClient(this, options);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    String username = user.getDisplayName();
                    String userId = user.getUid();
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent toMain = new Intent(LoginActivity.this, MainActivity.class);

                    SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userId", userId);
                    editor.putString("name", username);
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();


                    startActivity(toMain);
                    finish();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailET.setError("Email is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordET.setError("Password is required");
            return false;
        }

        return true;
    }

    private void checkUserInFirebase(String email, String password) {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean userFound = false;
                String username = null;
                String userId = null;

                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String dbEmail = snapshot.child("email").getValue(String.class);
                    String dbPassword = snapshot.child("password").getValue(String.class);
                    String dbName = snapshot.child("name").getValue(String.class);
                    userId = snapshot.getKey();

                    if (dbEmail != null && dbPassword != null && dbEmail.equals(email) && dbPassword.equals(password)) {
                        userFound = true;
                        username = dbName;
                        break;
                    }
                }

                if (userFound) {
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    Intent toMain = new Intent(LoginActivity.this, MainActivity.class);
                    toMain.putExtra("name", username);

                    SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userId", userId);
                    editor.putString("name", username);
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    startActivity(toMain);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
