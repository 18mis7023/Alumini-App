package com.vitap.aluminireconnect;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.vitap.aluminireconnect.Fragments.AccademicDetailsFragment;
import com.vitap.aluminireconnect.Fragments.RegistrationFragment;


public class RegistrationActivity extends AppCompatActivity {
    private MaterialCardView backCard;
    private TextView txtRegisterHere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        backCard=findViewById(R.id.back_card);
        txtRegisterHere=findViewById(R.id.txt_register_here);


        backCard.setOnClickListener(view -> RegistrationActivity.super.onBackPressed());
        
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new RegistrationFragment()).commit();
        }else getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new AccademicDetailsFragment()).commit();
    }

}