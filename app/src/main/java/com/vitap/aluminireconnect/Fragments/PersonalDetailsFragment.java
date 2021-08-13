package com.vitap.aluminireconnect.Fragments;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vitap.aluminireconnect.R;

import java.util.HashMap;


public class PersonalDetailsFragment extends Fragment {

    private TextInputEditText FatherName, FatherMobileNumber;
    private TextInputEditText MotherName,MotherMobileNumber,PermanentAddress;
    private Button PersonalNext,PersonalBack;
    private FirebaseAuth mAuth;
    private final static String TAG = "PersonalDetails";
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    private FirebaseUser user;
    private RelativeLayout personalRelativeLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_personal_details, container, false);
        FatherName=view.findViewById(R.id.father_name);
        FatherMobileNumber=view.findViewById(R.id.father_mobile_number);
        MotherName=view.findViewById(R.id.mother_name);
        MotherMobileNumber=view.findViewById(R.id.mother_mobile_number);
        PermanentAddress=view.findViewById(R.id.permanent_address);
        PersonalNext=view.findViewById(R.id.personal_next);
        PersonalBack=view.findViewById(R.id.personal_back);
        personalRelativeLayout=view.findViewById(R.id.personal_relative_layout);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());

        int currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                //light mode
                FatherName.setBackgroundColor(getResources().getColor(R.color.white));
                FatherName.setTextColor(getResources().getColor(R.color.black));
                FatherMobileNumber.setBackgroundColor(getResources().getColor(R.color.white));
                FatherMobileNumber.setTextColor(getResources().getColor(R.color.black));
                MotherName.setBackgroundColor(getResources().getColor(R.color.white));
                MotherName.setTextColor(getResources().getColor(R.color.black));
                MotherMobileNumber.setBackgroundColor(getResources().getColor(R.color.white));
                MotherMobileNumber.setTextColor(getResources().getColor(R.color.black));
                PermanentAddress.setBackgroundColor(getResources().getColor(R.color.white));
                PermanentAddress.setTextColor(getResources().getColor(R.color.black));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //Dark mode
                FatherName.setBackgroundColor(getResources().getColor(R.color.black));
                FatherName.setTextColor(getResources().getColor(R.color.white));
                FatherMobileNumber.setBackgroundColor(getResources().getColor(R.color.black));
                FatherMobileNumber.setTextColor(getResources().getColor(R.color.white));
                MotherName.setBackgroundColor(getResources().getColor(R.color.black));
                MotherName.setTextColor(getResources().getColor(R.color.white));
                MotherMobileNumber.setBackgroundColor(getResources().getColor(R.color.black));
                MotherMobileNumber.setTextColor(getResources().getColor(R.color.white));
                PermanentAddress.setBackgroundColor(getResources().getColor(R.color.black));
                PermanentAddress.setTextColor(getResources().getColor(R.color.white));
                break;
        }

        PersonalBack.setOnClickListener(view12 ->
                getFragmentManager().beginTransaction().replace(R.id.frame_layout,new AccademicDetailsFragment()).commit()
        );
        PersonalNext.setOnClickListener(view1 -> {

            if(TextUtils.isEmpty(FatherName.getText().toString())){
                FatherName.setError("Please Fill the father name");
            }else if(TextUtils.isEmpty(FatherMobileNumber.getText().toString())){
                FatherMobileNumber.setError("Please Fill the father number");
            }else if(TextUtils.isEmpty(MotherMobileNumber.getText().toString())){
                MotherMobileNumber.setError("Please Fill the mother number");
            }else if(TextUtils.isEmpty(MotherName.getText().toString())){
                MotherName.setError("Please Fill the mother name");
            }else if(TextUtils.isEmpty(PermanentAddress.getText().toString())){
                PermanentAddress.setError("Please Fill the permanent address");
            }else{
                progressDialog.show();
                progressDialog.setMessage("Loading...");
                HashMap UserDetails=new HashMap();
                UserDetails.put("FatherName",FatherName.getText().toString());
                UserDetails.put("FathersMobileNumber",FatherMobileNumber.getText().toString());
                UserDetails.put("MotherName",MotherName.getText().toString());
                UserDetails.put("MothersMobileNumber",MotherMobileNumber.getText().toString());
                UserDetails.put("PermanentAddress",PermanentAddress.getText().toString());

                db.collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update(UserDetails)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                progressDialog.dismiss();
                                getFragmentManager().beginTransaction().replace(R.id.frame_layout,new AdditionalDetailsFragment()).commit();
//                                Toast.makeText(getContext(), "UserData Updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Log.w(TAG, "Error writing document", e);
                                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getContext(), "Error : "+e, Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        });
        return view;
    }
}