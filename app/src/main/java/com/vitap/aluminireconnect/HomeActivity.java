package com.vitap.aluminireconnect;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vitap.aluminireconnect.Fragments.DiscussionFeedFragment;
import com.vitap.aluminireconnect.Fragments.ProfileFragment;

import java.io.ByteArrayOutputStream;

public class HomeActivity extends AppCompatActivity{

    private BottomNavigationView bottomNavigationView;
    private FirebaseUser user ;
    private String ProfileImageUrl = null;
    private Uri uri = null;
    private ImageView ProfileImage;
    private ProgressDialog PD;
    public Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        user = FirebaseAuth.getInstance().getCurrentUser();
        PD = new ProgressDialog(this);

        try {
            ProfileImageUrl = user.getPhotoUrl().toString();
        }catch (Exception ex){
            if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("profile_img_DSA",false)) {
                ShowImageUplodeDialog();
            }
        }


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

    public void ShowImageUplodeDialog(){
        dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.profile_image_update_dialog);

        ProfileImage = dialog.findViewById(R.id.profile_img);
        Button SKip = dialog.findViewById(R.id.skip);
        MaterialCheckBox checkBox = dialog.findViewById(R.id.dont_show_again);

        checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit().putBoolean("profile_img_DSA",true).apply();
            }else {
                PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit().putBoolean("profile_img_DSA",false).apply();
            }
        });

        SKip.setOnClickListener(view -> {
            dialog.dismiss();
        });

        ProfileImage.setOnClickListener(view -> {
            if (uri == null) {
                ImagePicker.with(HomeActivity.this)
                        .crop(1, 1)
                        .galleryOnly()
                        .compress(100)
                        .start(0);
            }else {
                UploadImage();
            }
        });
        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            assert data != null;
            uri = data.getData();
            if (requestCode == 0) {
                ProfileImage.setImageURI(uri);
                UploadImage();
            }

        } else if (requestCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void UploadImage() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        PD.setMessage("Uploading...");
        PD.show();
        ProfileImage.setDrawingCacheEnabled(true);
        ProfileImage.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) ProfileImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference storageRef = storage.getReference().child("UsersProfileImg/");
        StorageReference mountainsRef = storageRef.child(user.getUid());
        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnProgressListener(snapshot -> {
            double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            PD.setMessage("Upload is " + progress + "% done");
        });
        uploadTask.addOnFailureListener(exception -> {
            Toast.makeText(HomeActivity.this, "Error : "+exception, Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            mountainsRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String ImageUrl = uri.toString();

                FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(user.getUid())
                        .update("ProfileImage",ImageUrl)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    UpdateUser(ImageUrl);
                                }else {
                                    PD.dismiss();
                                    Toast.makeText(HomeActivity.this, "Error : "+task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            });

        });
    }

    public void UpdateUser(String url){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(url))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    PD.dismiss();
                    dialog.dismiss();
                    recreate();
                    if (!task.isSuccessful()) {
                        Toast.makeText(HomeActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}