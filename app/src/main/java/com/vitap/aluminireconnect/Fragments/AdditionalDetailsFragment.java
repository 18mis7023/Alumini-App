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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vitap.aluminireconnect.HomeActivity;
import com.vitap.aluminireconnect.R;

import java.util.HashMap;


public class AdditionalDetailsFragment extends Fragment {


    private TextInputLayout outlinedPlacedOrNot,outlinedPlacedOrNotIfYes,outlinedHigherEducationIfYes,outlinedAttendedAnyCompetativeExamIfYes,outlinedInvolvedInAnyStartupIfYes;
    private AutoCompleteTextView placedOrNot,higherEducation,attendedAnyCompetativeExam,involvedInAnyStartup;
    private TextInputEditText placedOrNotIfYes,higherEducationIfYes,attendedAnyCompetativeExamIfYes,involvedInAnyStartupIfYes;
    private TextInputEditText feedbackOnCircullum,feedbackOnCampus;
    private Button additionalBack,additionalSubmit;
    private FirebaseAuth mAuth;
    private final static String TAG = " Additional Details";
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_additional_details, container, false);
        outlinedPlacedOrNot=view.findViewById(R.id.outlined_placed_or_not);
        outlinedPlacedOrNotIfYes=view.findViewById(R.id.outlined_placed_or_not_if_yes);
        outlinedHigherEducationIfYes=view.findViewById(R.id.outlined_higher_education_if_yes);
        outlinedAttendedAnyCompetativeExamIfYes=view.findViewById(R.id.outlined_attended_any_competative_exam_if_yes);
        outlinedInvolvedInAnyStartupIfYes=view.findViewById(R.id.outlined_involved_in_any_startup_if_yes);
        placedOrNot=view.findViewById(R.id.placed_or_not);
        higherEducation=view.findViewById(R.id.higher_education);
        attendedAnyCompetativeExam=view.findViewById(R.id.attended_any_competative_exam);
        involvedInAnyStartup=view.findViewById(R.id.involved_in_any_startup);
        placedOrNotIfYes=view.findViewById(R.id.placed_or_not_if_yes);
        higherEducationIfYes=view.findViewById(R.id.higher_education_if_yes);
        attendedAnyCompetativeExamIfYes=view.findViewById(R.id.attended_any_competative_exam_if_yes);
        involvedInAnyStartupIfYes=view.findViewById(R.id.involved_in_any_startup_if_yes);
        additionalBack=view.findViewById(R.id.additional_back);
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
                placedOrNotIfYes.setBackgroundColor(getResources().getColor(R.color.black));
                placedOrNotIfYes.setTextColor(getResources().getColor(R.color.white));
                higherEducationIfYes.setBackgroundColor(getResources().getColor(R.color.black));
                higherEducationIfYes.setTextColor(getResources().getColor(R.color.white));
                attendedAnyCompetativeExamIfYes.setBackgroundColor(getResources().getColor(R.color.black));
                attendedAnyCompetativeExamIfYes.setTextColor(getResources().getColor(R.color.white));
                involvedInAnyStartupIfYes.setBackgroundColor(getResources().getColor(R.color.black));
                involvedInAnyStartupIfYes.setTextColor(getResources().getColor(R.color.white));
                feedbackOnCircullum.setBackgroundColor(getResources().getColor(R.color.black));
                feedbackOnCircullum.setTextColor(getResources().getColor(R.color.white));
                feedbackOnCampus.setBackgroundColor(getResources().getColor(R.color.black));
                feedbackOnCampus.setTextColor(getResources().getColor(R.color.white));
                break;
        }
        String[] placedOrNotStr=new String[]{
                "Yes","No"
        };
        ArrayAdapter<String> placedOrNotAdapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_item,placedOrNotStr);
        placedOrNot.setAdapter(placedOrNotAdapter);
        placedOrNot.setOnItemClickListener((adapterView, view16, i, l) -> {
            String placedText=placedOrNot.getText().toString();
            if(placedText.matches("Yes")){
                outlinedPlacedOrNotIfYes.setVisibility(View.VISIBLE);
            }else{
                outlinedPlacedOrNotIfYes.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), placedOrNot.getText().toString(), Toast.LENGTH_SHORT).show();
        });
        String[] higherEducationStr=new String[]{
                "Yes","No"
        };
        ArrayAdapter<String> higherEducationAdapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_item,higherEducationStr);
        higherEducation.setAdapter(higherEducationAdapter);
        higherEducation.setOnItemClickListener((adapterView, view15, i, l) -> {
            String educationText=higherEducation.getText().toString();
            if(educationText.matches("Yes")){
                outlinedHigherEducationIfYes.setVisibility(View.VISIBLE);
            }else{
                outlinedHigherEducationIfYes.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), higherEducation.getText().toString(), Toast.LENGTH_SHORT).show();
        });
        String[] attendedAnyCompetativeExamStr=new String[]{
                "Yes","No"
        };
        ArrayAdapter<String> attendedAnyCompetativeExamAdapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_item,attendedAnyCompetativeExamStr);
        attendedAnyCompetativeExam.setAdapter(attendedAnyCompetativeExamAdapter);
        attendedAnyCompetativeExam.setOnItemClickListener((adapterView, view14, i, l) -> {
            String competativeExamText=attendedAnyCompetativeExam.getText().toString();
            if(competativeExamText.matches("Yes")){
                outlinedAttendedAnyCompetativeExamIfYes.setVisibility(View.VISIBLE);
            }else{
                outlinedAttendedAnyCompetativeExamIfYes.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), attendedAnyCompetativeExam.getText().toString(), Toast.LENGTH_SHORT).show();
        });
        String[] involvedInAnyStartupStr=new String[]{
                "Yes","No"
        };
        ArrayAdapter<String> involvedInAnyStartupAdapter=new ArrayAdapter<>(getContext(),R.layout.dropdown_item,involvedInAnyStartupStr);
        involvedInAnyStartup.setAdapter(involvedInAnyStartupAdapter);
        involvedInAnyStartup.setOnItemClickListener((adapterView, view13, i, l) -> {
            String startUpText=involvedInAnyStartup.getText().toString();
            if(startUpText.matches("Yes")){
                outlinedInvolvedInAnyStartupIfYes.setVisibility(View.VISIBLE);
            }else{
                outlinedInvolvedInAnyStartupIfYes.setVisibility(View.GONE);
            }
            Toast.makeText(getActivity(), involvedInAnyStartup.getText().toString(), Toast.LENGTH_SHORT).show();
        });

        additionalBack.setOnClickListener(view12 ->
                getFragmentManager().beginTransaction().replace(R.id.frame_layout,new PersonalDetailsFragment()).commit()
        );

        additionalSubmit.setOnClickListener(view1 -> {

            if (CheckDetails()) {
                progressDialog.show();
                progressDialog.setMessage("Loading...");
                HashMap<String, Object> AdditionalDetails = new HashMap<String, Object>();
                AdditionalDetails.put("PlacedOrNot", placedOrNot.getText().toString());
                AdditionalDetails.put("HigherEducation", higherEducation.getText().toString());
                AdditionalDetails.put("CompetativeExam", attendedAnyCompetativeExam.getText().toString());
                AdditionalDetails.put("Startup", involvedInAnyStartup.getText().toString());
                AdditionalDetails.put("FeedbackOnCurriculum", feedbackOnCircullum.getText().toString());
                AdditionalDetails.put("FeedbackOnCampus", feedbackOnCampus.getText().toString());
                if(placedOrNot.getText().toString().equals("Yes"))
                {
                    AdditionalDetails.put("WherePlaced",placedOrNotIfYes.getText().toString());
                }
                if(higherEducation.getText().toString().equals("Yes"))
                {
                    AdditionalDetails.put("WhereHigherEducation",higherEducationIfYes.getText().toString());
                }
                if(attendedAnyCompetativeExam.getText().toString().equals("Yes"))
                {
                    AdditionalDetails.put("WhereCompetativeExam",attendedAnyCompetativeExamIfYes.getText().toString());
                }
                if(involvedInAnyStartup.getText().toString().equals("Yes"))
                {
                    AdditionalDetails.put("WhereStartup",involvedInAnyStartupIfYes.getText().toString());
                }

                FirebaseFirestore.getInstance().collection("Users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update(AdditionalDetails)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            PreferenceManager.getDefaultSharedPreferences(getContext())
                                    .edit().putBoolean("AllDetailsAvailable", true).apply();
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            Log.w(TAG, "Error writing document", e);
                        Toast.makeText(getContext(), "Error : "+e, Toast.LENGTH_SHORT).show();
                        });


//

//                }
//                End of else



            }
//            End of onClick Method
        });
        return view;
    }


    public boolean CheckDetails(){

        boolean details = true;
        if(TextUtils.isEmpty(feedbackOnCampus.getText().toString())){
            feedbackOnCampus.setError("Please enter feedback on campus");
            details = false;
        }else if(TextUtils.isEmpty(feedbackOnCircullum.getText().toString())){
            feedbackOnCircullum.setError("Please enter feedback on circullum");
            details = false;
        } else if(TextUtils.isEmpty(placedOrNot.getText().toString())){
            placedOrNot.setError("Please select placed or not");
            details = false;
        }else if(TextUtils.isEmpty(higherEducation.getText().toString())){
            higherEducation.setError("Please select higher education");
            details = false;
        }
        else if(TextUtils.isEmpty(placedOrNot.getText().toString())){
            placedOrNot.setError("Please select placed or not");
            details = false;
        }else if(TextUtils.isEmpty(higherEducation.getText().toString())){
            higherEducation.setError("Please select higher education");
            details = false;
        } else {
            if (placedOrNot.getText().toString().matches("Yes")){
                if(TextUtils.isEmpty(attendedAnyCompetativeExam.getText().toString())) {
                    attendedAnyCompetativeExam.setError("Please select competative exam");
                    details = false;
                }
            }else if (higherEducation.getText().toString().matches("Yes")){
                if(TextUtils.isEmpty(involvedInAnyStartup.getText().toString())) {
                    involvedInAnyStartup.setError("Please select involved in startup");
                    details = false;
                }
            }else if (attendedAnyCompetativeExam.getText().toString().matches("Yes")){
                if(TextUtils.isEmpty(attendedAnyCompetativeExamIfYes.getText().toString())) {
                    attendedAnyCompetativeExamIfYes.setError("Enter competative exam details");
                    details = false;
                }
            }else  if (involvedInAnyStartup.getText().toString().matches("Yes")){
                if(TextUtils.isEmpty(involvedInAnyStartupIfYes.getText().toString())) {
                    involvedInAnyStartupIfYes.setError("Please enter startup details");
                    details = false;
                }
            }
        }
         return details;
    }

}