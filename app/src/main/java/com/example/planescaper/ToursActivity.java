package com.example.planescaper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planescaper.adapter.PopularTourAdapter;
import com.example.planescaper.adapter.TourAdapter;
import com.example.planescaper.data.TourData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ToursActivity extends AppCompatActivity {

    RecyclerView toursRV;
    ProgressBar progressBar;
    List<TourData> tourData = new ArrayList<>();
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        TourAdapter adapter = new TourAdapter(this, tourData);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        toursRV.setLayoutManager(layoutManager);
        toursRV.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("tours");
        loadFirebaseData();
    }

    public void navbar(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        Intent intentHome = new Intent(ToursActivity.this, MainActivity.class);
                        startActivity(intentHome);
                        return true;
                    case R.id.menuOrders:
                        Intent intentOrders = new Intent(ToursActivity.this, OrdersActivity.class);
                        startActivity(intentOrders);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void loadFirebaseData(){
        progressBar.setVisibility(View.VISIBLE);

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
                toursRV.getAdapter().notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ToursActivity", "Database error: " + error.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}