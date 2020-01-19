package com.example.gentree;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseDecorator {

    public static void pushNodes(FirebaseAuth mAuth, FirebaseFirestore db, Tree gentree) {
        final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db.collection("users")
                .whereEqualTo("userID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, String> treeSnap = new HashMap<>();
                            for (Node node : gentree.getGraph().vertexSet()) {
                                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                    documentSnapshot.getReference()
                                            .collection("treeSnap")
                                            .add(node)
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

                    }
        });
    }

    public static ArrayList<Node> pullNodesArray(FirebaseAuth mAuth, FirebaseFirestore db) {
        Tree newTree = new Tree();
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
                                                        System.out.println(nodes);
                                                        System.out.println("new");
                                                    }
                                                }
                                            }
                                        });

                            }


                        }

                    }
                });
        nodes.sort(new Comparator<Node>() {
            @Override
            public int compare(Node node, Node t1) {
                return node.getNumberofParent() - t1.getNumberofParent();
            }
        });
        for (Node node : nodes) {
            Node child = null;
            if (node.getNumber() != 0)
                child = Tree.findNodeByNumber(nodes, node.getNumberofParent());
            newTree.AddPatron(child, node);
        }
        return nodes;
    }
}
