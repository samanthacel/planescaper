package com.example.planescaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planescaper.adapter.PopularTourAdapter;
import com.example.planescaper.data.TourData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView popularRV;
    private ProgressBar progressBar;
    private ImageView logoutBtn;
    private List<TourData> tourData = new ArrayList<>();
    private LinearLayout mountainMenu, beachMenu, forestMenu, desertMenu, citiesMenu;
    private EditText searchBar;
    private ImageView searchBtn;
    private DatabaseReference databaseReference;
    private BottomNavigationView bottomNavigationView;
    private TextView mainNameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        navbar();
        mainNameTV = findViewById(R.id.mainNameTV);
        logoutBtn = findViewById(R.id.mainLogoutIV);
        mountainMenu = findViewById(R.id.mainMountainMenu);
        beachMenu = findViewById(R.id.mainBeachMenu);
        forestMenu = findViewById(R.id.mainForestMenu);
        desertMenu = findViewById(R.id.mainDesertMenu);
        citiesMenu = findViewById(R.id.mainCitiesMenu);
        searchBar = findViewById(R.id.mainSearchbar);
        searchBtn = findViewById(R.id.mainSearchBtn);


        SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", null);

        String firstName = name.split(" ")[0];
        mainNameTV.setText("Hey, " + firstName +"!");

        popularRV = findViewById(R.id.mainPopularRV);
        progressBar = findViewById(R.id.progressBar);

        PopularTourAdapter adapter = new PopularTourAdapter(this, tourData);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        popularRV.setLayoutManager(layoutManager);
        popularRV.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("tours");
        fetchToursFromFirebase(adapter);

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                searchTour();
                return true;
            }
            return false;
        });

        searchBtn.setOnClickListener(v -> searchTour());

        mountainMenu.setOnClickListener(v -> navigateToCategory("Mountain"));
        beachMenu.setOnClickListener(v -> navigateToCategory("Beach"));
        forestMenu.setOnClickListener(v -> navigateToCategory("Forest"));
        desertMenu.setOnClickListener(v -> navigateToCategory("Desert"));
        citiesMenu.setOnClickListener(v -> navigateToCategory("Cities"));
    }

    private void searchTour(){
        String query = searchBar.getText().toString().trim();
        if (!query.isEmpty()) {
            Intent searchIntent = new Intent(MainActivity.this, ToursActivity.class);
            searchIntent.putExtra("searchQuery", query);
            startActivity(searchIntent);
        } else {
            Toast.makeText(MainActivity.this, "Enter tour or place", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToCategory(String category) {
        Intent intent = new Intent(MainActivity.this, ToursActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    public void navbar(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuTours:
                        Intent intentTours = new Intent(MainActivity.this, ToursActivity.class);
                        intentTours.putExtra("category", "All");
                        startActivity(intentTours);
                        return true;
                    case R.id.menuOrders:
                        Intent intentOrders = new Intent(MainActivity.this, OrdersActivity.class);
                        startActivity(intentOrders);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void fetchToursFromFirebase(PopularTourAdapter adapter) {
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.orderByChild("rating").limitToLast(5).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tourData.clear();

                List<TourData> tempList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TourData tour = dataSnapshot.getValue(TourData.class);
                    if (tour != null) {
                        tempList.add(tour);
                    }
                }

                for (int i = tempList.size() - 1; i >= 0; i--) {
                    tourData.add(tempList.get(i));
                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Log.e("MainActivity", "Error fetching data: " + error.getMessage());
            }
        });
    }
}