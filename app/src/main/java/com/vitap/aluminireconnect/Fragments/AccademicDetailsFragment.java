package com.vitap.aluminireconnect.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vitap.aluminireconnect.R;

import java.util.HashMap;
import java.util.regex.Pattern;

public class AccademicDetailsFragment extends Fragment {

    private EditText FirstName;
    private EditText LastName;
    private EditText RegistrationNumber;
    private AutoCompleteTextView School;
    private TextInputEditText MobileNumber,EmailId;
    private Button AccademicNext;
    private FirebaseAuth mAuth;
    private final static String TAG = " AccademicDetails";
    private FirebaseFirestore db;
    private FirebaseUser user;
    DocumentReference ref;

    public AccademicDetailsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = new ContextThemeWrapper(getActivity(),R.style.AppTheme);
        LayoutInflater local = inflater.cloneInContext(context);
        View view = local.inflate(R.layout.fragment_accademic_details, container, false);

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

                if(TextUtils.isEmpty(FirstName.getText().toString())){
                    FirstName.setError("Please Fill the First Name");
                }else if(TextUtils.isEmpty(LastName.getText().toString())){
                    LastName.setError("Please Fill the Last Name");
                }else if(TextUtils.isEmpty(RegistrationNumber.getText().toString())){
                    RegistrationNumber.setError("Please Fill the Registration Number");
                }else if(TextUtils.isEmpty(School.getText().toString())){
                    School.setError("Please Fill the School");
                }else if(TextUtils.isEmpty(MobileNumber.getText().toString())){
                    MobileNumber.setError("Please Fill the Mobile Number");
                }else if(TextUtils.isEmpty(EmailId.getText().toString())){
                    EmailId.setError("Please Fill the Email Id");
                }else if(validateEmail(EmailId.getText().toString())){
                    EmailId.setError("Please Give the Valid Email Id");
                }else{

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
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
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
//                End of else



            }
//            End of onClick Method
        });
//        End of Accademic Next on Click Action

        return view;
    }

//    To Validate Email Id whether the id is valid or not
    private boolean validateEmail(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}