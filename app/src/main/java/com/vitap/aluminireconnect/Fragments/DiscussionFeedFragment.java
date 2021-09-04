package com.vitap.aluminireconnect.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.LoadState;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;
import com.vitap.aluminireconnect.CommentsActivity;
import com.vitap.aluminireconnect.NewPostActivity;
import com.vitap.aluminireconnect.PrettytimeFromat;
import com.vitap.aluminireconnect.R;
import com.vitap.aluminireconnect.model.FeedModel;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

public class DiscussionFeedFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView EventsRecycler;
    private FirebaseFirestore fireStore;
    private Context context;
    private MaterialCardView EventsCard,NewsFeedCard;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    DatabaseReference likesRef = databaseReference.child("Likes");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Boolean IsLiked = false;
    private FirestorePagingAdapter<FeedModel,FeedViewHolder> adapter;

    public DiscussionFeedFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discussion_feed, container, false);
        progressBar = view.findViewById(R.id.progress_circular);

        fireStore = FirebaseFirestore.getInstance();
        context = getContext().getApplicationContext();

        progressBar.setVisibility(View.VISIBLE);
        EventsRecycler = view.findViewById(R.id.feed_recyclerView);
        EventsCard = view.findViewById(R.id.events_card);
        NewsFeedCard = view.findViewById(R.id.news_feed_card);


        /*
        EventsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Query query = fireStore.collection("Posts")
                        .whereEqualTo("Filter","Discussion")
                        .orderBy("Time", Query.Direction.DESCENDING);
                PagingConfig config =  new PagingConfig(3,1);
                Pagging(query,config);

            }
        });
        Memories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "Memories", Toast.LENGTH_SHORT).show();
                adapter.stopListening();

                Query query = fireStore.collection("Posts")
                        .whereEqualTo("Filter","Memories")
                        .orderBy("Time", Query.Direction.DESCENDING);
                PagingConfig config =  new PagingConfig(3,1);
                Pagging(query,config);
            }
        });
        Internship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = fireStore.collection("Posts")
                        .whereEqualTo("Filter","Internship")
                        .orderBy("Time", Query.Direction.DESCENDING);

                PagingConfig config =  new PagingConfig(3,1);

                Pagging(query,config);
            }
        });
         */

        Query query = fireStore
                .collection("Posts")
                .orderBy("Time", Query.Direction.DESCENDING);

        PagingConfig config =  new PagingConfig(2,2);

        FirestorePagingOptions<FeedModel> options = new FirestorePagingOptions.Builder<FeedModel>()
                .setLifecycleOwner(this)
                .setQuery(query, config, snapshot -> {
                    String Title = Objects.requireNonNull(snapshot.get("Filter")).toString();
                    String ImgUrl = Objects.requireNonNull(snapshot.get("ImgUrl")).toString();
                    String Desc = Objects.requireNonNull(snapshot.get("Desc")).toString();
                    String UserId = Objects.requireNonNull(snapshot.get("UserId")).toString();
                    String postId = snapshot.getId();
                    Timestamp Time = snapshot.getTimestamp("Time");

                    return new FeedModel(
                            Title, Desc, ImgUrl, UserId, postId,
                            Objects.requireNonNull(Time).toDate()
                    );
                })
                .build();


        adapter = new FirestorePagingAdapter<FeedModel, FeedViewHolder>(options) {

            @NonNull
            public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_recycler_items, parent, false);
                return new FeedViewHolder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull FeedViewHolder holder, int position, @NonNull FeedModel model) {

                holder.FilterText.setText(model.getFilter());
                holder.ImageView.setImageTintList(null);

                holder.time.setText(new PrettytimeFromat().Ago(model.getTime().toString()));

                String PostId = model.getPostId();
                String Desc = model.getDesc();
                String UserId = model.getUserId();
                holder.Decs.setText(Desc);

                if (!model.getImgUrl().isEmpty()) {
                    Picasso.get()
                            .load(model.getImgUrl())
                            .into(holder.ImageView);
                    holder.Decs.setMaxLines(3);
                    holder.Decs.setEllipsize(TextUtils.TruncateAt.END);
                } else {
                    holder.ImageView.setVisibility(View.GONE);
                    holder.Decs.setMaxLines(14);
                    holder.Decs.setSingleLine(false);
                }

                FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(UserId)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                holder.clubName.setText(Objects.requireNonNull(document.get("FirstName")).toString());
                                String ImageUrl = document.get("ProfileImage").toString();
                                if (!ImageUrl.equals("null")) {
                                    Picasso.get()
                                            .load(ImageUrl)
                                            .centerInside()
                                            .resize(300, 200)
                                            .into(holder.ClubImg);
                                }
                            }
                        });

                holder.Comment.setOnClickListener(v -> openCommentsActivity(PostId, Desc));
                holder.PostCard.setOnClickListener(v -> openCommentsActivity(PostId, Desc));
                holder.Decs.setOnClickListener(v -> openCommentsActivity(PostId, Desc));
                holder.ImageView.setOnClickListener(v -> {
                    Bitmap bitmap = ((BitmapDrawable) holder.ImageView.getDrawable()).getBitmap();
                    ShowImage(bitmap);
                });

                holder.clubName.setOnClickListener(v -> {
                   /*
                    Intent intent = new Intent(context, Profile.class);
                    intent.putExtra("UserId", model.getUserId());
                    context.startActivity(intent);
                    */
                    Toast.makeText(getContext(), "profile", Toast.LENGTH_SHORT).show();
                });
                holder.ClubImg.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "profile", Toast.LENGTH_SHORT).show();
                });

               /*
               holder.Share.setOnClickListener(v -> {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, model.getDyLink());
                    sendIntent.setType("text/plain");
                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                });

                */

                holder.setLikesButtonStatus(PostId);
                holder.getCommentsCount(PostId);

                holder.Like.setOnClickListener(v -> {
                    LikeClick(model.getPostId(), holder.Like);
                });
                holder.LikesLayout.setOnClickListener(view ->
                        LikeClick(model.getPostId(), holder.Like)
                );

            }

        };



        EventsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        EventsRecycler.setHasFixedSize(true);
        //eventsAdapter = new EventsAdapter(EventsList, Events.this);
        EventsRecycler.setAdapter(adapter);


        view.findViewById(R.id.new_feed).setOnClickListener(v ->{
                    startActivity(new Intent(getContext(), NewPostActivity.class));
                }
        );



        adapter.addLoadStateListener(states -> {

            LoadState refresh = states.getRefresh();
            LoadState append = states.getAppend();

            // The previous load (either initial or additional) failed. Call
            // the retry() method in order to retry the load operation.
            if (refresh instanceof LoadState.Error || append instanceof LoadState.Error) {
                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                adapter.retry();
            }

            if (refresh instanceof LoadState.Loading) {
                // The initial Load has begun
                // ...

                progressBar.setVisibility(View.VISIBLE);
            }


            if (append instanceof LoadState.Loading) {
                // The adapter has started to load an additional page
                // ...
                progressBar.setVisibility(View.VISIBLE);
            }

            if (append instanceof LoadState.NotLoading) {
                LoadState.NotLoading notLoading = (LoadState.NotLoading) append;
                if (notLoading.getEndOfPaginationReached()) {
                    // The adapter has finished loading all of the data set
                    // ...
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "No more posts", Toast.LENGTH_SHORT).show();
                    return null;
                }

                if (refresh instanceof LoadState.NotLoading) {
                    // The previous load (either initial or additional) completed
                    // ...
                    progressBar.setVisibility(View.GONE);

                    return null;
                }
            }
            return null;
        });


        return view;
    }



    public class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView FilterText, Decs, time, clubName, likesCount, CommentsCount;
        android.widget.ImageView ImageView, ClubImg;
        ImageButton Like, Comment;
        CardView PostCard;
        LinearLayout LikesLayout;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            FilterText = itemView.findViewById(R.id.filter_text);
            Decs = itemView.findViewById(R.id.event_decs);
            ImageView = itemView.findViewById(R.id.event_image);
            time = itemView.findViewById(R.id.time);
            clubName = itemView.findViewById(R.id.club_name);
            ClubImg = itemView.findViewById(R.id.club_prof_img);
            Like = itemView.findViewById(R.id.like);
            Comment = itemView.findViewById(R.id.comment);
            likesCount = itemView.findViewById(R.id.like_counter);
            CommentsCount = itemView.findViewById(R.id.comment_counter);
            PostCard = itemView.findViewById(R.id.post_card);
            LikesLayout = itemView.findViewById(R.id.likes_layout);

        }

        public void setLikesButtonStatus(final String postId) {
            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.child(postId).hasChild(user.getUid())) {
                        String LikesCount = String.valueOf((int) snapshot.child(postId).getChildrenCount());
                        likesCount.setText(LikesCount);
                        Like.setImageResource(R.drawable.ic_filled_heart);
                    } else {
                        String LikesCount = String.valueOf((int) snapshot.child(postId).getChildrenCount());
                        likesCount.setText(LikesCount);
                        Like.setImageResource(R.drawable.ic_heart);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });


        }
        public void getCommentsCount(final String postId){
            databaseReference
                    .child("Comments")
                    .child(postId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            String commentsCount = String.valueOf((int) snapshot.getChildrenCount());
                            CommentsCount.setText(commentsCount);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }

    }



    public void openCommentsActivity(String PostId, String Desc) {
       Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra("PostId", PostId);
        intent.putExtra("Desc", Desc);
        startActivity(intent);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void ShowImage(Bitmap bitmap) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.image_dialog);

        ImageView imageView = dialog.findViewById(R.id.image);
        ImageButton CloseButton = dialog.findViewById(R.id.close);
        CloseButton.setOnClickListener(v -> dialog.dismiss());
        imageView.setImageBitmap(bitmap);
        imageView.setOnTouchListener(new ImageMatrixTouchHandler(getActivity()));
        dialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }



    public void LikeClick(String PostId,ImageButton LikeButton){
        IsLiked = true;
        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (IsLiked) {
                    if (snapshot.child(PostId).hasChild(user.getUid())) {
                        likesRef.child(PostId).child(user.getUid()).removeValue();
                        LikeButton.setImageResource(R.drawable.ic_heart);
                    } else {
                        likesRef.child(PostId).child(user.getUid()).setValue(new Date().toString());
                        LikeButton.setImageResource(R.drawable.ic_filled_heart);
                    }
                    IsLiked = false;
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

}