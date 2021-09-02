package com.vitap.aluminireconnect;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText Email, Passwd;
    private Button Login;
    private TextView ForgotPasswd,Register;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email = findViewById(R.id.email);
        Passwd = findViewById(R.id.passwd);
        Login = findViewById(R.id.log_in);
        ForgotPasswd = findViewById(R.id.forgot_passwd_text);
        Register = findViewById(R.id.register_text);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        mAuth = FirebaseAuth.getInstance();
        ConstraintLayout background = findViewById(R.id.background);

       /* int currentNightMode = this.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                //light mode
                background.setBackgroundResource(R.drawable.lightnormalpattern);
                Email.setBackgroundColor(getResources().getColor(R.color.white));
                Email.setTextColor(getResources().getColor(R.color.black));
                Passwd.setBackgroundColor(getResources().getColor(R.color.white));
                Passwd.setTextColor(getResources().getColor(R.color.black));
                ForgotPasswd.setTextColor(getResources().getColor(R.color.black));
                Register.setTextColor(getResources().getColor(R.color.black));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                //Dark mode
                Email.setBackgroundColor(getResources().getColor(R.color.black));
                Email.setTextColor(getResources().getColor(R.color.white));
                Passwd.setBackgroundColor(getResources().getColor(R.color.black));
                Passwd.setTextColor(getResources().getColor(R.color.white));
                background.setBackgroundResource(R.drawable.dark_background);
                ForgotPasswd.setTextColor(getResources().getColor(R.color.white));
                Register.setTextColor(getResources().getColor(R.color.white));
                break;
        }

        */

        MaterialCardView BackCard = findViewById(R.id.back_card);
        BackCard.setOnClickListener(view -> {
                finishAffinity();
                });
        Login.setOnClickListener(v -> {
            if (Email.getText().toString().isEmpty() || Passwd.getText().toString().isEmpty()){
                Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            }else {
                StartLogin(Email.getText().toString(),Passwd.getText().toString());
            }
        });

        Register.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class))
        );
        ForgotPasswd.setOnClickListener(v -> {
            //Send to forgot password activity
            startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
        });


    }

    private void StartLogin(String email,String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("Login ", "signInWithEmail:success");
                        Toast.makeText(LoginActivity.this, "LogIn success", Toast.LENGTH_SHORT).show();
                        PreferenceManager.getDefaultSharedPreferences(this)
                                .edit()
                                .putBoolean("AllDetailsAvailable",false)
                                .apply();
                        if (mAuth.getCurrentUser().isEmailVerified()){
                            boolean isAvailable = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("AllDetailsAvailable",false);
                            if (!isAvailable){
                                isAllDetailsAvailable();
                            }else{
                                //Send user to Feed Activity
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                finish();
                            }
                        }else {
                            EmailVerification();
                            progressDialog.dismiss();
                        }

                    } else {
                        progressDialog.dismiss();
                        Log.d("login", "signInWithEmail:failure", task.getException());
                        if (task.getException().toString().contains("no user")){
                            Toast.makeText(LoginActivity.this, "Account not found please sign-up", Toast.LENGTH_SHORT).show();
                        }else if (task.getException().toString().contains("password")){
                            Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(LoginActivity.this, "Authentication failed."+task.getException(),Toast.LENGTH_SHORT).show();
                    }

                });
    }
    private void isAllDetailsAvailable(){
        progressDialog.setMessage("Getting data...");
        FirebaseFirestore.getInstance().collection("Users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()){
                        //checking all fields are available
                        try {
                            String Name = snapshot.get("FirstName").toString();
                            String FatherName = snapshot.get("FatherName").toString();
                            String Placed = snapshot.get("PlacedOrNot").toString();

                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            finish();

                        }catch (Exception e){
                            Log.e("Details error : ",e.toString());
                            Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    progressDialog.dismiss();
                });

    }

    public void EmailVerification(){
        new MaterialAlertDialogBuilder(this)
                .setMessage("Please verify your email address and come back.")
                .setTitle("Email not verified")
                .setPositiveButton("Resend", (dialogInterface, i) -> {
                    mAuth.getCurrentUser().sendEmailVerification();
                    mAuth.signOut();
                    dialogInterface.dismiss();
                }).setNeutralButton("ok",(dialogInterface, i) -> {
                dialogInterface.dismiss();
                mAuth.signOut();
                }).show();
    }
}