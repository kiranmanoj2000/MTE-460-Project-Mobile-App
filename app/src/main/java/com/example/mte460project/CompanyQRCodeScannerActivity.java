package com.example.mte460project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CompanyQRCodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private final String TAG = "scannerView";
    private ZXingScannerView mScannerView;
    private DatabaseReference ref;
    private SharedPreferences sharedPref;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        sharedPref = this.getSharedPreferences("com.example.mte460project", Context.MODE_PRIVATE);
        ref = FirebaseDatabase.getInstance().getReference();
        Toast.makeText(this, "Please scan in your company QR code", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        String companyId = rawResult.getText();

        connectToCompany(companyId);
        finish();
    }

    public void connectToCompany(String companyId){
        ref.child("companies").child(companyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("companyId", companyId);
                    editor.apply();
                    Intent myIntent = new Intent(CompanyQRCodeScannerActivity.this, MainActivity.class);
                    CompanyQRCodeScannerActivity.this.startActivity(myIntent);
                    finish();
                } else{
                    Toast.makeText(CompanyQRCodeScannerActivity.this, "Invalid QR code scanned", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
