package com.example.planescaper.data;

import java.text.DecimalFormat;

public class TourData {
    private String id, placeName, imageUrl, location, hotel, plane, date, itinerary, guide, category;
    private float rating;
    private int price, person = 1, tax, personPrice, totalPrice;

    public TourData(){

    }

    public TourData(String placeName, String imageUrl,String location, String hotel, String plane, String date, String itinerary, String guide, String nights, String category, float rating, int price) {
        this.placeName = placeName;
        this.location = location;
        this.imageUrl = imageUrl;
        this.hotel = hotel;
        this.plane = plane;
        this.date = date;
        this.itinerary = itinerary;
        this.guide = guide;
        this.category = category;
        this.rating = rating;
        this.price = price;
    }

    public int getTax(){
        this.tax = (int)(this.price*0.12);
        return tax;
    }

    public int getPersonPrice(){
        this.personPrice = this.tax + this.price;
        return personPrice;
    }

    public int getTotalPrice() {
        this.totalPrice = this.personPrice * this.person;
        return totalPrice;
    }

    public String getName() {
        return placeName;
    }

    public void setName(String placeName) {
        this.placeName = placeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public String getPlane() {
        return plane;
    }

    public void setPlane(String plane) {
        this.plane = plane;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItinerary() {
        return itinerary;
    }

    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public String getFormattedPrice(int formattedPrice) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return "Rp" + formatter.format(price) + "/pax";
    }

}
