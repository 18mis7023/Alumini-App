package com.vitap.aluminireconnect;

import static java.security.AccessController.getContext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NewPostActivity extends AppCompatActivity {

    private TextInputEditText Desc_ET;
    private ImageView PostImage,postSelectedImage;
    private Uri uri = null;
    private String ImageUrl = null,PostId;
    private ProgressDialog PD;
    private FirebaseFirestore db;
    private DocumentReference PostKey;
    private AutoCompleteTextView addFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        PD = new ProgressDialog(this);
        PostImage = findViewById(R.id.post_img);
        postSelectedImage=findViewById(R.id.post_selected_img);
        RelativeLayout upload_img = findViewById(R.id.upload_img);
//        Title_ET = findViewById(R.id.title_et);
        Desc_ET = findViewById(R.id.desc_et);
        MaterialCardView post_bt = findViewById(R.id.post_bt);
        db = FirebaseFirestore.getInstance();
        addFilter=findViewById(R.id.add_filter);

        upload_img.setOnClickListener(v -> ImagePicker());
        PostImage.setOnClickListener(v -> ImagePicker());

        String[] addFilterItems=new String[]{
                "Discussion","NewsFeed",
                "Memories",
                "Events"
        };
        ArrayAdapter<String> schoolAdapter=new ArrayAdapter<>(
                this,R.layout.dropdown_item,addFilterItems
        );
        addFilter.setAdapter(schoolAdapter);

        post_bt.setOnClickListener(v -> {
            if (Objects.requireNonNull(addFilter.getText()).toString().isEmpty() || Objects.requireNonNull(Desc_ET.getText()).toString().isEmpty()){
                Toast.makeText(this, "Filter and Description can't empty", Toast.LENGTH_SHORT).show();
            } else {
                PostKey = db.collection("Posts").document();
                PostId = PostKey.getId();
                updatePost();
            }

        });

        findViewById(R.id.back_card).setOnClickListener(view -> NewPostActivity.super.onBackPressed());


    }


    private void updatePost() {
        PD.setMessage("Posting...");
        PD.show();
        if (uri!=null){
            UploadImage();
        }else UploadNewpost(false);

    }

    private void UploadNewpost(boolean withImg) {

        PD.setMessage("Posting...");
        Map<String, Object> Post = new HashMap<>();
        if (withImg){
            Post.put("ImgUrl",ImageUrl);
        }else Post.put("ImgUrl","");

        Post.put("Filter", Objects.requireNonNull(addFilter.getText()).toString().trim());
        Post.put("Desc", Objects.requireNonNull(Desc_ET.getText()).toString().trim());
        Post.put("UserId", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid() );
        Post.put("Time", FieldValue.serverTimestamp());
        PostKey.set(Post)
                .addOnSuccessListener(aVoid -> {
                    PD.dismiss();
                    startActivity(new Intent(NewPostActivity.this,HomeActivity.class));
                })
                .addOnFailureListener(e -> {
                    PD.dismiss();
                    Toast.makeText(NewPostActivity.this, "Error : "+e, Toast.LENGTH_SHORT).show();
                });

    }

    private void UploadImage() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        PD.setMessage("Uploading...");
        postSelectedImage.setDrawingCacheEnabled(true);
        postSelectedImage.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) postSelectedImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference storageRef = storage.getReference().child("Posts/");
        StorageReference mountainsRef = storageRef.child(PostId);
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnProgressListener(snapshot -> {
            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            PD.setMessage("Upload is " + progress + "% done");
        });
        uploadTask.addOnFailureListener(exception -> {
            PD.dismiss();
            Toast.makeText(NewPostActivity.this, "Error : "+exception, Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            mountainsRef.getDownloadUrl().addOnSuccessListener(uri -> {
                ImageUrl = uri.toString();
                UploadNewpost(true);
            });

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            assert data != null;
            uri = data.getData();
            if (requestCode == 0) {
                postSelectedImage.setImageURI(uri);
            }

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

    }

    public void ImagePicker(){
        ImagePicker.with(NewPostActivity.this)
                .crop(3,2)
                .galleryOnly()
                .compress(150)
                .start(0);
    }

}