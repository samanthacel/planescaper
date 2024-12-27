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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.planescaper.data.UserData;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameET, emailET, passwordET;
    private Button registerSigninBtn, registerSignupBtn;
    private LinearLayout googleBtn;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private GoogleSignInClient client;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == RESULT_OK){
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(o.getData());
                try {
                    GoogleSignInAccount signInAccount = task.getResult(ApiException.class);
                    AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                auth = FirebaseAuth.getInstance();
                                nameET.setText(auth.getCurrentUser().getDisplayName());
                                emailET.setText(auth.getCurrentUser().getEmail());

                                FirebaseUser user = auth.getCurrentUser();
                                String userName = user.getDisplayName();
                                String email = user.getEmail();
                                String password = passwordET.getText().toString().trim();

                                googleSaveUser(userName, email, password);
                            }else {
                                Toast.makeText(RegisterActivity.this, "Sign up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    });

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

        nameET = findViewById(R.id.registerNameET);
        emailET = findViewById(R.id.registerEmailET);
        passwordET = findViewById(R.id.registerPasswordET);
        registerSigninBtn = findViewById(R.id.registerSignInBtn);
        registerSignupBtn = findViewById(R.id.registerSignUpBtn);
        googleBtn = findViewById(R.id.registerGoogleBtn);

        registerSigninBtn.setOnClickListener(v -> {
            Intent toLogin = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(toLogin);
        });

        registerSignupBtn.setOnClickListener(v -> {
            String name = nameET.getText().toString().trim();
            String email = emailET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();

            if (validateInputs(name, email, password)) {
                saveUserToFirebase(name, email, password);
            }
        });

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.client_id)).requestEmail().build();
        client = GoogleSignIn.getClient(RegisterActivity.this, options);
        auth = FirebaseAuth.getInstance();
        googleBtn.setOnClickListener(v -> {
            Intent intent = client.getSignInIntent();
            launcher.launch(intent);
        });
    }

    private boolean validateInputs(String name, String email, String password) {
        if (TextUtils.isEmpty(name)) {
            nameET.setError("Name is required");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            emailET.setError("Email is required");
            return false;
        }

        if (!email.endsWith("@gmail.com")) {
            emailET.setError("Email must end with @gmail.com");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordET.setError("Password is required");
            return false;
        }

        if (password.length() < 6) {
            passwordET.setError("Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    private void saveUserToFirebase(String name, String email, String password) {
        databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(RegisterActivity.this, "Email already registered, please login", Toast.LENGTH_SHORT).show();
                    Intent toLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(toLogin);
                    finish();
                } else {
                    String userId = databaseReference.push().getKey();
                    UserData user = new UserData(userId, name, email, password);

                    if (userId != null) {
                        databaseReference.child(userId).setValue(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();

                                    SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("userId", userId);
                                    editor.putString("name", name);
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.apply();


                                    Intent toMain = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(toMain);
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Error checking user: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void googleSaveUser(String name, String email, String password) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(RegisterActivity.this, "Authentication error. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = firebaseUser.getUid();
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(RegisterActivity.this, "Email already registered, please login", Toast.LENGTH_SHORT).show();
                    Intent toLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(toLogin);
                    finish();
                } else {
                    UserData user = new UserData(userId, name, email, password);
                    databaseReference.child(userId).setValue(user)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();

                                SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("userId", userId);
                                editor.putString("name", name);
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();


                                Intent toMain = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(toMain);
                                finish();
                            })
                            .addOnFailureListener(e -> Toast.makeText(RegisterActivity.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RegisterActivity.this, "Error checking user: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
