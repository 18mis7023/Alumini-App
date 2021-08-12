package com.vitap.aluminireconnect.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vitap.aluminireconnect.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class AccademicDetailsFragment extends Fragment {

    private EditText FirstName;
    private EditText LastName;
    private EditText RegistrationNumber;
    private TextInputLayout OutlinedSchool;
    private AutoCompleteTextView School;
    private TextInputEditText MobileNumber,EmailId;
    private Button AccademicNext;
    private FirebaseAuth mAuth;
    private final static String TAG = " AccademicDetails";
    private FirebaseFirestore db;
    private FirebaseUser user;
    DocumentReference ref;
    private ProgressDialog progressDialog;

//    ArrayList<String> arraylistSchool;
//    ArrayAdapter<String> arrayAdapterSchool;

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
        OutlinedSchool=(TextInputLayout) view.findViewById(R.id.outlined_school);
        School = (AutoCompleteTextView) view.findViewById(R.id.school);
        MobileNumber=view.findViewById((R.id.mobile_number));
        EmailId=view.findViewById(R.id.email_id);
        AccademicNext = view.findViewById(R.id.accademic_next);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getContext());

        int currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                //light mode
                FirstName.setBackgroundColor(getResources().getColor(R.color.white));
                FirstName.setTextColor(getResources().getColor(R.color.black));
                LastName.setBackgroundColor(getResources().getColor(R.color.white));
                LastName.setTextColor(getResources().getColor(R.color.black));
                RegistrationNumber.setBackgroundColor(getResources().getColor(R.color.white));
                RegistrationNumber.setTextColor(getResources().getColor(R.color.black));
                School.setBackgroundColor(getResources().getColor(R.color.white));
                School.setTextColor(getResources().getColor(R.color.black));
                MobileNumber.setBackgroundColor(getResources().getColor(R.color.white));
                MobileNumber.setTextColor(getResources().getColor(R.color.black));
                EmailId.setBackgroundColor(getResources().getColor(R.color.white));
                EmailId.setTextColor(getResources().getColor(R.color.black));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //Dark mode
                FirstName.setBackgroundColor(getResources().getColor(R.color.black));
                FirstName.setTextColor(getResources().getColor(R.color.white));
                LastName.setBackgroundColor(getResources().getColor(R.color.black));
                LastName.setTextColor(getResources().getColor(R.color.white));
                RegistrationNumber.setBackgroundColor(getResources().getColor(R.color.black));
                RegistrationNumber.setTextColor(getResources().getColor(R.color.white));
                School.setBackgroundColor(getResources().getColor(R.color.black));
                School.setTextColor(getResources().getColor(R.color.white));
                MobileNumber.setBackgroundColor(getResources().getColor(R.color.black));
                MobileNumber.setTextColor(getResources().getColor(R.color.white));
                EmailId.setBackgroundColor(getResources().getColor(R.color.black));
                EmailId.setTextColor(getResources().getColor(R.color.white));
                break;
        }
        String[] schoolItems=new String[]{
                "SCOPE","SENSE",
                "SMEC","VSB",
                "VSL"
        };
        ArrayAdapter<String> schoolAdapter=new ArrayAdapter<>(
                getContext(),R.layout.dropdown_item,schoolItems
        );
        School.setAdapter(schoolAdapter);

//        arraylistSchool.add("Hello");
//        arraylistSchool.add("Hello2");
//        arraylistSchool.add("Hello3");
//        arraylistSchool.add("Hello4");
//        arraylistSchool.add("Hello5");
//        arrayAdapterSchool=new ArrayAdapter<>(context.getApplicationContext(), R.layout.support_simple_spinner_dropdown_item,arraylistSchool);
//        School.setAdapter(arrayAdapterSchool);
//        School.setThreshold(1);
//        ===how many characters required to load suggestion spinner ==


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
                }else{
                    progressDialog.show();
                    progressDialog.setMessage("Loading...");
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
                                    progressDialog.dismiss();
                                    getFragmentManager().beginTransaction().replace(R.id.frame_layout,personalFragment).commit();
//                                Toast.makeText(getContext(), "UserData Updated", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                    progressDialog.dismiss();
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