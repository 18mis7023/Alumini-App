package com.vitap.aluminireconnect.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.vitap.aluminireconnect.NewPostActivity;
import com.vitap.aluminireconnect.R;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private ImageView ProfileImage;
    private Uri uri = null;
    private ProgressDialog PD;
    private String UserName = null;
    private String UID;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ProfileImage = view.findViewById(R.id.profile_image);
        TextView userName = view.findViewById(R.id.user_name);
        PD = new ProgressDialog(getContext());
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(UID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            UserName = snapshot.get("FirstName").toString()+" "+snapshot.get("LastName").toString();
                            userName.setText(UserName);
                            String ImageUrl = snapshot.get("ProfileImage").toString();
                            ProfileImage.setImageTintMode(null);
                            if (!ImageUrl.equals("null")){
                                Picasso.get()
                                        .load(ImageUrl)
                                        .into(ProfileImage);
                            }
                        }
                    }
                });



        return view;
    }

}