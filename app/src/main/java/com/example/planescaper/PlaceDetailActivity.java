package com.example.planescaper;

import android.os.Bundle;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RatingBar;
import android.widget.Button;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlaceDetailActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView destinationName, location, dates, nights, hotel, airplane, guide, description, price;
    private ImageView imageView;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("trips/1");

        // Bind views
        destinationName = findViewById(R.id.destination_name);
        location = findViewById(R.id.location);
        imageView = findViewById(R.id.image);
        ratingBar = findViewById(R.id.rating_bar);
        dates = findViewById(R.id.dates);
        nights = findViewById(R.id.nights);
        hotel = findViewById(R.id.hotel);
        airplane = findViewById(R.id.airplane);
        guide = findViewById(R.id.guide);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);

        // Fetch data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    destinationName.setText(dataSnapshot.child("name").getValue(String.class));
                    location.setText(dataSnapshot.child("location").getValue(String.class));
                    ratingBar.setRating(dataSnapshot.child("rating").getValue(Float.class));
                    dates.setText(dataSnapshot.child("dates").getValue(String.class));
                    nights.setText(dataSnapshot.child("nights").getValue(String.class));
                    hotel.setText(dataSnapshot.child("hotel").getValue(String.class));
                    airplane.setText(dataSnapshot.child("airplane").getValue(String.class));
                    guide.setText(dataSnapshot.child("guide").getValue(String.class));
                    description.setText(dataSnapshot.child("description").getValue(String.class));
                    price.setText(dataSnapshot.child("price").getValue(String.class));

                    // Load image using Picasso
                    Picasso.get().load(dataSnapshot.child("imageUrl").getValue(String.class)).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }
}