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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseDecorator {

    public static void pushTree(FirebaseAuth mAuth, FirebaseFirestore db, Tree gentree) {
        final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db.collection("users")
                .whereEqualTo("userID", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, String> treeSnap = new HashMap<>();
                            for (Node node : gentree.getGraph().vertexSet())
                                treeSnap.put("node" + node.getNumber(), node.toJson(node.getNumberofParent()));
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                documentSnapshot.getReference()
                                        .collection("treeSnap")
                                        .add(treeSnap)
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

    public Tree pullTree(FirebaseAuth mAuth, FirebaseFirestore db) {
        Tree result = new Tree();
    }
}
