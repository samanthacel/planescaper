package com.example.planescaper.data;

import java.util.ArrayList;
import java.util.List;

public class OrderData {
    private static OrderData instance;
    private List<TourData> orderData;
    private OrderData() {
        orderData = new ArrayList<>();
    }
    public static OrderData getInstance() {
        if (instance == null) {
            instance = new OrderData();
        }
        return instance;
    }

    public List<TourData> getOrderData() {
        return orderData;
    }

    public void addOrder(TourData tour) {
        orderData.add(tour);
    }

}
