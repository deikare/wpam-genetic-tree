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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditNodeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button button;
    private EditText number;
    private EditText name, lastName, dateOfBirth, dateOfDeath, location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_node);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.edit_node_name);
        lastName = findViewById(R.id.edit_node_lname);
        dateOfBirth = findViewById(R.id.edit_node_dob);
        dateOfDeath = findViewById(R.id.edit_node_dod);
        location = findViewById(R.id.edit_node_location);

        button = findViewById(R.id.edit_node_button);
        number = findViewById(R.id.edit_node_number);
    }

    public void editNode(View view) {
        final String numberToEdit = number.getText().toString();
        final String currentName = name.getText().toString();
        final String currentLastName = lastName.getText().toString();
        final String dob = dateOfBirth.getText().toString();
        final String dod = dateOfDeath.getText().toString();

        final String currentLocation = location.getText().toString();


        Map<String, String> attributes = new HashMap<>();

        ArrayList<Node> nodesToAdd = (ArrayList<Node>)getIntent().getSerializableExtra("treeNodes");
        if (nodesToAdd == null)
            nodesToAdd = FirebaseDecorator.pullNodesArray(mAuth, db);
//        ArrayList<Node> nodesToAdd = FirebaseDecorator.pullNodesArray(mAuth, db);
        Tree treeToAdd = Tree.treeFromNodesArray(nodesToAdd);

        if (TextUtils.isEmpty(numberToEdit)) {
            number.setError("Cannot be empty");
            return;
        }

        if (!TextUtils.isDigitsOnly(numberToEdit)) {
            number.setError("Not a number");
            return;
        }

        int no1 = Integer.parseInt(numberToEdit);
        if (no1 < 0 || no1 > treeToAdd.getMaxNodeNumber()) {
            number.setError("Id is from 0 to " + treeToAdd.getMaxNodeNumber());
            return;
        }

        Node beforeNode = Tree.findNodeByNumber(nodesToAdd, no1);


        boolean isChanged = false;


        if (beforeNode != null) {

            if (beforeNode.getAttributes().containsKey(NodeKeys.DATE_OF_DEATH)) {
                if (TextUtils.isEmpty(dod))
                    attributes.put(NodeKeys.DATE_OF_DEATH, beforeNode.getAttributes().get(NodeKeys.DATE_OF_DEATH));
            }
            else if (!TextUtils.isEmpty(dod)) {
                if (!TextUtils.isDigitsOnly(dod)) {
                    dateOfDeath.setError("Bad format of field");
                    return;
                }
                else {
                    attributes.put(NodeKeys.DATE_OF_DEATH, dod);
                    isChanged = true;
                }
            }


            if (beforeNode.getAttributes().containsKey(NodeKeys.LOCATION)) {
                if (TextUtils.isEmpty(currentLocation))
                    attributes.put(NodeKeys.LOCATION, beforeNode.getAttributes().get(NodeKeys.LOCATION));
            }
            else if (!TextUtils.isEmpty(currentLocation)) {
                isChanged = true;
                attributes.put(NodeKeys.LOCATION, currentLocation);
            }


            if (TextUtils.isEmpty(currentName))
                attributes.put(NodeKeys.NAME, beforeNode.getAttributes().get(NodeKeys.NAME));
            else {
                attributes.put(NodeKeys.NAME, currentName);
                isChanged = true;
            }


            if (TextUtils.isEmpty(currentLastName))
                attributes.put(NodeKeys.LAST_NAME, beforeNode.getAttributes().get(NodeKeys.LAST_NAME));
            else {
                isChanged = true;
                attributes.put(NodeKeys.LAST_NAME, currentLastName);
            }

            if (TextUtils.isEmpty(dob))
                attributes.put(NodeKeys.DATE_OF_BIRTH, beforeNode.getAttributes().get(NodeKeys.DATE_OF_BIRTH));
            else {
                if (!TextUtils.isDigitsOnly(dob)) {
                    dateOfDeath.setError("Bad format of field");
                    return;
                }
                else {
                    attributes.put(NodeKeys.DATE_OF_BIRTH, dob);
                    isChanged = true;
                }
            }



            Node newNode = new Node(attributes, beforeNode.getNumber());
            newNode.setNumberofParent(beforeNode.getNumberofParent());
            treeToAdd.EditPatron(beforeNode, newNode);

            FirebaseDecorator.editGivenNode(mAuth, db, newNode);
            if (isChanged)
                Toast.makeText(getApplicationContext(), "Node edited", Toast.LENGTH_SHORT).show();
        }

        ArrayList<Node> nodesToPassFurther;
        if (isChanged) {
            nodesToPassFurther = treeToAdd.toNodeArrayList();
        }
        else {
            nodesToPassFurther = (ArrayList<Node>)getIntent().getSerializableExtra("treeNodes");
            if (nodesToPassFurther == null)
                nodesToPassFurther = FirebaseDecorator.pullNodesArray(mAuth, db);
        }

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("treeNodes", nodesToPassFurther);
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
