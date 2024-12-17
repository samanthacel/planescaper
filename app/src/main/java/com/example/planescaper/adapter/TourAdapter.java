package com.example.planescaper.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planescaper.R;
import com.example.planescaper.data.TourData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {
    Context context;
    List<TourData> tourData;

    public TourAdapter(Context context, List<TourData> tourData) {
        this.context = context;
        this.tourData = tourData;
    }

    @NonNull
    @Override
    public TourAdapter.TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_tours, parent, false);
        TourViewHolder holder = new TourViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TourAdapter.TourViewHolder holder, int position) {
        TourData tour = tourData.get(position);

        Picasso.get().load(tour.getImageUrl()).into(holder.placeIV);
        holder.ratingTV.setText(String.valueOf(tour.getRating()));
        holder.placeTV.setText(tour.getName());
        holder.locationTV.setText(tour.getLocation());
        holder.priceTV.setText(tour.getFormattedPrice());
        holder.dateTV.setText(tour.getDate());
    }

    @Override
    public int getItemCount() {
        return tourData.size();
    }

    public class TourViewHolder extends RecyclerView.ViewHolder {
        ImageView placeIV;
        TextView ratingTV, placeTV, locationTV, priceTV, dateTV;
        public TourViewHolder(@NonNull View itemView) {
            super(itemView);

            placeIV = itemView.findViewById(R.id.tourPlaceIV);
            ratingTV = itemView.findViewById(R.id.tourRatingTV);
            placeTV = itemView.findViewById(R.id.tourPlaceTV);
            locationTV = itemView.findViewById(R.id.tourLocationTV);
            priceTV = itemView.findViewById(R.id.tourPriceTV);
            dateTV = itemView.findViewById(R.id.tourDateTV);

        }
    }
}
