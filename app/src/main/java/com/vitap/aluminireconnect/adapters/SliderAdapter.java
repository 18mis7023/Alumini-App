package com.vitap.aluminireconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.vitap.aluminireconnect.R;


public class SliderAdapter extends PagerAdapter {

    public Context context;
    public String[] SlideTitles = {"RECONNECT","RECREATE","REPRESENT"};
    public String[] SlideDescription = {
            "Reconnect with your friends and\n" + "share your wonderful memories ",
            "A platform to recreate millions of\n" + "feeling and thousands of memories",
            "An opportunity to represent your\n" + "university and experience the life"};
    public int[] SlideImages = {
            R.drawable.ic_group_persons_icon,
            R.drawable.ic_heart_icon,
            R.drawable.ic_person_icon
    };

    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return SlideTitles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);

        TextView Title = view.findViewById(R.id.title);
        TextView Description = view.findViewById(R.id.description);
        ImageView Image = view.findViewById(R.id.image);

        Title.setText(SlideTitles[position]);
        Description.setText(SlideDescription[position]);
        Image.setImageResource(SlideImages[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
