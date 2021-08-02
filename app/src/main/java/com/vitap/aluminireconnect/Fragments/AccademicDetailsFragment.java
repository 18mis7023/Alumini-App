package com.vitap.aluminireconnect.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vitap.aluminireconnect.R;
import com.vitap.aluminireconnect.RegistrationActivity;

import java.util.HashMap;
import java.util.Map;

public class AccademicDetailsFragment extends Fragment {

    private EditText FirstName;
    private EditText LastName;
    private EditText RegistrationNumber;
    private EditText School;
    private TextInputEditText MobileNumber,EmailId;
    private Button AccademicNext;
    private FirebaseAuth mAuth;
    private final static String TAG = " AccademicDetails";
    private FirebaseFirestore db;
    private FirebaseUser user;
    DocumentReference ref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accademic_details, container, false);
        FirstName = view.findViewById(R.id.first_name);
        LastName = view.findViewById(R.id.last_name);
        RegistrationNumber = view.findViewById(R.id.registration_number);
        School = view.findViewById(R.id.school);
        MobileNumber=view.findViewById((R.id.mobile_number));
        EmailId=view.findViewById(R.id.email_id);
        AccademicNext = view.findViewById(R.id.accademic_next);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        AccademicNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
//                bundle.putString("FirstName",FirstName.getText().toString());
//                bundle.putString("LastName",LastName.getText().toString());
//                bundle.putString("RegistrationNumber",RegistrationNumber.getText().toString());
//                bundle.putString("School",School.getText().toString());
                PersonalDetailsFragment personalFragment=new PersonalDetailsFragment();
//                personalFragment.setArguments(bundle);
                HashMap UserDetails=new HashMap();
                UserDetails.put("FirstName",FirstName.getText().toString());
                UserDetails.put("LastName",LastName.getText().toString());
                UserDetails.put("RegistrationNumber",RegistrationNumber.getText().toString());
                UserDetails.put("School",School.getText().toString());
                UserDetails.put("MobileNumber",MobileNumber.getText().toString());
                UserDetails.put("EmailId",EmailId.getText().toString());
                db.collection("Users")
                        .document("6SX37hFaW8cSS4Ut8b5DaRWzXr92")
                        .set(UserDetails)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                getFragmentManager().beginTransaction().replace(R.id.frame_layout,personalFragment).commit();
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


//
//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean("FIRST_NAME", FirstName.getText().toString());
//        outState.putString("LAST_NAME", LastName);
//    }
}