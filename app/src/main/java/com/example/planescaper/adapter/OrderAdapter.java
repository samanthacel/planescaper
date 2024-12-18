package com.example.planescaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planescaper.DetailActivity;
import com.example.planescaper.R;
import com.example.planescaper.TicketActivity;
import com.example.planescaper.data.TourData;
import com.google.gson.Gson;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    List<TourData> orderData;

    public OrderAdapter(Context context, List<TourData> orderData) {
        this.context = context;
        this.orderData = orderData;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_orders, parent, false);
        OrderViewHolder holder = new OrderViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        TourData order = orderData.get(position);

        holder.placeTV.setText(order.getName());
        holder.dateTV.setText(order.getDate());
        holder.personTV.setText(String.valueOf(order.getPerson() + " person"));
        holder.priceTV.setText(order.getFormattedPrice(order.getPrice()));

        holder.detailBtn.setOnClickListener(v->{
            Intent intent = new Intent(context, TicketActivity.class);
            intent.putExtra("orderData", new Gson().toJson(order));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView placeTV, dateTV, personTV, priceTV;
        Button detailBtn;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            placeTV = itemView.findViewById(R.id.orderPlaceTV);
            dateTV = itemView.findViewById(R.id.orderDateTV);
            personTV = itemView.findViewById(R.id.orderPersonTV);
            priceTV = itemView.findViewById(R.id.orderPriceTV);
            detailBtn = itemView.findViewById(R.id.orderDetailBtn);
        }
    }
}
