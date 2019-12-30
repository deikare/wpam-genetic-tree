package com.example.gentree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNewNodeActivity extends AppCompatActivity {

    private EditText name, lastName, dateOfBirth, dateOfDeath;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_node);



        name = findViewById(R.id.add_node_name);
        lastName = findViewById(R.id.add_node_last_name);
        dateOfBirth = findViewById(R.id.add_node_date_of_birth);
        dateOfDeath = findViewById(R.id.add_node_date_of_death);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        Button button = findViewById(R.id.add_node_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String currentName = name.getText().toString();
                final String currentLastName = lastName.getText().toString();
                final String dob = dateOfBirth.getText().toString();
                final String dod = dateOfDeath.getText().toString();

                final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                db.collection("users")
                        .whereEqualTo("userID", userID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    ArrayList<String> userList = new ArrayList<>();
                                    userList.add(userID);

                                    Map<String, String> attributes = new HashMap<>();
                                    attributes.put(NodeKeys.NAME, currentName);
                                    attributes.put(NodeKeys.LAST_NAME, currentLastName);
                                    attributes.put(NodeKeys.DATE_OF_BIRTH, dob);
                                    attributes.put(NodeKeys.DATE_OF_DEATH, dod);

                                    Map<String, Object> newNode = new HashMap<>();
                                    newNode.put("user_list", userList);
                                    newNode.put("attributes", attributes);
//                                    Node newNode = new Node(userList, attributes);

                                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                        documentSnapshot.getReference()
                                                .collection("nodes")
                                                .add(newNode)
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
                                    }
                                }
                            }
                        });
            }
        });
    }

/*
    public void addNode(View view) {
        final String currentName = name.getText().toString();
        final String currentLastName = lastName.getText().toString();
        final String dob = dateOfBirth.getText().toString();
        final String dod = dateOfDeath.getText().toString();

        final String userID = mAuth.getCurrentUser().getUid();
        db.collection("users")
                .whereEqualTo("userID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> userList = new ArrayList<>();
                            userList.add(userID);
                            Map<String, String> attributes = new HashMap<>();
                            attributes.put(NodeKeys.NAME, currentName);
                            attributes.put(NodeKeys.LAST_NAME, currentLastName);
                            attributes.put(NodeKeys.DATE_OF_BIRTH, dob);
                            attributes.put(NodeKeys.DATE_OF_DEATH, dod);
                            Node newNode = new Node(userList, attributes);

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                documentSnapshot.getReference()
                                        .collection("Nodes")
                                        .add(newNode);
                            }
                        }
                    }
                });
    }
*/
}
