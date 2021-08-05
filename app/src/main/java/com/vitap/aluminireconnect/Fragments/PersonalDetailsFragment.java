package com.vitap.aluminireconnect.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vitap.aluminireconnect.R;

import java.util.HashMap;


public class PersonalDetailsFragment extends Fragment {

    private EditText FatherName;
    private EditText FatherMobileNumber;
    private TextInputEditText MotherName,MotherMobileNumber,PermanentAddress;
    private Button PersonalNext;
    private FirebaseAuth mAuth;
    private final static String TAG = "PersonalDetails";
    private FirebaseFirestore db;
    private FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_personal_details, container, false);
        FatherName=view.findViewById(R.id.father_name);
        FatherMobileNumber=view.findViewById(R.id.father_mobile_number);
        PersonalNext=view.findViewById(R.id.personal_next);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        PersonalNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap UserDetails=new HashMap();
                UserDetails.put("FatherName",FatherName.getText().toString());
                UserDetails.put("FathersMobileNumber",FatherMobileNumber.getText().toString());
                UserDetails.put("MotherName",MotherName.getText().toString());
                UserDetails.put("MothersMobileNumber",MotherMobileNumber.getText().toString());
                UserDetails.put("PermanentAddress",PermanentAddress.getText().toString());


                db.collection("Users")
                        .document("6SX37hFaW8cSS4Ut8b5DaRWzXr92")
                        .update(UserDetails)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                getFragmentManager().beginTransaction().replace(R.id.frame_layout,new AdditionalDetailsFragment()).commit();
//                                Toast.makeText(getContext(), "UserData Updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
//                                Toast.makeText(getContext(), "Error : "+e, Toast.LENGTH_SHORT).show();
                            }
                        });


            }
        });
        return view;
    }
}