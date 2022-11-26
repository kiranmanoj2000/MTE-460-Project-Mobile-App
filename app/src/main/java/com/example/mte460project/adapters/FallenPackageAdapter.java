package com.example.mte460project.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mte460project.R;
import com.example.mte460project.models.FallenPackageEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FallenPackageAdapter extends RecyclerView.Adapter<FallenPackageAdapter.FallenPackageEventViewHolder> {

    ArrayList<FallenPackageEvent> fallenPackageEvents;

    public FallenPackageAdapter(ArrayList<FallenPackageEvent> fallenPackageEventList) {
        this.fallenPackageEvents = fallenPackageEventList;
    }

    @Override
    public void onBindViewHolder(@NonNull FallenPackageEventViewHolder holder, int position) {
        FallenPackageEvent model = this.fallenPackageEvents.get(position);
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy  hh:mm a", java.util.Locale.getDefault());
        String date = format.format(new Date(model.getCreatedDate()));

        holder.fallenPackageEventId.setText("Package fallen");


        holder.createdDate.setText(date);

    }

    @NonNull
    @Override
    public FallenPackageEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fallen_package_event_item, parent, false);
        return new FallenPackageAdapter.FallenPackageEventViewHolder(view);

    }

    @Override
    public int getItemCount() {
        // myText1.length to return length of array
        return this.fallenPackageEvents.size();
    }

    class FallenPackageEventViewHolder extends RecyclerView.ViewHolder {
        TextView createdDate, fallenPackageEventId;

        public FallenPackageEventViewHolder(@NonNull View itemView) {
            super(itemView);

            createdDate = itemView.findViewById(R.id.fallenPackageEventCreatedDate);
            fallenPackageEventId = itemView.findViewById(R.id.fallenPackageEventId);
        }


    }
}