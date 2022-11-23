package com.example.mte460project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    String data1[], data2[];
    Context context;
    List<DroppedPackage> droppedPackageList;

    public RecycleAdapter(List<DroppedPackage> droppedPackageListA){
        this.droppedPackageList = droppedPackageListA;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater inflater = LayoutInflater.from(context);
        //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = inflater.inflate(R.layout.my_row, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DroppedPackage droppedPackage = droppedPackageList.get(position);

        holder.myText1.setText(droppedPackage.getConveyorBeltId());
        holder.myText2.setText(String.valueOf(droppedPackage.getCreatedDate()));
    }

    @Override
    public int getItemCount() {
        // myText1.length to return length of array
        return droppedPackageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.conveyorText);
            myText2 = itemView.findViewById(R.id.dateText);
        }
    }
}
