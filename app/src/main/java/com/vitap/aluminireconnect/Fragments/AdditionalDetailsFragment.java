package com.vitap.aluminireconnect.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;
import com.vitap.aluminireconnect.R;


public class AdditionalDetailsFragment extends Fragment {


    private TextInputLayout outlinedTest;
    private AutoCompleteTextView test;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_additional_details, container, false);
        outlinedTest=view.findViewById(R.id.outlined_test);
        test=view.findViewById(R.id.test);
        String[] items=new String[]{
                "Item 1","Item 2",
                "Item 1","Item 2",
                "Item 1","Others"
        };
        ArrayAdapter<String> adapter=new ArrayAdapter<>(
                getContext(),R.layout.dropdown_item,items
        );
        test.setAdapter(adapter);

        return view;
    }
}