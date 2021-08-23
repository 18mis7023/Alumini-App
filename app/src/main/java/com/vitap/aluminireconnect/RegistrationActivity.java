package com.vitap.aluminireconnect;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.vitap.aluminireconnect.Fragments.AccademicDetailsFragment;
import com.vitap.aluminireconnect.Fragments.RegistrationFragment;


public class RegistrationActivity extends AppCompatActivity {
    private MaterialCardView backCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        backCard=findViewById(R.id.back_card);

        RelativeLayout background = findViewById(R.id.background);
        int currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                //light mode
                background.setBackgroundResource(R.drawable.lightnormalpattern);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //Dark mode
                background.setBackgroundResource(R.drawable.dark_background);
                break;
        }

        backCard.setOnClickListener(view -> RegistrationActivity.super.onBackPressed());
        
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new RegistrationFragment()).commit();
        }else getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new AccademicDetailsFragment()).commit();
    }

}