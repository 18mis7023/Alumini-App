package com.vitap.aluminireconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.vitap.aluminireconnect.R;
import com.vitap.aluminireconnect.model.CommentsModel;

import org.jetbrains.annotations.NotNull;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.commentsViewHolder> {

    ArrayList<CommentsModel> commentsList;
    Context context;

    public CommentsAdapter(ArrayList<CommentsModel> list, Context context){
        this.commentsList = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public commentsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_list_view,parent,false);
        return new commentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentsAdapter.commentsViewHolder holder, int position) {

        holder.Comment.setText(commentsList.get(position).getComment());

        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
        Date date = new Date(commentsList.get(position).getTime());
        String ago = prettyTime.format(date);
        holder.Time.setText(ago);

        FirebaseFirestore.getInstance().collection("Users")
                .document(commentsList.get(position).getUserId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            String name = Objects.requireNonNull(document.get("FirstName")).toString();
                            String url = Objects.requireNonNull(document.get("ProfileImage")).toString();
                            holder.Name.setText(name);
                            if (!url.equals("null")) {
                                holder.UserProfileImg.setImageTintMode(null);
                                Picasso.get()
                                      .load(url)
                                     .into(holder.UserProfileImg);
                            }
                    }else Toast.makeText(context, "error : "+task.getException(), Toast.LENGTH_SHORT).show();
                });


    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class commentsViewHolder extends RecyclerView.ViewHolder {
        ImageView UserProfileImg;
        TextView Name,Comment,Time;
        public commentsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            UserProfileImg = itemView.findViewById(R.id.user_prof_img);
            Name = itemView.findViewById(R.id.user_name);
            Comment = itemView.findViewById(R.id.user_comment);
            Time = itemView.findViewById(R.id.comment_time);

        }
    }



}
