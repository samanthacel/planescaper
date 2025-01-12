package com.example.planescaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.planescaper.data.TourData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class DetailActivity extends AppCompatActivity {

    ImageView backBtn, placeIV, minBtn, plusBtn;
    TextView placeTV,locationTV, dateTV, hotelTV, planeTV, guideTV, personTV, tourpriceTV, taxTV, personPriceTV, totalPriceTV, detailItineraryTV;
    Button bookBtn;
    private int person = 1;
    private int tourPrice, tax, personPrice, totalPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tourDetail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindId();

        backBtn.setOnClickListener(v -> finish());

        String tourJson = getIntent().getStringExtra("tourData");
        TourData tour = new Gson().fromJson(tourJson, TourData.class);

        Picasso.get().load(tour.getImageUrl()).into(placeIV);
        placeTV.setText(tour.getPlaceName());
        locationTV.setText(tour.getLocation());
        dateTV.setText(tour.getDate());
        hotelTV.setText(tour.getHotel());
        planeTV.setText(tour.getPlane());
        guideTV.setText(tour.getGuide());
        detailItineraryTV.setText(tour.getItinerary());

        updateData(tour);
        minBtn.setOnClickListener(v -> {
            if (person > 1) {
                person--;
                updateData(tour);
            }
        });

        plusBtn.setOnClickListener(v -> {
            person++;
            updateData(tour);
        });

        bookBtn.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);
            String userId = sharedPreferences.getString("userId", null);

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        DatabaseReference orderRef = userRef.child("orders");
                        String orderId = orderRef.push().getKey();

                        Log.d("DetailActivity", "Order ID: " + orderId);

                        if (orderId != null) {
                            orderRef.child(orderId).setValue(tour).addOnSuccessListener(aVoid -> {
                                Toast.makeText(DetailActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(DetailActivity.this, TicketActivity.class);
                                intent.putExtra("orderData", new Gson().toJson(tour));
                                startActivity(intent);
                            }).addOnFailureListener(e -> {
                                Toast.makeText(DetailActivity.this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            Toast.makeText(DetailActivity.this, "Failed to generate order ID", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(DetailActivity.this, "User profile not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(DetailActivity.this, "Error fetching user profile: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    private void bindId(){
        backBtn = findViewById(R.id.detailBackBtn);
        placeIV = findViewById(R.id.detailPlaceIV);
        placeTV = findViewById(R.id.detailPlaceTV);
        locationTV = findViewById(R.id.detailLocationTV);
        dateTV = findViewById(R.id.detailDateTV);
        hotelTV = findViewById(R.id.detailHotelTV);
        planeTV = findViewById(R.id.detailPlaneTV);
        guideTV = findViewById(R.id.detailGuideTV);
        detailItineraryTV = findViewById(R.id.detailItineraryTV);
        personTV = findViewById(R.id.detailPersonTV);
        tourpriceTV = findViewById(R.id.detailTourpriceTV);
        taxTV = findViewById(R.id.detailTaxTV);
        personPriceTV = findViewById(R.id.detailPersonPriceTV);
        totalPriceTV = findViewById(R.id.detailTotalPriceTV);
        bookBtn = findViewById(R.id.detailBookBtn);
        minBtn = findViewById(R.id.detailMinBtn);
        plusBtn = findViewById(R.id.detailPlusBtn);
    }

    private void updateData(TourData tour){
        personTV.setText(String.valueOf(person));
        tour.setPerson(person);

        tourPrice = tour.getPrice();
        tourpriceTV.setText(formatPrice(tourPrice));

        tax = tour.getTax();
        taxTV.setText(formatPrice(tax));

        personPrice = tour.getPersonPrice();
        personPriceTV.setText(formatPrice(personPrice));

        totalPrice = tour.getTotalPrice();
        totalPriceTV.setText(formatPrice(totalPrice));

    }

    private String formatPrice(int price) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return "Rp" + formatter.format(price);
    }

}