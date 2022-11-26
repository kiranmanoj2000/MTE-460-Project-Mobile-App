package com.example.mte460project.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mte460project.R;
import com.example.mte460project.RecycleAdapter;
import com.example.mte460project.models.FallenPackageEvent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class FallenPackageAdapter extends RecyclerView.Adapter<FallenPackageAdapter.FallenPackageEventViewHolder> {

    ArrayList<FallenPackageEvent> fallenPackageEvents;
public FallenPackageAdapter(ArrayList<FallenPackageEvent> fallenPackageEventList) {
        this.fallenPackageEvents = fallenPackageEventList;
}

// Function to bind the view in Card view(here
// "person.xml") iwth data in
// model class(here "person.class")
@Override
public void onBindViewHolder(@NonNull FallenPackageEventViewHolder holder, int position)
        {
            FallenPackageEvent model = this.fallenPackageEvents.get(position);

            holder.fallenPackageEventId.setText(model.getFallenPackageEventId());


            holder.createdDate.setText(model.getCreatedDate().toString());

        }

// Function to tell the class about the Card view (here
// "person.xml")in
// which the data will be shown
    @NonNull
    @Override
    public FallenPackageEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fallen_package_event_item, parent, false);
            return new FallenPackageAdapter.FallenPackageEventViewHolder(view);
        }

    @Override
    public int getItemCount() {
        // myText1.length to return length of array
        return this.fallenPackageEvents.size();
    }

// Sub Class to create references of the views in Crad
// view (here "person.xml")
class FallenPackageEventViewHolder extends RecyclerView.ViewHolder {
    TextView createdDate, fallenPackageEventId;

    public FallenPackageEventViewHolder(@NonNull View itemView) {
        super(itemView);

        createdDate = itemView.findViewById(R.id.fallenPackageEventCreatedDate);
        fallenPackageEventId = itemView.findViewById(R.id.fallenPackageEventId);
    }


}
}