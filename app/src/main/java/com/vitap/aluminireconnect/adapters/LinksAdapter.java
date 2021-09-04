package com.vitap.aluminireconnect.adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;
import com.vitap.aluminireconnect.R;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.List;

public class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.LinksHolder> {

    public Context context;
    public List<String> LinksList;

    public LinksAdapter(Context context, List<String> linksList) {
        this.context = context;
        LinksList = linksList;
    }


    @NonNull
    @Override
    public LinksHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.social_links_layout,parent,false);
        return new LinksHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LinksHolder holder, @SuppressLint("RecyclerView") int position) {

        String link = LinksList.get(position);
        if (link.contains("linkedin.com")){
            Picasso.get()
                    .load("https://www.keesingtechnologies.com/wp-content/uploads/2018/07/Linkedin-Icon.png")
                    .into(holder.SocialImage);
        }else{
            Picasso.get()
                    .load("https://www.google.com/s2/favicons?sz=64&domain_url=" + LinksList.get(position))
                    .into(holder.SocialImage);
        }
        holder.SocialImage.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(LinksList.get(position)));
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return LinksList.size();
    }

    public class LinksHolder extends RecyclerView.ViewHolder {
        ImageView SocialImage;
        MaterialCardView imageCard;
        public LinksHolder(@NonNull View itemView) {
            super(itemView);
            SocialImage = itemView.findViewById(R.id.social_icon);
            imageCard = itemView.findViewById(R.id.social_image_card);
        }


    }
}
