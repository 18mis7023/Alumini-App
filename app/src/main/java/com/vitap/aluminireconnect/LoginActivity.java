package com.vitap.aluminireconnect;


import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

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
        });


    }

    private void StartLogin(String email,String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("Login ", "signInWithEmail:success");
                        Toast.makeText(LoginActivity.this, "LogIn success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this,RegistrationActivity.class));
                    } else {
                        Log.d("login", "signInWithEmail:failure", task.getException());
                        if (task.getException().toString().contains("no user")){
                            Toast.makeText(LoginActivity.this, "Account not found please sign-up", Toast.LENGTH_SHORT).show();
                        }else if (task.getException().toString().contains("password")){
                            Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                        }else Toast.makeText(LoginActivity.this, "Authentication failed."+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                });
    }

}