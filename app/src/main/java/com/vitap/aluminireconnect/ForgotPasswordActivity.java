package com.vitap.aluminireconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "Forgot password";
    private TextInputEditText Email;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Email = findViewById(R.id.email);
        Button send = findViewById(R.id.send);
        progressDialog = new ProgressDialog(this);

        int currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                //light mode
                Email.setBackgroundColor(getResources().getColor(R.color.white));
                Email.setTextColor(getResources().getColor(R.color.black));

                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //Dark mode
                Email.setBackgroundColor(getResources().getColor(R.color.black));
                Email.setTextColor(getResources().getColor(R.color.white));

                break;
        }


        send.setOnClickListener(view -> {

            if (!Email.getText().toString().trim().isEmpty()){

                progressDialog.setMessage("Sending...");
                progressDialog.show();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = Email.getText().toString().trim();
                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(task -> {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Email sent.");
                            }else {
                                String Error = task.getException().toString().trim();
                                Log.e(TAG,""+Error);
                                if (Error.contains("Badly")) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                                }else if (Error.contains("no user")){
                                    Toast.makeText(ForgotPasswordActivity.this, "Email not find. Please Register first", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ForgotPasswordActivity.this, "Error : "+Error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }else {
                Email.setError("Enter registered email");
            }
        });


    }
}