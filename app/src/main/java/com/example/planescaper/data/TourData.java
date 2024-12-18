package com.example.planescaper.data;

import java.text.DecimalFormat;

public class TourData {
    private String name, imageUrl, location, hotel, airplane, date, description, guide, nights, category;
    private float rating;
    private int price, person = 1, tax, personPrice, totalPrice;

    public TourData(){}

    public TourData(String name, String imageUrl, String location, String hotel, String airplane, String date, String description, String guide, String nights, String category, float rating, int price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.location = location;
        this.hotel = hotel;
        this.airplane = airplane;
        this.date = date;
        this.description = description;
        this.guide = guide;
        this.nights = nights;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAirplane() {
        return airplane;
    }

    public void setAirplane(String airplane) {
        this.airplane = airplane;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public String getNights() {
        return nights;
    }

    public void setNights(String nights) {
        this.nights = nights;
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
