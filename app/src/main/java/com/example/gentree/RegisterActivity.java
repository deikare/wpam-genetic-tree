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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, email, passwd;
    private Button registerButton;
    private TextView goToLogin;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String currentEmail = getEmail().getText().toString();
                final String currentPasswd = getPasswd().getText().toString().trim();
                final String currentUsername = getUsername().getText().toString();

                if (TextUtils.isEmpty(currentUsername)) {
                    username.setError("Email is required");
                    return;
                }

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
                            Map<String, Object> user = new HashMap<>();

                            Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(currentUsername).build());

                            user.put("userID", Objects.requireNonNull(task.getResult().getUser()).getUid());
                            user.put("username", currentUsername);
                            user.put("email", currentEmail);
                            user.put("password", currentPasswd);

                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                            Tree userTree = new Tree();
                            Intent newIntent = new Intent(getApplicationContext(), FirstNodeActivity.class);
                            newIntent.putExtra("tree", userTree);
                            startActivity(newIntent);
                            finish();
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

    public void goToLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
