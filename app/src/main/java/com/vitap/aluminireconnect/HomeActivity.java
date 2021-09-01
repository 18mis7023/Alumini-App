package com.vitap.aluminireconnect;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vitap.aluminireconnect.Fragments.DiscussionFeedFragment;
import com.vitap.aluminireconnect.Fragments.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);



        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    RemoveAllFragments();
                    getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout, new DiscussionFeedFragment()).commit();
                    return true;
                case R.id.new_post:
                     startActivity(new Intent(HomeActivity.this,NewPostActivity.class));
                    return false;
                case R.id.chat:
                    Toast.makeText(HomeActivity.this, "chat", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.profile:
                    RemoveAllFragments();
                    getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout, new ProfileFragment()).commit();
                    return true;
            }
            return false;
        });

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame_layout, new DiscussionFeedFragment()).commit();

    }


    public void RemoveAllFragments(){
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
    }
}