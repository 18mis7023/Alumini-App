package com.vitap.aluminireconnect.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.vitap.aluminireconnect.R;
import com.vitap.aluminireconnect.adapters.LinksAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {

    private ImageView ProfileImage;
    private Uri uri = null;
    private ProgressDialog PD;
    private String UserName = null;
    private String UID;
    private RecyclerView PostRecycler;
    private MaterialCardView AddLink ;
    private FirebaseFirestore fireStore;
    private DocumentReference UserDocument;
    private List<String> LinksList = null;
    private RecyclerView SocialLinksRecycler;
    public LinksAdapter linksAdapter;
    private LinearLayoutManager layoutManager;

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
        AddLink = view.findViewById(R.id.add_link);
        PostRecycler = view.findViewById(R.id.post_recycler);
        PD = new ProgressDialog(getContext());
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        fireStore =  FirebaseFirestore.getInstance();
        SocialLinksRecycler = view.findViewById(R.id.links_recycler);

        UserDocument = fireStore.collection("Users").document(UID);

        SocialLinksRecycler.setHasFixedSize(true);
        // The number of Columns
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        SocialLinksRecycler.setLayoutManager(layoutManager);


        AddLink.setOnClickListener(view12 -> {

           Dialog dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.add_links_dialog);

            TextInputEditText LinkEt = dialog.findViewById(R.id.link);
            Button add = dialog.findViewById(R.id.add_link_button);

            add.setOnClickListener(view1 -> {
                if (!LinkEt.getText().toString().trim().isEmpty()){
                    String link = LinkEt.getText().toString().trim();
                    if (extractUrls(link) != null){
                        UpdateLink(link,dialog);
                    }else Toast.makeText(getContext(), "Invalid link", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "Enter link", Toast.LENGTH_SHORT).show();
                }
            });

            dialog.show();
        });

       UserDocument
                .get()
                .addOnCompleteListener(task -> {
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
                        try {
                            LinksList = (List<String>) snapshot.get("Links");
                            if (LinksList != null) {
                                linksAdapter = new LinksAdapter(getContext(), LinksList);
                                SocialLinksRecycler.setAdapter(linksAdapter);
                            }
                        }catch (Exception e){
                            Log.e("Error : ",e.toString());
                        }

                    }
                });

        return view;
    }

    public static String extractUrls(String input)
    {
        String result = null;

        String[] words = input.split("\\s+");
        Pattern pattern = Patterns.WEB_URL;
        for(String word : words) {
            if(pattern.matcher(word).find()) {
                if(!word.toLowerCase().contains("http://") && !word.toLowerCase().contains("https://")) {
                    word = "http://" + word;
                }
                result = word;
            }
        }

        return result;
    }

    public void UpdateLink(String link,Dialog dialog){
        PD.show();
        PD.setMessage("Loading...");

            UserDocument
                    .update("Links", FieldValue.arrayUnion(link))
                    .addOnCompleteListener(task -> {
                        PD.dismiss();
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            if (LinksList == null){
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                if (Build.VERSION.SDK_INT >= 26) {
                                    ft.setReorderingAllowed(false);
                                }
                                ft.detach(this).attach(this).commit();
                            } else {
                                LinksList.add(link);
                                linksAdapter.notifyDataSetChanged();
                            }
                        } else
                            Toast.makeText(getContext(), "error : Can't update", Toast.LENGTH_SHORT).show();
                    });

    }
}