package com.vitap.aluminireconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.vitap.aluminireconnect.Fragments.DiscussionFeedFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout, new DiscussionFeedFragment()).commit();
                        return true;
                    case R.id.new_post:
                        Toast.makeText(HomeActivity.this, "New post", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.chat:
                        Toast.makeText(HomeActivity.this, "chat", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.profile:
                        Toast.makeText(HomeActivity.this, "profile", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout, new DiscussionFeedFragment()).commit();

    }
}