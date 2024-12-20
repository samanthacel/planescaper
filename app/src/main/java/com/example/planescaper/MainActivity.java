package com.example.planescaper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.google.firebase.database.FirebaseDatabase;
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

        databaseReference = FirebaseDatabase.getInstance().getReference("tours");
        fetchToursFromFirebase(adapter);

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