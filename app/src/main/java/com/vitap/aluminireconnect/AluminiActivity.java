package com.vitap.aluminireconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

public class AluminiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumini);

        ConstraintLayout background = findViewById(R.id.background);
        int currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                //light mode
//                background.setBackgroundResource(R.drawable.lightmode_pattern);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //Dark mode
                background.setBackgroundResource(R.drawable.darkmode_pattern);
                break;
        }

        findViewById(R.id.log_in).setOnClickListener(view -> {
            startActivity(new Intent(AluminiActivity.this,LoginActivity.class));
            finish();
        });

    }
}