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

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(() -> {
            Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        },2000);
    }

    private void isAllDetailsAvailable(){
        FirebaseFirestore.getInstance().collection("Users")
                .document(user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()){
                        //checking all fields are available
                        try {
                            String Name = snapshot.get("FirstName").toString();
                            String FatherName = snapshot.get("FatherName").toString();
                            String Placed = snapshot.get("Placed").toString();
                            sharedPreferences.edit().putBoolean("AllDetailsAvailable",true).apply();
                            //send user to Feed activity

                        }catch (Exception e){
                            Log.e("Details error : ",e.toString());
                            Intent intent=new Intent(SplashActivity.this,RegistrationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Intent intent=new Intent(SplashActivity.this,RegistrationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }


}