package com.example.gentree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_node);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        button = findViewById(R.id.edit_node_button);
        number = findViewById(R.id.edit_node_number);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentStringNo = number.getText().toString();
                int no = Integer.parseInt(currentStringNo);

                final String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                db.collection("users")
                        .whereEqualTo("userID", uid)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : Objects.requireNonNull(task.getResult())) {
                                        queryDocumentSnapshot
                                                .getReference()
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

                                                            Map<String, String> attributes = new HashMap<>();
                                                            attributes.put(NodeKeys.NAME, "zmienione");
                                                            attributes.put(NodeKeys.LAST_NAME, "");
                                                            attributes.put(NodeKeys.DATE_OF_BIRTH, "");
                                                            attributes.put(NodeKeys.DATE_OF_DEATH, "");
                                                            attributes.put(NodeKeys.EDUCATION, "");
                                                            attributes.put(NodeKeys.WORK, "");
                                                            attributes.put(NodeKeys.LOCATION, "");
                                                            attributes.put(NodeKeys.DESCRIPTION, "");

                                                            Tree newTree = Tree.treeFromNodesArray(nodes);
                                                            Node beforeNode = Tree.findNodeByNumber(nodes, no);
                                                            Node newNode = new Node(attributes, beforeNode.getNumber());
                                                            newNode.setNumberofParent(beforeNode.getNumberofParent());

                                                            System.out.println("before:");
                                                            System.out.println(newTree.toJson());
                                                            newTree.EditPatron(beforeNode, newNode);
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
                                                                                                                    /*.addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                        @Override
                                                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                                                            Toast.makeText(getApplicationContext(), "Node deleted", Toast.LENGTH_SHORT).show();
                                                                                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                                                                            finish();
                                                                                                                        }
                                                                                                                    });*/
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
                                                                                                                                Toast.makeText(getApplicationContext(), "Node modified", Toast.LENGTH_SHORT).show();
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
                                                });
                                    }
                                }
                            }
                        });
            }
        });
    }
}
