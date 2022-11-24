package com.example.mte460project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        emailField = (EditText)findViewById(R.id.editTextTextEmailAddressSignUp);
        passwordField = (EditText)findViewById(R.id.editTextTextPasswordSignUp);
        mAuth = FirebaseAuth.getInstance();
        sharedPref = this.getSharedPreferences("com.example.mte460project", Context.MODE_PRIVATE);
    }


    public void signup(View v){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Signup Activity", "createUserWithEmail:failure");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignupActivity.this, "Authentication SUCCESS.",
                                    Toast.LENGTH_SHORT).show();

                            // take them to the page to connect to a company

//                            Intent myIntent = new Intent(SignupActivity.this, CompanyQRCodeScannerActivity.class);
//                            SignupActivity.this.startActivity(myIntent);
                            Intent myIntent = new Intent(SignupActivity.this, MainActivity.class);
                            SignupActivity.this.startActivity(myIntent);

                            //
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Signup Activity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    public void redirectToLogin(View v){
        Intent myIntent = new Intent(SignupActivity.this, LoginActivity.class);
        SignupActivity.this.startActivity(myIntent);
    }
}