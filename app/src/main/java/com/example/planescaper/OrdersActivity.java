package com.example.planescaper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.planescaper.adapter.OrderAdapter;
import com.example.planescaper.data.OrderData;
import com.example.planescaper.data.TourData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    RecyclerView ordersRV;
    ProgressBar progressBar;
    List<TourData> orderData = new ArrayList<>();
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.orders), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        navbar();

        ordersRV = findViewById(R.id.ordersRV);
        progressBar = findViewById(R.id.progressBar);

        orderData = OrderData.getInstance().getOrderData();

        OrderAdapter adapter = new OrderAdapter(this, orderData);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ordersRV.setLayoutManager(layoutManager);
        ordersRV.setAdapter(adapter);

//        databaseReference = FirebaseDatabase.getInstance().getReference("trips");
//        progressBar.setVisibility(View.VISIBLE);
    }

    public void navbar(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        Intent intentHome = new Intent(OrdersActivity.this, MainActivity.class);
                        startActivity(intentHome);
                        return true;
                    case R.id.menuTours:
                        Intent intentTours = new Intent(OrdersActivity.this, ToursActivity.class);
                        startActivity(intentTours);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

}