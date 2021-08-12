package com.vitap.aluminireconnect.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vitap.aluminireconnect.HomeActivity;
import com.vitap.aluminireconnect.R;

import java.util.HashMap;


public class AdditionalDetailsFragment extends Fragment {


    private TextInputLayout outlinedPlacedOrNot,outlinedHigherEducation,outlinedAttendedAnyCompetativeExam,outlinedInvolvedInAnyStartup;
    private AutoCompleteTextView placedOrNot,higherEducation,attendedAnyCompetativeExam,involvedInAnyStartup;
    private TextInputEditText feedbackOnCircullum,feedbackOnCampus;
    private Button personalBack,additionalSubmit;
    private FirebaseAuth mAuth;
    private final static String TAG = " Additional Details";
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_additional_details, container, false);
        outlinedPlacedOrNot=view.findViewById(R.id.outlined_placed_or_not);
        outlinedHigherEducation=view.findViewById(R.id.outlined_higher_education);
        outlinedAttendedAnyCompetativeExam=view.findViewById(R.id.outlined_attended_any_competative_exam);
        outlinedInvolvedInAnyStartup=view.findViewById(R.id.outlined_involved_in_any_startup);
        placedOrNot=view.findViewById(R.id.placed_or_not);
        higherEducation=view.findViewById(R.id.higher_education);
        attendedAnyCompetativeExam=view.findViewById(R.id.attended_any_competative_exam);
        involvedInAnyStartup=view.findViewById(R.id.involved_in_any_startup);
        personalBack=view.findViewById(R.id.personal_back);
        additionalSubmit=view.findViewById(R.id.additional_submit);
        progressDialog = new ProgressDialog(getContext());
        feedbackOnCampus = view.findViewById(R.id.feedback_on_campus);
        feedbackOnCircullum = view.findViewById(R.id.feedback_on_circullum);

        int currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                //light mode
                placedOrNot.setBackgroundColor(getResources().getColor(R.color.white));
                placedOrNot.setTextColor(getResources().getColor(R.color.black));
                higherEducation.setBackgroundColor(getResources().getColor(R.color.white));
                higherEducation.setTextColor(getResources().getColor(R.color.black));
                attendedAnyCompetativeExam.setBackgroundColor(getResources().getColor(R.color.white));
                attendedAnyCompetativeExam.setTextColor(getResources().getColor(R.color.black));
                involvedInAnyStartup.setBackgroundColor(getResources().getColor(R.color.white));
                involvedInAnyStartup.setTextColor(getResources().getColor(R.color.black));
                feedbackOnCircullum.setBackgroundColor(getResources().getColor(R.color.white));
                feedbackOnCircullum.setTextColor(getResources().getColor(R.color.black));
                feedbackOnCampus.setBackgroundColor(getResources().getColor(R.color.white));
                feedbackOnCampus.setTextColor(getResources().getColor(R.color.black));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //Dark mode
                placedOrNot.setBackgroundColor(getResources().getColor(R.color.black));
                placedOrNot.setTextColor(getResources().getColor(R.color.white));
                higherEducation.setBackgroundColor(getResources().getColor(R.color.black));
                higherEducation.setTextColor(getResources().getColor(R.color.white));
                attendedAnyCompetativeExam.setBackgroundColor(getResources().getColor(R.color.black));
                attendedAnyCompetativeExam.setTextColor(getResources().getColor(R.color.white));
                involvedInAnyStartup.setBackgroundColor(getResources().getColor(R.color.black));
                involvedInAnyStartup.setTextColor(getResources().getColor(R.color.white));
                feedbackOnCircullum.setBackgroundColor(getResources().getColor(R.color.black));
                feedbackOnCircullum.setTextColor(getResources().getColor(R.color.white));
                feedbackOnCampus.setBackgroundColor(getResources().getColor(R.color.black));
                feedbackOnCampus.setTextColor(getResources().getColor(R.color.white));
                break;
        }
        String[] placedOrNotStr=new String[]{
                "Item 1","Item 2",
                "Item 1","Item 2",
                "Item 1","Others"
        };
        ArrayAdapter<String> placedOrNotAdapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_item,placedOrNotStr);
        placedOrNot.setAdapter(placedOrNotAdapter);
        String[] higherEducationStr=new String[]{
                "Item 1","Item 2",
                "Item 1","Item 2",
                "Item 1","Others"
        };
        ArrayAdapter<String> higherEducationAdapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_item,higherEducationStr);
        higherEducation.setAdapter(higherEducationAdapter);
        String[] attendedAnyCompetativeExamStr=new String[]{
                "Item 1","Item 2",
                "Item 1","Item 2",
                "Item 1","Others"
        };
        ArrayAdapter<String> attendedAnyCompetativeExamAdapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_item,attendedAnyCompetativeExamStr);
        attendedAnyCompetativeExam.setAdapter(attendedAnyCompetativeExamAdapter);
        String[] involvedInAnyStartupStr=new String[]{
                "Item 1","Item 2",
                "Item 1","Item 2",
                "Item 1","Others"
        };
        ArrayAdapter<String> involvedInAnyStartupAdapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_item,involvedInAnyStartupStr);
        involvedInAnyStartup.setAdapter(involvedInAnyStartupAdapter);
        personalBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frame_layout,new PersonalDetailsFragment()).commit();

            }
        });
        additionalSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if(TextUtils.isEmpty(FirstName.getText().toString())){
//                    FirstName.setError("Please Fill the First Name");
//                }else if(TextUtils.isEmpty(LastName.getText().toString())){
//                    LastName.setError("Please Fill the Last Name");
//                }else if(TextUtils.isEmpty(RegistrationNumber.getText().toString())){
//                    RegistrationNumber.setError("Please Fill the Registration Number");
//                }else if(TextUtils.isEmpty(School.getText().toString())){
//                    School.setError("Please Fill the School");
//                }else if(TextUtils.isEmpty(MobileNumber.getText().toString())){
//                    MobileNumber.setError("Please Fill the Mobile Number");
//                }else{
//
//                    Bundle bundle=new Bundle();
////                bundle.putString("FirstName",FirstName.getText().toString());
////                bundle.putString("LastName",LastName.getText().toString());
////                bundle.putString("RegistrationNumber",RegistrationNumber.getText().toString());
////                bundle.putString("School",School.getText().toString());
//                    PersonalDetailsFragment personalFragment=new PersonalDetailsFragment();
////                personalFragment.setArguments(bundle);
//
                progressDialog.show();
                progressDialog.setMessage("Loading...");
                    HashMap AdditionalDetails=new HashMap();
                    AdditionalDetails.put("PlacedOrNot",placedOrNot.getText().toString());
                    AdditionalDetails.put("HigherEducation",higherEducation.getText().toString());
                    AdditionalDetails.put("CompetativeExam",attendedAnyCompetativeExam.getText().toString());
                    AdditionalDetails.put("Startup",involvedInAnyStartup.getText().toString());
                    AdditionalDetails.put("FeedbackOnCurriculum",feedbackOnCircullum.getText().toString());
                    AdditionalDetails.put("FeedbackOnCampus",feedbackOnCampus.getText().toString());
//
                    FirebaseFirestore.getInstance().collection("Users")
                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .update(AdditionalDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    PreferenceManager.getDefaultSharedPreferences(getContext())
                                            .edit().putBoolean("AllDetailsAvailable", true).apply();
                                    Intent intent=new Intent(getActivity(), HomeActivity.class);
                                    startActivity(intent);
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
//                }
//                End of else



            }
//            End of onClick Method
        });


        return view;
    }
}