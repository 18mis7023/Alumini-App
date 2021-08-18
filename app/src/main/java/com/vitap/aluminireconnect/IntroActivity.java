package com.vitap.aluminireconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vitap.aluminireconnect.adapters.SliderAdapter;

public class IntroActivity extends AppCompatActivity {

    private ViewPager SlideViewPager;
    private LinearLayout DotIndicatorLayout;
    private RelativeLayout MainLayout;
    private final int[] BackgroundImages = {
            R.drawable.first_background,
            R.drawable.second_background,
            R.drawable.third_background
    };
    private Button Next,Skip;
    private ImageView EndImage;
    private int CurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        SlideViewPager = findViewById(R.id.slide_view_pager);
        DotIndicatorLayout = findViewById(R.id.indicator_layout);
        MainLayout = findViewById(R.id.main_layout);
        Next = findViewById(R.id.next_slide);
        Skip = findViewById(R.id.skip_slide);
        EndImage = findViewById(R.id.end_image);
        EndImage.setVisibility(View.GONE);

        SliderAdapter sliderAdapter = new SliderAdapter(this);
        SlideViewPager.setAdapter(sliderAdapter);

        AddDotsIndicator(0);
        SlideViewPager.addOnPageChangeListener(viewListener);

        CurrentPage = 0;

        Next.setOnClickListener(view -> SlideViewPager.setCurrentItem(CurrentPage+1));
        Skip.setOnClickListener(view -> startActivity(new Intent(IntroActivity.this,AluminiActivity.class)));
        EndImage.setOnClickListener(view -> startActivity(new Intent(IntroActivity.this,AluminiActivity.class)));
    }

    public void AddDotsIndicator(int position){

        TextView[] dots = new TextView[3];
        DotIndicatorLayout.removeAllViews();

        for (int i = 0; i< dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.aluminium));

            DotIndicatorLayout.addView(dots[i]);
        }

        if (dots.length > 0){
            dots[position].setText(Html.fromHtml("&#8226"));
            dots[position].setTextColor(getResources().getColor(R.color.white));
            dots[position].setTextSize(35);
        }

    }


    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            CurrentPage = position;
            AddDotsIndicator(position);
            MainLayout.setBackgroundResource(BackgroundImages[position]);
            if (position == 2){
                Next.setVisibility(View.GONE);
                Skip.setVisibility(View.GONE);
                EndImage.setVisibility(View.VISIBLE);
            }else {
                Next.setVisibility(View.VISIBLE);
                Skip.setVisibility(View.VISIBLE);
                EndImage.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}