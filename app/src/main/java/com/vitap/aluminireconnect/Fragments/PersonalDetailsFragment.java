package com.vitap.aluminireconnect.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vitap.aluminireconnect.R;


public class PersonalDetailsFragment extends Fragment {

    private EditText FatherName;
    private EditText FatherMobileNumber;
    private Button PersonalNext;
    private TextView temp1,temp2,temp3,temp4;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_personal_details, container, false);
        FatherName=view.findViewById(R.id.father_name);
        FatherMobileNumber=view.findViewById(R.id.father_mobile_number);
        PersonalNext=view.findViewById(R.id.personal_next);
        Bundle bundle=this.getArguments();
        System.out.println(bundle.getString("FirstName"));
        System.out.println(bundle.getString("LastName"));
        System.out.println(bundle.getString("RegistrationNumber"));
        System.out.println(bundle.getString("School"));

//        temp1=view.findViewById(R.id.temp1);
//        temp2=view.findViewById(R.id.temp2);
//        temp3=view.findViewById(R.id.temp3);
//        temp4=view.findViewById(R.id.temp4);
//        temp1.setText(bundle.getString("FirstName"));
//        temp2.setText(bundle.getString("LastName"));
//        temp3.setText(bundle.getString("RegistrationNumber"));
//        temp4.setText(bundle.getString("School"));
        PersonalNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        return view;
    }
}