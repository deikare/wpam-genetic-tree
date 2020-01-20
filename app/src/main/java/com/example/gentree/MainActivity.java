package com.example.gentree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();



    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void addNewRecord(View view) {
        startActivity(new Intent(getApplicationContext(), AddNewNodeActivity.class));
        finish();
    }

    public void readRecord(View view) {
        final TextView textView = findViewById(R.id.textView2);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        /*ArrayList<Node> nodeList = FirebaseDecorator.pullNodesArray(mAuth, db);
        System.out.println("po wyjsciu z funkcji");
        System.out.println(nodeList.size());
        System.out.println(nodeList.toString());*/
//        Tree newTree = new Tree();
        ArrayList<Node> nodes = new ArrayList<>();

        final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
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
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot queryDocumentSnapshots : Objects.requireNonNull(task.getResult())) {
//                                                        textView.setText(queryDocumentSnapshots.getData().toString());
//                                                        System.out.println(queryDocumentSnapshots.toObject(Object.class));
//                                                        System.out.println(queryDocumentSnapshots.getData().toString());
                                                        nodes.add(queryDocumentSnapshots.toObject(Node.class));
//                                                        System.out.println(nodes);
//                                                        System.out.println("new");
                                                    }
                                                    /*nodes.sort(new Comparator<Node>() {
                                                        @Override
                                                        public int compare(Node node, Node t1) {
                                                            int result = node.getNumberofParent() - t1.getNumberofParent();
                                                            return Integer.compare(result, 0);
                                                        }
                                                    });
//                                                    System.out.println(nodes);
                                                    for (Node node : nodes) {
                                                        System.out.println(node.toJson(node.getNumberofParent()));
                                                        System.out.println(node.getNumberofParent());
                                                    }

                                                    for (Node node : nodes) {
                                                        Node child = null;
                                                        if (node.getNumber() != 0)
                                                            child = Tree.findNodeByNumber(nodes, node.getNumberofParent());
                                                        newTree.AddPatron(child, node);

                                                    }*/
                                                    Tree newTree = Tree.treeFromNodesArray(nodes);
                                                    System.out.println(newTree.toJson());
                                                    /*System.out.println("dasz rade");
                                                    System.out.println("wierze w ciebie kotku <3");
                                                    System.out.println("o chuj o chuj o chuj!");*/
                                                }
                                            }
                                        });

                            }


                        }

                    }
                });

        /*String uID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db.collection("users")
                .whereEqualTo("userID", uID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                documentSnapshot.getReference()
                                        .collection("treeSnap")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot queryDocumentSnapshots : Objects.requireNonNull(task.getResult())) {
//                                                        textView.setText(queryDocumentSnapshots.getData().toString());
//                                                        System.out.println(queryDocumentSnapshots.toObject(Object.class));
//                                                        System.out.println(queryDocumentSnapshots.getData().toString());
                                                        Node newNode = queryDocumentSnapshots.toObject(Node.class);
                                                        System.out.println(newNode.toJson(newNode.getNumberofParent()));
                                                        System.out.println("chuj");
                                                    }
                                                }
                                            }
                                        });

                            }


                        else {
                            Toast.makeText(getApplicationContext(), "Nie udalo sie", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
    }

    public void deleteRecord(View view) {
        startActivity(new Intent(getApplicationContext(), DeleteRecordActivity.class));
        finish();
    }

    public void editRecord(View view) {
        startActivity(new Intent(getApplicationContext(), EditNodeActivity.class));
        finish();
    }
}
