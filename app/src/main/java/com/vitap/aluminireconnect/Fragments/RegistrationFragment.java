package com.vitap.aluminireconnect.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vitap.aluminireconnect.LoginActivity;
import com.vitap.aluminireconnect.R;
import com.vitap.aluminireconnect.RegistrationActivity;
import com.vitap.aluminireconnect.SplashActivity;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationFragment extends Fragment {

    public TextInputEditText Email, Passwd, ConformPasswd;
    public Button Create;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        Email = view.findViewById(R.id.alumini_email);
        Passwd = view.findViewById(R.id.password);
        ConformPasswd = view.findViewById(R.id.conform_password);
        Create = view.findViewById(R.id.sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(getContext());
        Create.setOnClickListener(v -> {
            if (Email.getText().toString().trim().isEmpty() ||
                    Passwd.getText().toString().trim().isEmpty() ||
                    ConformPasswd.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Invalid details", Toast.LENGTH_SHORT).show();
            } else {
                String email = Email.getText().toString().trim();
                String password = Passwd.getText().toString().trim();
                String conformPassword = ConformPasswd.getText().toString().trim();
                if (password.length() <= 6) {
                    Toast.makeText(getContext(), "password must  be more then 6 characters", Toast.LENGTH_SHORT).show();
                } else if (password.equals(conformPassword)) {
                    if (isValidPassword(password)) {
                        CreateNewUser(email, password);
                    }else {
                        Toast.makeText(getContext(), "password must contains one spacial,number and capital letters", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Password not matched", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;


    }

    public void CreateNewUser(String Email, String Password) {
        progressDialog.show();
        progressDialog.setMessage("Creating Account...");

        mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification();
                Toast.makeText(getContext(), "Please verify email and Login", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
            } else {
                progressDialog.dismiss();
                Log.e("Registration error : ", Objects.requireNonNull(task.getException()).toString());
                if (task.getException().toString().contains("already")) {
                    Toast.makeText(getContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getContext(), "Error : "+task.getException(), Toast.LENGTH_SHORT).show();

            }

        });
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }


}