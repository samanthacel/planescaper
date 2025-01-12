package com.example.planescaper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planescaper.R;
import com.example.planescaper.data.WondersData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WondersAdapter extends RecyclerView.Adapter<WondersAdapter.WondersViewHolder> {
    Context context;
    List<WondersData> wondersData;
    public WondersAdapter(Context context, List<WondersData> wondersData) {
        this.context = context;
        this.wondersData = wondersData;
    }

    @NonNull
    @Override
    public WondersAdapter.WondersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_wonders, parent, false);
        WondersViewHolder holder = new WondersViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WondersAdapter.WondersViewHolder holder, int position) {
        WondersData wonder = wondersData.get(position);

        Picasso.get().load(wonder.getImageUrl()).into(holder.wondersIV);
    }

    @Override
    public int getItemCount() {
        return wondersData.size();
    }

    public class WondersViewHolder extends RecyclerView.ViewHolder {
        ImageView wondersIV;
        public WondersViewHolder(@NonNull View itemView) {
            super(itemView);
            wondersIV = itemView.findViewById(R.id.wondersIV);
        }
    }
}
