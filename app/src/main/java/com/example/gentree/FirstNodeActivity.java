package com.example.gentree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class FirstNodeActivity extends AppCompatActivity {
    private EditText name, lastName, dateOfBirth, dateOfDeath, work, location, education, description;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_node);

        name = findViewById(R.id.first_add_node_name);
        lastName = findViewById(R.id.first_add_node_last_name);
        dateOfBirth = findViewById(R.id.first_add_node_date_of_birth);
        dateOfDeath = findViewById(R.id.first_add_node_date_of_death);
        work = findViewById(R.id.first_add_node_work);
        location = findViewById(R.id.first_add_node_location);
        education = findViewById(R.id.first_add_node_education);
        description = findViewById(R.id.first_add_node_description);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        Button button = findViewById(R.id.first_add_node_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String currentName = name.getText().toString();
                final String currentLastName = lastName.getText().toString();
                final String dob = dateOfBirth.getText().toString();
                final String dod = dateOfDeath.getText().toString();
                final String currentWork = work.getText().toString();
                final String currentEducation = education.getText().toString();
                final String currentLocation = location.getText().toString();
                final String currentDescription = description.getText().toString();

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
                                    attributes.put(NodeKeys.EDUCATION, currentEducation);
                                    attributes.put(NodeKeys.WORK, currentWork);
                                    attributes.put(NodeKeys.LOCATION, currentLocation);
                                    attributes.put(NodeKeys.DESCRIPTION, currentDescription);

                                    Map<String, String> nodea = new HashMap<>();
                                    nodea.put("d", "d");
                                    Node newNode = new Node(attributes, 0);
                                    Intent i = getIntent();
                                    Tree userTree = (Tree)i.getSerializableExtra("tree");
                                    userTree.AddPatron(null, newNode);

                                    Map<String, String> treeSnap = new HashMap<>();
                                    Node newNode2 = new Node(nodea, 1);
                                    userTree.AddPatron(newNode, newNode2);
                                    String result = new String(userTree.toJson());
//                                    treeSnap.put("data", userTree.toJson());
                                    for (Node node : userTree.getGraph().vertexSet())
                                        treeSnap.put("node" + node.getNumber(), node.toJson(node.getNumberofParent()));
                                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                        documentSnapshot.getReference()
                                                .collection("treeSnap")
                                                .add(treeSnap)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(newIntent);
                                                        finish();
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
}
