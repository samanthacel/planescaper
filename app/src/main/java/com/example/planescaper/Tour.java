package com.example.planescaper;


import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Tour {
    private String name;
    private String location;
    private double rating;
    private String imageUrl;
    private String dates;
    private String nights;
    private String hotel;
    private String airplane;
    private String guide;
    private String description;
    private String price;

    public Tour() {
        // Default constructor required for Firebase
    }

    // Getters and setters
    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getRating() { return rating; }
    public String getImageUrl() { return imageUrl; }
    public String getDates() { return dates; }
    public String getNights() { return nights; }
    public String getHotel() { return hotel; }
    public String getAirplane() { return airplane; }
    public String getGuide() { return guide; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
}
