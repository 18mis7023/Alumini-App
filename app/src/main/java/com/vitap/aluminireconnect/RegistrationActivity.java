package com.vitap.aluminireconnect;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.RelativeLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.vitap.aluminireconnect.Fragments.AccademicDetailsFragment;
import com.vitap.aluminireconnect.Fragments.RegistrationFragment;


public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        RelativeLayout background = findViewById(R.id.background);
        int currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                //light mode
                background.setBackgroundResource(R.drawable.light_background);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //Dark mode
                background.setBackgroundResource(R.drawable.darkmode_pattern);
                break;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new RegistrationFragment()).commit();
        }else getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new AccademicDetailsFragment()).commit();
    }

}