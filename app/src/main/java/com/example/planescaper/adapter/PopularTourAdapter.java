package com.example.planescaper.adapter;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class PopularTourAdapter extends RecyclerView.Adapter<PopularTourAdapter.PopularTourViewHolder> {

    Context context;
    List<TourData> popularData;


    public PopularTourAdapter(Context context, List<TourData> popularData) {
        this.context = context;
        this.popularData = popularData;
    }

    @NonNull
    @Override
    public PopularTourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_popular, parent, false);
        PopularTourViewHolder holder = new PopularTourViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PopularTourViewHolder holder, int position) {
        TourData tour = popularData.get(position);

        Picasso.get().load(tour.getImageUrl()).into(holder.placeIV);
        holder.ratingTV.setText(String.valueOf(tour.getRating()));
        holder.placeTV.setText(tour.getName());
        holder.locationTV.setText(tour.getLocation());
        holder.priceTV.setText(tour.getFormattedPrice(tour.getPrice()));
    }

    @Override
    public int getItemCount() {
        return popularData.size();
    }

    public void searchDataList(ArrayList<TourData> searchList){
        popularData = searchList;
        notifyDataSetChanged();
    }

    public class PopularTourViewHolder extends RecyclerView.ViewHolder {
        ImageView placeIV;
        TextView ratingTV, placeTV, locationTV, priceTV;
        public PopularTourViewHolder(@NonNull View itemView) {
            super(itemView);

            placeIV = itemView.findViewById(R.id.popularPlaceIV);
            ratingTV = itemView.findViewById(R.id.popularRatingTV);
            placeTV = itemView.findViewById(R.id.popularPlaceTV);
            locationTV = itemView.findViewById(R.id.popularLocationTV);
            priceTV = itemView.findViewById(R.id.popularPriceTV);
        }
    }
}
