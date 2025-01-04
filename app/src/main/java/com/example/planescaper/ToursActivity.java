package com.example.planescaper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planescaper.adapter.TourAdapter;
import com.example.planescaper.data.TourData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ToursActivity extends AppCompatActivity {

    private RecyclerView toursRV;
    private ProgressBar progressBar;
    private List<TourData> tourData = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;
    private EditText searchBar;
    private ImageView searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tours);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tours), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        navbar();

        toursRV = findViewById(R.id.toursRV);
        progressBar = findViewById(R.id.progressBar);
        searchBar = findViewById(R.id.tourSearchbar);
        searchBtn = findViewById(R.id.tourSearchBtn);

        TourAdapter adapter = new TourAdapter(this, tourData);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        toursRV.setLayoutManager(layoutManager);
        toursRV.setAdapter(adapter);

        String searchQuery = getIntent().getStringExtra("searchQuery");
        String category = getIntent().getStringExtra("category");

        if (searchQuery != null) {
            searchBar.setText(searchQuery);
            searchTours(searchQuery, adapter, progressBar);
        } else if (category != null) {
            fetchToursByCategory(category, adapter, progressBar);
        } else {
            fetchToursByCategory("All", adapter, progressBar);
        }

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchTours(s.toString().trim(), adapter, progressBar);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void navbar() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuHome:
                        Intent intentHome = new Intent(ToursActivity.this, MainActivity.class);
                        startActivity(intentHome);
                        return true;
                    case R.id.menuOrders:
                        Intent intentOrders = new Intent(ToursActivity.this, OrdersActivity.class);
                        startActivity(intentOrders);
                        return true;
                    case R.id.menuTours:
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void searchTours(String query, TourAdapter adapter, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tours");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tourData.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TourData tour = dataSnapshot.getValue(TourData.class);
                    if (tour != null &&
                            (tour.getName().toLowerCase().contains(query.toLowerCase()) ||
                                    tour.getCategory().toLowerCase().contains(query.toLowerCase()) ||
                                    tour.getLocation().toLowerCase().contains(query.toLowerCase()))) {
                        tourData.add(tour);
                    }
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (tourData.isEmpty()) {
                    Toast.makeText(ToursActivity.this, "No results found for \"" + query + "\"", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Log.e("ToursActivity", "Error searching tours: " + error.getMessage());
            }
        });
    }

    private void fetchToursByCategory(String category, TourAdapter adapter, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("tours");

        if ("All".equals(category)) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tourData.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        TourData tour = dataSnapshot.getValue(TourData.class);
                        if (tour != null) {
                            tourData.add(tour);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            databaseReference.orderByChild("category").equalTo(category).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tourData.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        TourData tour = dataSnapshot.getValue(TourData.class);
                        if (tour != null) {
                            tourData.add(tour);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }
}
