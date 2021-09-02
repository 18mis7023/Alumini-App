package com.vitap.aluminireconnect;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.vitap.aluminireconnect.adapters.CommentsAdapter;
import com.vitap.aluminireconnect.model.CommentsModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommentsActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView CommentsRecycler;
    private ArrayList<CommentsModel> commentsList;
    private CommentsAdapter commentsAdapter;
    private DatabaseReference postCommentRef;
    private EditText commentET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);



        String postId = getIntent().getStringExtra("PostId");
        final String[] desc = {""};
        try {
            desc[0] =  getIntent().getStringExtra("Desc");
        }catch (Exception e){
            FirebaseFirestore.getInstance().collection("Posts").document(postId).get()
                    .addOnCompleteListener(task -> desc[0] = String.valueOf(task.getResult().get("Desc")));
        }

        progressBar =  findViewById(R.id.pm);
        TextView description = findViewById(R.id.post_desc);
        description.setText(desc[0]);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        postCommentRef = database.child("Comments").child(postId);

        commentET = findViewById(R.id.comment_et);
        ImageButton send = findViewById(R.id.send_comment);

        progressBar.setVisibility(View.VISIBLE);
        CommentsRecycler = findViewById(R.id.comments_recyclerView);
        ImageView profileImg = findViewById(R.id.current_user_prof_img);

        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String url = Objects.requireNonNull(document.get("ProfileImage")).toString();
                            if (!url.equals("null")) {
                                profileImg.setImageTintMode(null);
                                Picasso.get()
                                        .load(url)
                                        .into(profileImg);
                            }
                        }

                    }
                });


        CommentsRecycler.setLayoutManager(new LinearLayoutManager(this));
        commentsList = new ArrayList<>();


        postCommentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot ds : snapshot.getChildren())
                    {
                        commentsList.add(ds.getValue(CommentsModel.class));
                    }
                    commentsAdapter = new CommentsAdapter(commentsList,CommentsActivity.this);
                    CommentsRecycler.setAdapter(commentsAdapter);
                    commentsAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(CommentsActivity.this, "No comments", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(CommentsActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });
        commentsAdapter = new CommentsAdapter(commentsList,CommentsActivity.this);
        CommentsRecycler.setAdapter(commentsAdapter);

        send.setOnClickListener(v -> {
            if (!commentET.getText().toString().isEmpty()){
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                String Comment = commentET.getText().toString().trim();
                String UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String Time = new Date().toString();
                String key = postCommentRef.push().getKey();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put( key+"/UserId/", UserId);
                childUpdates.put( key+"/Comment/", Comment);
                childUpdates.put( key+"/Time",Time);

                commentsList.add(new CommentsModel(Comment,UserId,Time));
                commentsAdapter.notifyDataSetChanged();

                postCommentRef.updateChildren(childUpdates).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        commentET.setText("");
                    }else Toast.makeText(CommentsActivity.this, "Error : "+task.getException(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });

            }
        });


    }
}