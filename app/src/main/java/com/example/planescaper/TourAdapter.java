package com.example.planescaper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TourAdapter extends RecyclerView.Adapter<TourAdapter.TourViewHolder> {
    private List<Tour> tours;

    public TourAdapter(List<Tour> tours) {
        this.tours = tours;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tour_item, parent, false);
        return new TourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {
        Tour tour = tours.get(position);
        holder.title.setText(tour.getName());
        holder.location.setText(tour.getLocation());
        holder.rating.setText(String.valueOf(tour.getRating()));
        holder.dates.setText(tour.getDates());
        holder.price.setText(tour.getPrice());

        Picasso.get()
                .load(tour.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return tours.size();
    }

    public static class TourViewHolder extends RecyclerView.ViewHolder {
        TextView title, location, rating, dates, price;
        ImageView image;

        public TourViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tourTitle);
            location = itemView.findViewById(R.id.tourLocation);
            rating = itemView.findViewById(R.id.tourRating);
            dates = itemView.findViewById(R.id.tourDates);
            price = itemView.findViewById(R.id.tourPrice);
            image = itemView.findViewById(R.id.tourImage);
        }
    }
}

