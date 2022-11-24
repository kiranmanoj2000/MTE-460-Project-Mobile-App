package com.example.mte460project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private SharedPreferences sharedPref;

    String conveyorArray[];
    String dateArray[];

    List<DroppedPackage> droppedPackage;

    RecyclerView recyclerView;
    RecycleAdapter helperAdapter;

    private Spinner spinner;
    private ArrayList<ConveyorName> conveyorname = new ArrayList<>();
    private ArrayList<String> conveyornamestring = new ArrayList<>();
    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        sharedPref = this.getSharedPreferences("com.example.mte460project", Context.MODE_PRIVATE);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // redirect to login
        if(currentUser == null){
            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);
        }
        else{
            // have they been connected to a company?
//            if(sharedPref.getString("companyId", "").equals("")){
//                Intent myIntent = new Intent(MainActivity.this, CompanyQRCodeScannerActivity.class);
//                MainActivity.this.startActivity(myIntent);
//            }
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // .child("specific string")
        DatabaseReference myRef = database.getReference("fallenPackageEvents");
        Context n = this;
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

        //ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ConveyorDropdown, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //spinnerConveyor.setAdapter(adapter);

        // Show conveyor names in spinner
        spinner = findViewById(R.id.spinner);
        showDataSpinner();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void showDataSpinner()
    {
        dbRef.child("conveyorBelts").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                conveyorname.clear();
                conveyornamestring.clear();
                for(DataSnapshot item : snapshot.getChildren())
                {
                    String compId = item.child("companyId").getValue(String.class);
                    Long cdate = item.child("createdDate").getValue(Long.class);
                    String cname = item.child("companyId").getValue(String.class);

                    ConveyorName conveyordata = new ConveyorName(compId, cdate, cname);
                    conveyorname.add(conveyordata);
                    conveyornamestring.add(cname);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, conveyornamestring);
                spinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}