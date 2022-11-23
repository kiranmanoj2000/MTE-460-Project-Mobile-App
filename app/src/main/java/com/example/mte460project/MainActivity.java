package com.example.mte460project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String conveyorArray[];
    String dateArray[];

    List<DroppedPackage> droppedPackage;

    RecyclerView recyclerView;
    RecycleAdapter helperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinnerConveyor  = findViewById(R.id.spinner_conveyor);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // .child("specific string")
        DatabaseReference myRef = database.getReference("fallenPackageEvents");
        Context n = this;
        /*myRef.child("newPath").setValue("HELLO").addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(n,  e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(n, "SUCESS", Toast.LENGTH_LONG);
            }
        });*/

        recyclerView = findViewById(R.id.recyclerView);

        //conveyorArray = getResources().getStringArray(R.array.conveyorBeltId);
        //dateArray = getResources().getStringArray(R.array.createdDate);

        //RecycleAdapter recycleAdapter = new RecycleAdapter(this, conveyorArray, dateArray);
        //recyclerView.setAdapter(recycleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        droppedPackage = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    // DroppedPackage.class?
                    //DroppedPackage data = ds.getValue(DroppedPackage.class);
                    //droppedPackage.add(data);

                    //DroppedPackage data = new DroppedPackage();

                    String compId = ds.child("companyId").getValue(String.class);
                    String beltId = ds.child("coneyorBeltId").getValue(String.class);
                    Long dropDate = ds.child("createdDate").getValue(Long.class);
                    Log.d("TAG", "companyId: " + compId);
                    Log.d("TAG", "companyId: " + beltId);
                    Log.d("TAG", "companyId: " + dropDate);

                    DroppedPackage data = new DroppedPackage(compId, beltId, dropDate);
                    droppedPackage.add(data);
                }
                helperAdapter = new RecycleAdapter(droppedPackage);
                recyclerView.setAdapter(helperAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ConveyorDropdown, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinnerConveyor.setAdapter(adapter);
    }
}