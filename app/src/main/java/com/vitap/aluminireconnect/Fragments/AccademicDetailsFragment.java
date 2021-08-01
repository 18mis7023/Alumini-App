package com.vitap.aluminireconnect.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vitap.aluminireconnect.R;

public class AccademicDetailsFragment extends Fragment {

    private EditText FirstName;
    private EditText LastName;
    private EditText RegistrationNumber;
    private EditText School;
    private Button AccademicNext;
    private SharedPreferences AccademicDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accademic_details, container, false);
        FirstName = view.findViewById(R.id.first_name);
        LastName = view.findViewById(R.id.last_name);
        RegistrationNumber = view.findViewById(R.id.registration_number);
        School = view.findViewById(R.id.school);
        AccademicNext = view.findViewById(R.id.accademic_next);
        AccademicNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("FirstName",FirstName.getText().toString());
                bundle.putString("LastName",LastName.getText().toString());
                bundle.putString("RegistrationNumber",RegistrationNumber.getText().toString());
                bundle.putString("School",School.getText().toString());
                PersonalDetailsFragment personalFragment=new PersonalDetailsFragment();
                personalFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frame_layout,personalFragment).commit();
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