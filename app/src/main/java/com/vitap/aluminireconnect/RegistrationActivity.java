package com.vitap.aluminireconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vitap.aluminireconnect.Fragments.AccademicDetailsFragment;
import com.vitap.aluminireconnect.Fragments.RegistrationFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {

    private String FirstName,SecondName,School,PhoneNumber,PersonalEmail,AluminiEmail,Password,
            FatherName,FatherPhoneNumber,MotherName,MotherPhoneNumber,PermanentAddress,
            PlacedCompanyName,HigherEducation, CurriculumFeedback,
            CampusFeedback,CompetitiveExams,Startup;
    private FirebaseAuth mAuth;
    private final static String TAG = "Registration ";
    private FirebaseFirestore db;
    private FirebaseUser user;
    private ImageView background;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        background = findViewById(R.id.background_img);

       /*
        int currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                //light mode
                background.setImageResource(R.drawable.light_reg_background);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //Dark mode
                background.setImageResource(R.drawable.dark_reg_background);
                break;
        }
        */

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        AluminiEmail = "sai.gopalsai143@gmail.com";
        Password = "Password";

        //Call this method when data is ready
        //CreateNewUser(AluminiEmail,Password);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new RegistrationFragment()).commit();
        }else getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new AccademicDetailsFragment()).commit();
    }

    private void SetUserData() {

        Map<String, Object> UserDetails = new HashMap<>();
        UserDetails.put("FirstName", FirstName);
        UserDetails.put("SecondName", SecondName);
        UserDetails.put("School", School);
        UserDetails.put("PhoneNumber", PhoneNumber);
        UserDetails.put("PersonalEmail", PersonalEmail);
        UserDetails.put("AluminiEmail", AluminiEmail);
        UserDetails.put("FatherName", FatherName);
        UserDetails.put("FatherPhoneNumber", FatherPhoneNumber);
        UserDetails.put("MotherName", MotherName);
        UserDetails.put("MotherPhoneNumber", MotherPhoneNumber);
        UserDetails.put("PermanentAddress", PermanentAddress);
        UserDetails.put("PlacedCompanyName", PlacedCompanyName);
        UserDetails.put("HigherEducation", HigherEducation);
        UserDetails.put("CurriculumFeedback", CurriculumFeedback);
        UserDetails.put("CampusFeedback", CampusFeedback);
        UserDetails.put("CompetitiveExams", CompetitiveExams);
        UserDetails.put("StartupName", Startup);

        db.collection("Users")
                .document(user.getUid())
                .set(UserDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(RegistrationActivity.this, "UserData Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(RegistrationActivity.this, "Error : "+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void CreateNewUser(String Email,String Password){

        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        Log.d(TAG, "createUserWithEmail:success");
                        user = mAuth.getCurrentUser();
                        Objects.requireNonNull(user).sendEmailVerification();
                        SetUserData();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegistrationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}