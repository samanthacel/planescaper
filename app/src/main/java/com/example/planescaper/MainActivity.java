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
import com.example.planescaper.data.TourData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    RecyclerView popularRV;
    ProgressBar progressBar;
    List<TourData> tourData = new ArrayList<>();
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    BottomNavigationView bottomNavigationView;

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

        popularRV = findViewById(R.id.mainPopularRV);
        progressBar = findViewById(R.id.progressBar);

        PopularTourAdapter adapter = new PopularTourAdapter(this, tourData);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        popularRV.setLayoutManager(layoutManager);
        popularRV.setAdapter(adapter);

//        databaseReference = FirebaseDatabase.getInstance().getReference("trips");
//        progressBar.setVisibility(View.VISIBLE);

        loadData();
//        fetchToursFromFirebase(adapter);
    }

    public void navbar(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuTours:
                        Intent intentTours = new Intent(MainActivity.this, ToursActivity.class);
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

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tourData.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TourData tour = dataSnapshot.getValue(TourData.class);

                    if (tour != null) {
                        Log.d("MainActivity", "Fetched tour: " + tour.getName());
                        tourData.add(tour);
                    }
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

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tourData.addAll(initPopularData());
                popularRV.getAdapter().notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        }, 500);
    }

    private List<TourData> initPopularData() {
        tourData = new ArrayList<>();

        tourData.add(new TourData(
                "Mount Bromo",
                "https://images.unsplash.com/photo-1580137491561-92d55f5ab0d1",
                "East Java, Indonesia",
                "Hotel Le Meurice",
                "Air France",
                "2024-06-15",
                "Explore the iconic Mount Bromo, famous for its sunrise and volcanic activity.",
                "Jean Dupont",
                "3",
                "Adventure",
                4.8f,
                1200000,
                0 // Person set to 0
        ));

        tourData.add(new TourData(
                "Eiffel Tower",
                "https://images.unsplash.com/photo-1555685811-e5729d4b8b09",
                "Paris, France",
                "Hotel de Russie",
                "Alitalia",
                "2024-07-20",
                "Discover the grandeur of the Eiffel Tower and enjoy the beauty of Paris.",
                "Maria Rossi",
                "2",
                "Landmarks",
                4.7f,
                800000,
                0 // Person set to 0
        ));

        tourData.add(new TourData(
                "Great Wall of China",
                "https://images.unsplash.com/photo-1536741534982-cfb3751302f5",
                "Beijing, China",
                "The Peninsula Beijing",
                "China Airlines",
                "2024-08-05",
                "Walk along the historic Great Wall and explore ancient Chinese culture.",
                "Wang Wei",
                "4",
                "Cultural",
                4.9f,
                1500000,
                0 // Person set to 0
        ));

        tourData.add(new TourData(
                "Machu Picchu",
                "https://images.unsplash.com/photo-1575936123450-500ad5d1e300",
                "Cusco, Peru",
                "Hotel Inkaterra",
                "LATAM Airlines",
                "2024-09-10",
                "Visit the ancient Incan city of Machu Picchu and enjoy breathtaking views.",
                "Carlos Martinez",
                "5",
                "Historical",
                4.6f,
                700000,
                0 // Person set to 0
        ));

        tourData.add(new TourData(
                "Sydney Opera House",
                "https://images.unsplash.com/photo-1588961844214-2cd11001b04a",
                "Sydney, Australia",
                "Shangri-La Sydney",
                "Qantas Airways",
                "2024-10-25",
                "Enjoy a tour of the Sydney Opera House and the stunning harbor.",
                "Emma Brown",
                "3",
                "Theater",
                4.8f,
                600000,
                0 // Person set to 0
        ));

        return tourData;
    }



}