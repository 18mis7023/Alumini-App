package com.vitap.aluminireconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vitap.aluminireconnect.PrettytimeFromat;
import com.vitap.aluminireconnect.R;
import com.vitap.aluminireconnect.model.FeedModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserPostsAdapter extends RecyclerView.Adapter<UserPostsAdapter.UserPostViewHolder> {

    public Context context;
    public ArrayList<FeedModel> FeedList;

    public UserPostsAdapter(Context context, ArrayList<FeedModel> feedList) {
        this.context = context;
        FeedList = feedList;
    }

    @NonNull
    @Override
    public UserPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_posts_layout,parent,false);
        return new UserPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPostViewHolder holder, int position) {

        String PTime = new PrettytimeFromat().Ago(FeedList.get(position).getTime().toString());
        holder.Time.setText(PTime);
        holder.Description.setText(FeedList.get(position).getDesc());
        String ImageUrl = FeedList.get(position).getImgUrl();

        if (!ImageUrl.isEmpty()){
            Picasso.get()
                    .load(ImageUrl)
                    .into(holder.PostImage);
        }else {
            holder.PostImage.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return FeedList.size();
    }

    public class UserPostViewHolder extends RecyclerView.ViewHolder {
        ImageView PostImage;
        TextView Description,Time;
        public UserPostViewHolder(@NonNull View itemView) {
            super(itemView);
            PostImage = itemView.findViewById(R.id.post_image);
            Description = itemView.findViewById(R.id.post_description);
            Time = itemView.findViewById(R.id.post_time);
        }
    }
}
