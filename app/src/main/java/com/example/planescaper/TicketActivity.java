package com.example.planescaper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.planescaper.data.TourData;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class TicketActivity extends AppCompatActivity {

    ImageView backBtn, placeIV, qrCodeIV;
    TextView placeTV, locationTV, dateTV, hotelTV, planeTV, guideTV, personTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ticket), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindId();

        backBtn.setOnClickListener(v -> {
            Intent toOrder = new Intent(TicketActivity.this, OrdersActivity.class);
            startActivity(toOrder);
        });

        String tourJson = getIntent().getStringExtra("orderData");
        TourData tour = new Gson().fromJson(tourJson, TourData.class);

        Picasso.get().load(tour.getImageUrl()).into(placeIV);
        placeTV.setText(tour.getName());
        locationTV.setText(tour.getLocation());
        dateTV.setText(tour.getDate());
        hotelTV.setText(tour.getHotel());
        planeTV.setText(tour.getAirplane());
        guideTV.setText(tour.getGuide());
        personTV.setText(String.valueOf(tour.getPerson() + " person"));

        generateQRCode(qrCodeIV);

    }

    private void bindId(){
        backBtn = findViewById(R.id.ticketBackBtn);
        placeIV = findViewById(R.id.ticketPlaceIV);
        placeTV = findViewById(R.id.ticketPlaceTV);
        locationTV = findViewById(R.id.ticketLocationTV);
        dateTV = findViewById(R.id.ticketDateTV);
        hotelTV = findViewById(R.id.ticketHotelTV);
        planeTV = findViewById(R.id.ticketPlaneTV);
        guideTV = findViewById(R.id.ticketGuideTV);
        personTV = findViewById(R.id.ticketPersonTV);
        qrCodeIV = findViewById(R.id.qrCodeImage);
    }

    private void generateQRCode(ImageView qrCodeImage) {
        String randomData = UUID.randomUUID().toString();
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap(randomData, BarcodeFormat.QR_CODE, 300, 300);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


}