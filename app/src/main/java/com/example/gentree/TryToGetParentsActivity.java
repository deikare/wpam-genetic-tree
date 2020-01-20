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

public class TryToGetParentsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Button button;
    private EditText number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_to_get_parents);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        button = findViewById(R.id.new_parents_button);
        number = findViewById(R.id.new_parents_node_number);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentStringNo = number.getText().toString();
                System.out.println("wchodze");
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

                                                            Tree newTree = Tree.treeFromNodesArray(nodes);
                                                            Node testedNode = Tree.findNodeByNumber(nodes, no);
                                                            if (newTree.getNodeParentsAmount(testedNode) < 2) {
                                                                db.collection("users")
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                                                                        if (!Objects.requireNonNull(documentSnapshot.getString("userID")).equals(uid)) {
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
                                                                                                                Tree subjectedTree = Tree.treeFromNodesArray(nodes);
                                                                                                                ArrayList<Node> protoParents = Tree.searchForParents(testedNode, subjectedTree.getGraph());
                                                                                                                if (protoParents != null) {
                                                                                                                    if (newTree.getNodeParentsAmount(testedNode) < 2) {
                                                                                                                        for (Node parent : protoParents) {
                                                                                                                            if (newTree.getNodeParentsAmount(testedNode) < 2) {
                                                                                                                                parent.setNumber(newTree.getMaxNodeNumber() + 1);
                                                                                                                                parent.setNumberofParent(testedNode.getNumber());
                                                                                                                                newTree.AddPatron(testedNode, parent);
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }

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

                                                                                                                                                                /*db.collection("users")
                                                                                                                                                                        .whereEqualTo("userID", uid)
                                                                                                                                                                        .get()
                                                                                                                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                                                                                                            @Override
                                                                                                                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                                                                                                                if (task.isSuccessful()) {
                                                                                                                                                                                    for (QueryDocumentSnapshot queryDocumentSnapshot : Objects.requireNonNull(task.getResult())) {
                                                                                                                                                                                        Tree toSend = newTree.deleteDuplicats();
                                                                                                                                                                                        for (Node nodeToAdd : toSend.getGraph().vertexSet())
                                                                                                                                                                                            queryDocumentSnapshot.getReference()
                                                                                                                                                                                                    .collection("treeSnap")
                                                                                                                                                                                                    .add(nodeToAdd)
                                                                                                                                                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                                                                                                                        @Override
                                                                                                                                                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                                                                                                                                                            System.out.println("dodaje");
                                                                                                                                                                                                        }
                                                                                                                                                                                                    });
                                                                                                                                                                                        Toast.makeText(getApplicationContext(), "Parents added", Toast.LENGTH_SHORT).show();
                                                                                                                                                                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                                                                                                                                        finish();
                                                                                                                                                                                    }
                                                                                                                                                                                }
                                                                                                                                                                            }
                                                                                                                                                                        });*/
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    });
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            });
                                                                                                                }
                                                                                                            }
                                                                                                        }

                                                                                                    });
                                                                                        }
                                                                                    }
                                                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                                    finish();
                                                                                }

                                                                            }
                                                                        });
                                                            }
                                                            else {
                                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                finish();
                                                            }



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
