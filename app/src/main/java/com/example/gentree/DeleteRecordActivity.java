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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DeleteRecordActivity extends AppCompatActivity {

    Button button;
    EditText nameToDelete;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_record);

        nameToDelete = findViewById(R.id.name_to_delete);
        button = findViewById(R.id.delete_button);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String currentNameDelete = nameToDelete.getText().toString();
                String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                db.collection("users")
                        .whereEqualTo("userID", uid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
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
                                                            int no = Integer.parseInt(currentNameDelete);
                                                            Node toDelete = Tree.findNodeByNumber(nodes, no);
                                                            System.out.println("before:");
                                                            System.out.println(newTree.toJson());
                                                            newTree.RemovePatron(toDelete);
                                                            System.out.println("after:");
                                                            System.out.println(newTree.toJson());
                                                            db.collection("users")
                                                                    .whereEqualTo("userID", uid)
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                                                                    documentSnapshot.getReference()
                                                                                            .collection("treeSnap")
                                                                                            .get()
                                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                    if (task.isSuccessful()) {
                                                                                                        System.out.println("usuwam");
                                                                                                        for (QueryDocumentSnapshot queryDocumentSnapshot : Objects.requireNonNull(task.getResult())) {
                                                                                                            Node thisNode = queryDocumentSnapshot.toObject(Node.class);
                                                                                                            System.out.println(thisNode.toJson(thisNode.getNumberofParent()));
                                                                                                            queryDocumentSnapshot.getReference()
                                                                                                                    .delete()
                                                                                                                    *//*.addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                            Toast.makeText(getApplicationContext(), "Node deleted", Toast.LENGTH_SHORT).show();
                                                                                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                                                                            finish();
                                                                                                                        }
                                                                                                                    });*//*
                                                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(Void aVoid) {
                                                                                                                            if (task.isSuccessful()) {

                                                                                                                            }

                                                                                                                        }
                                                                                                                    });
                                                                                                        }

                                                                                                        db.collection("users")
                                                                                                                .whereEqualTo("userID", uid)
                                                                                                                .get()
                                                                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                    @Override
                                                                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                        if (task.isSuccessful()) {
                                                                                                                            for (QueryDocumentSnapshot queryDocumentSnapshot : Objects.requireNonNull(task.getResult())) {
                                                                                                                                for (Node nodeToAdd : newTree.getGraph().vertexSet())
                                                                                                                                    queryDocumentSnapshot.getReference()
                                                                                                                                            .collection("treeSnap")
                                                                                                                                            .add(nodeToAdd)
                                                                                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                                                                @Override
                                                                                                                                                public void onSuccess(DocumentReference documentReference) {
                                                                                                                                                    System.out.println("dodaje");
                                                                                                                                                }
                                                                                                                                            });
                                                                                                                                Toast.makeText(getApplicationContext(), "Node deleted", Toast.LENGTH_SHORT).show();
                                                                                                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                                                                                finish();
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

                                                    }
                                                })
                                                *//*.addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                })*//*;
                                    }
                                }
                            }
                        });

            }
        });*/

    }

    public void deleteNode(View view) {
        final String numberToDelete = nameToDelete.getText().toString();

        ArrayList<Node> nodesToAdd = (ArrayList<Node>)getIntent().getSerializableExtra("treeNodes");
        if (nodesToAdd == null)
            nodesToAdd = FirebaseDecorator.pullNodesArray(mAuth, db);
        Tree treeToAdd = Tree.treeFromNodesArray(nodesToAdd);

        if (!TextUtils.isDigitsOnly(numberToDelete)) {
            nameToDelete.setError("Not a number");
            return;
        }

        int no = Integer.parseInt(numberToDelete);
        if (no < 1 || no > treeToAdd.getMaxNodeNumber()) {
            nameToDelete.setError("Id is from 1 to " + treeToAdd.getMaxNodeNumber());
            return;
        }


//        ArrayList<Node> nodesToAdd = FirebaseDecorator.pullNodesArray(mAuth, db);

        Node nodeToDelete = Tree.findNodeByNumber(nodesToAdd, no);

        ArrayList<Node> nodesToDelete = treeToAdd.getDeletedNodes(nodeToDelete);
        FirebaseDecorator.deleteCertainNodes(mAuth, db, nodesToDelete);
        treeToAdd.RemovePatron(nodeToDelete);
//        FirebaseDecorator.deleteNodes(mAuth, db);
        ArrayList<Node> nodesToPush = treeToAdd.toNodeArrayList();
//        FirebaseDecorator.pushNodes(mAuth, db, nodesToPush);

        Toast.makeText(getApplicationContext(), "Node deleted", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("treeNodes", nodesToPush);
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

