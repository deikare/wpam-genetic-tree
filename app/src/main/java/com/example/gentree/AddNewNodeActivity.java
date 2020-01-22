package com.example.gentree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    private EditText name, lastName, dateOfBirth, dateOfDeath, work, location, education, description, parentNumber;
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
//        work = findViewById(R.id.add_node_work);
        location = findViewById(R.id.add_node_location);
//        education = findViewById(R.id.add_node_education);
//        description = findViewById(R.id.add_node_description);
        parentNumber = findViewById(R.id.add_node_parentNo);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        Button button = findViewById(R.id.add_node_button);

        /*button.setOnClickListener(new View.OnClickListener() {
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
                final String currentParentNo = parentNumber.getText().toString();

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

                                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                        documentSnapshot.getReference()
                                                .collection("treeSnap")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            ArrayList<Node> nodes = new ArrayList<>();
                                                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                                                nodes.add(documentSnapshot.toObject(Node.class));
                                                            }
                                                            Tree newTree = Tree.treeFromNodesArray(nodes);
                                                            int no1 = newTree.getMaxNodeNumber() + 1;
                                                            Node newNode = new Node(attributes, no1);
                                                            no1 = Integer.parseInt(currentParentNo);
                                                            newNode.setNumberofParent(no1);

                                                            db.collection("users")
                                                                    .whereEqualTo("userID", userID)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                                                                    documentSnapshot.getReference()
                                                                                            .collection("treeSnap")
                                                                                            .add(newNode)
                                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                @Override
                                                                                                public void onSuccess(DocumentReference documentReference) {
                                                                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                                                    }
                                                });
                                    }




                                }
                            }
                        });
            }
        });*/
    }

    public void addNode(View view) {
        final String currentName = name.getText().toString();
        final String currentLastName = lastName.getText().toString();
        final String dob = dateOfBirth.getText().toString();
        final String dod = dateOfDeath.getText().toString();
//        final String currentWork = work.getText().toString();
//        final String currentEducation = education.getText().toString();
        final String currentLocation = location.getText().toString();
//        final String currentDescription = description.getText().toString();
        final String currentParentNo = parentNumber.getText().toString();

        Map<String, String> attributes = new HashMap<>();

        if (TextUtils.isEmpty(currentName)) {
            name.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(currentLastName)) {
            lastName.setError("Last name is required");
            return;
        }

        if (TextUtils.isEmpty(dob)) {
            dateOfBirth.setError("Date of birth is required");
            return;
        }
        else {
            if (!TextUtils.isDigitsOnly(dob)) {
                dateOfBirth.setError("Bad format of field");
                return;
            }
        }

        attributes.put(NodeKeys.NAME, currentName);
        attributes.put(NodeKeys.LAST_NAME, currentLastName);
        attributes.put(NodeKeys.DATE_OF_BIRTH, dob);

        if (!TextUtils.isEmpty(dod)) {
            if (!TextUtils.isDigitsOnly(dod)) {
                dateOfDeath.setError("Bad format of field");
                return;
            }
            else attributes.put(NodeKeys.DATE_OF_DEATH, dod);
        }

        if (!TextUtils.isEmpty(currentLocation)) {
            attributes.put(NodeKeys.LOCATION, currentLocation);
        }

        if (TextUtils.isEmpty(currentParentNo)) {
            parentNumber.setError("Cannot be empty");
            return;
        }

        if (!TextUtils.isDigitsOnly(currentParentNo)) {
            parentNumber.setError("Not a number");
            return;
        }

        ArrayList<Node> nodesToAdd = (ArrayList<Node>)getIntent().getSerializableExtra("treeNodes");
        if (nodesToAdd == null)
            nodesToAdd = FirebaseDecorator.pullNodesArray(mAuth, db);
//        ArrayList<Node> nodesToAdd = FirebaseDecorator.pullNodesArray(mAuth, db);
        Tree treeToAdd = Tree.treeFromNodesArray(nodesToAdd);

        if (TextUtils.isEmpty(currentParentNo)) {
            parentNumber.setError("Field is empty");
            return;
        }

        if (!TextUtils.isDigitsOnly(currentParentNo)) {
            parentNumber.setError("Not a number");
            return;
        }

        int no1 = Integer.parseInt(currentParentNo);
        if (no1 < 0 || no1 > treeToAdd.getMaxNodeNumber()) {
            parentNumber.setError("Id is from 0 to " + treeToAdd.getMaxNodeNumber());
            return;
        }

        /*if (!TextUtils.isEmpty(currentEducation)) {
            attributes.put(NodeKeys.DATE_OF_DEATH, currentEducation);
        }

        if (!TextUtils.isEmpty(currentWork)) {
            attributes.put(NodeKeys.DATE_OF_DEATH, currentWork);
        }



        if (!TextUtils.isEmpty(currentDescription)) {
            attributes.put(NodeKeys.DATE_OF_DEATH, currentDescription);
        }*/




        Node child = Tree.findNodeByNumber(nodesToAdd, no1);
        ArrayList<Node> nodesPassedFurther = null;
        if (child != null) {
            if (treeToAdd.getNodeParentsAmount(child) < 2) {
                int no2 = treeToAdd.getMaxNodeNumber() + 1;
                Node newNode = new Node(attributes, no2);
                newNode.setNumberofParent(no1);
                treeToAdd.AddPatron(child, newNode);
                nodesPassedFurther = treeToAdd.toNodeArrayList();
                FirebaseDecorator.pushNode(mAuth, db, newNode);

            }
        }
        else if (no1 == -1) {
            int no2 = treeToAdd.getMaxNodeNumber() + 1;
            Node newNode = new Node(attributes, no2);
            newNode.setNumberofParent(no1);
            treeToAdd.AddPatron(null, newNode);
            nodesPassedFurther = treeToAdd.toNodeArrayList();
            FirebaseDecorator.pushNode(mAuth, db, newNode);
        }


        Toast.makeText(getApplicationContext(), "Created new node", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        if (nodesPassedFurther != null)
            i.putExtra("treeNodes", nodesPassedFurther);
        else i.putExtra("treeNodes", nodesToAdd);
        startActivity(i);
        finish();
    }

    public void goToMenu(View view) {
        ArrayList<Node> nodesToAdd = (ArrayList<Node>)getIntent().getSerializableExtra("treeNodes");
        if (nodesToAdd == null)
            nodesToAdd = FirebaseDecorator.pullNodesArray(mAuth, db);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("treeNodes", nodesToAdd);
        startActivity(i);
        finish();
    }


}
