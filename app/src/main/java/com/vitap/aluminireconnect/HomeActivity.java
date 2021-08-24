package com.vitap.aluminireconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vitap.aluminireconnect.Fragments.DiscussionFeedFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout, new DiscussionFeedFragment()).commit();

    }
}