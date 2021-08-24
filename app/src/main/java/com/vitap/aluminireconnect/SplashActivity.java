package com.vitap.aluminireconnect;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (user == null) {
            new Handler().postDelayed(() -> {
                if(user==null){
                    Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        }else {
            if (!sharedPreferences.getBoolean("AllDetailsAvailable",false)){
                startActivity(new Intent(this,RegistrationActivity.class));
                finish();
            }else {
                Toast.makeText(this, "User login done and all details available", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                //send user to home activity
            }
        }


    }



}