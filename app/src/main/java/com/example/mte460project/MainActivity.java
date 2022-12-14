package com.example.mte460project;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mte460project.adapters.ConveyorBeltAdapter;
import com.example.mte460project.adapters.FallenPackageAdapter;
import com.example.mte460project.models.ConveyorBelt;
import com.example.mte460project.models.FallenPackageEvent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    DatePickerDialog startDatePickerDialog;
    DatePickerDialog endDatePickerDialog;

    private FirebaseAuth mAuth;

    private SharedPreferences sharedPref;

    RecyclerView recyclerView;

    FallenPackageAdapter fallenPackageAdapter;

    private Spinner conveyorBeltSpinner;
    private DatabaseReference dbRef;
    private String companyId;
    private String conveyorBeltId;


    private int setStartYear = Calendar.getInstance().get(Calendar.YEAR);
    private int setStartMonth  = Calendar.getInstance().get(Calendar.MONTH);
    private int setStartDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    private int setEndYear = Calendar.getInstance().get(Calendar.YEAR);
    private int setEndMonth  = Calendar.getInstance().get(Calendar.MONTH);
    private int setEndDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    private Date startDate;
    private Date endDate;

    private TextView startDateResTextView;
    private TextView endDateResTextView;

    private TextView companyNameTextView;
    private TextView numFallenPackagesTextView;



    // Declare the launcher at the top of your Activity/Fragment:
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                    // TODO: display an educational UI explaining to the user the features that will be enabled
                    //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                    //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                    //       If the user selects "No thanks," allow the user to continue without notifications.
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                } else {
                    // Directly ask for the permission
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                }
            }
        }
    }
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
            redirectToLogin();
            finish();
            return;
        }
        else{
            companyId = getCompanyId();

            // if they havent set up a company
            if(companyId.equals("")){
                Intent myIntent = new Intent(MainActivity.this, CompanyQRCodeScannerActivity.class);
                MainActivity.this.startActivity(myIntent);
                finish();
                return;
            }
        }

        askNotificationPermission();

        handleFCMTokens();


        Calendar c = Calendar.getInstance();
        c.set(setStartYear, setStartMonth, setStartDay, 0, 0);
        startDate = c.getTime();

        c.set(setEndYear, setEndMonth, setEndDay, 0, 0);
        endDate = c.getTime();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();

        setCompanyName();

        conveyorBeltSpinner = findViewById(R.id.spinner);


        populateDateFields();
        populateConveyorBeltList();

        populateFallenPackageEventsList();

    }

    public void onStartDateSelectClick(View view){
        startDatePickerDialog.show();
    }
    public void onEndDateSelectClick(View view){
        endDatePickerDialog.show();
    }

    public void populateDateFields(){
        startDateResTextView = (TextView) findViewById(R.id.startDateResTextView);
        endDateResTextView = (TextView) findViewById(R.id.endDateResTextView);

        String cur = setEndDay + "/" + (setEndMonth + 1) + "/" + setEndYear;
        startDateResTextView.setText(cur);
        endDateResTextView.setText(cur);

        startDatePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        setStartYear = year;
                        setStartMonth = monthOfYear;
                        setStartDay = dayOfMonth;
                        startDateResTextView.setText(setStartDay + "/" + (setStartMonth + 1) + "/" + setStartYear);
                        Calendar c = Calendar.getInstance();
                        c.set(setStartYear, setStartMonth, setStartDay, 0, 0);
                        startDate = c.getTime();

                        fetchFallenPackageEvents();
                    }

                }, setStartYear, setStartMonth, setStartDay);
        // cant select prev dates
        startDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        endDatePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        setEndYear = year;
                        setEndMonth = monthOfYear;
                        setEndDay = dayOfMonth;
                        endDateResTextView.setText(setEndDay + "/" + (setEndMonth + 1) + "/" + setEndYear);
                        Calendar c = Calendar.getInstance();
                        c.set(setEndYear, setEndMonth, setEndDay, 0, 0);
                        endDate = c.getTime();

                        fetchFallenPackageEvents();
                    }
                }, setEndYear, setEndMonth, setEndDay);
        // cant select prev dates
        endDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    public void fetchFallenPackageEvents(){
        ArrayList<FallenPackageEvent> fallenPackageEvents = new ArrayList<>();
        dbRef.child("fallenPackageEvents").orderByChild("companyId").equalTo(companyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fallenPackageEvents.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    FallenPackageEvent fallenPackageEvent = ds.getValue(FallenPackageEvent.class);
                    fallenPackageEvent.setFallenPackageEventId(ds.getKey());
                    // only process if the conveyorbeltid matches and within date range
                    if(!fallenPackageEvent.getConveyorBeltId().equals(conveyorBeltId) || !withinDateRange(fallenPackageEvent))
                        continue;

                    fallenPackageEvents.add(fallenPackageEvent);
                }
                numFallenPackagesTextView.setText("# Fallen Packages: " + fallenPackageEvents.size());
                fallenPackageAdapter = new FallenPackageAdapter(fallenPackageEvents);
                recyclerView.setAdapter(fallenPackageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void populateFallenPackageEventsList(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        numFallenPackagesTextView = findViewById(R.id.numFallenPackagesTextView);
        fetchFallenPackageEvents();
    }

    public boolean withinDateRange(FallenPackageEvent fallenPackageEvent){
        Date fallenDate = new Date(fallenPackageEvent.getCreatedDate());
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        Calendar fallenCal = Calendar.getInstance();

        startCal.setTime(startDate);
        endCal.setTime(endDate);
        fallenCal.setTime(fallenDate);

        if(isSameDay(startCal, fallenCal) || isSameDay(endCal, fallenCal))
            return true;

        return fallenDate.after(startDate) && fallenDate.before(endDate);
    }

    public boolean isSameDay(Calendar cal1, Calendar cal2){
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    public void redirectToLogin(){
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override protected void onStop()
    {
        super.onStop();
    }

    public void populateConveyorBeltList(){

        ArrayList<ConveyorBelt> conveyorBelts = new ArrayList<>();
        dbRef.child("conveyorBelts").orderByChild("companyId").equalTo(companyId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                conveyorBelts.clear();
                for(DataSnapshot item : snapshot.getChildren())
                {
                    String conveyorBeltId = item.getKey();
                    String companyId = item.child("companyId").getValue(String.class);
                    Long createdDate = item.child("createdDate").getValue(Long.class);
                    String name = item.child("name").getValue(String.class);

                    conveyorBelts.add(new ConveyorBelt(conveyorBeltId, companyId, createdDate, name));
                }
                conveyorBeltId = conveyorBelts.get(conveyorBelts.size()-1).getConveyorBeltId();
                ConveyorBeltAdapter adapter = new ConveyorBeltAdapter(MainActivity.this, conveyorBelts);
                conveyorBeltSpinner.setAdapter(adapter);
                conveyorBeltSpinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent,
                                                       View view, int position, long id)
                            {

                                // It returns the clicked item.
                                ConveyorBelt clickedItem = (ConveyorBelt)
                                        parent.getItemAtPosition(position);
                                conveyorBeltId = clickedItem.getConveyorBeltId();
                                fetchFallenPackageEvents();

                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent)
                            {
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void handleFCMTokens(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("CODE", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        if(!FCMTokenExists()){
                            pushFCMTokenToDB(token, getCompanyId());
                            sharedPref.edit().putString("fcmToken", token).apply();
                        }

                    }
                });
    }

    public boolean FCMTokenExists(){
        return !sharedPref.getString("fcmToken", "").equals("");
    }

    public void pushFCMTokenToDB(String FCMToken, String companyId){
        FirebaseDatabase.getInstance().getReference().child("companies").child(companyId).child("tokens").child(FCMToken).setValue(true);
    }

    public String getCompanyId(){

        return sharedPref.getString("companyId", "");
    }

    public void signOut(View v){
        FirebaseAuth.getInstance().signOut();
        redirectToLogin();
        finish();
    }

    public void setCompanyName(){
        companyNameTextView = (TextView) findViewById(R.id.companyName);
        dbRef.child("companies").child(companyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                companyNameTextView.setText(((HashMap<?, ?>) snapshot.getValue()).get("name").toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}