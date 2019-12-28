package com.example.gentree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, email, passwd;
    private Button registerButton;
    private TextView goToLogin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        passwd = findViewById(R.id.passwd);
        registerButton = findViewById(R.id.register_button);
        goToLogin = findViewById(R.id.to_login);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentEmail = getEmail().getText().toString();
                String currentPasswd = getPasswd().getText().toString().trim();

                if (TextUtils.isEmpty(currentEmail)) {
                    email.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(currentPasswd)) {
                    passwd.setError("Password is required");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                getmAuth().createUserWithEmailAndPassword(currentEmail, currentPasswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Error occured: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public EditText getUsername() {
        return username;
    }

    public void setUsername(EditText username) {
        this.username = username;
    }

    public EditText getEmail() {
        return email;
    }

    public void setEmail(EditText email) {
        this.email = email;
    }

    public EditText getPasswd() {
        return passwd;
    }

    public void setPasswd(EditText passwd) {
        this.passwd = passwd;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public void setRegisterButton(Button registerButton) {
        this.registerButton = registerButton;
    }

    public TextView getGoToLogin() {
        return goToLogin;
    }

    public void setGoToLogin(TextView goToLogin) {
        this.goToLogin = goToLogin;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
