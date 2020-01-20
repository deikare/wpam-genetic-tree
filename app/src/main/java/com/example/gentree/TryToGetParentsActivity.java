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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

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

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentStringNo = number.getText().toString();
                System.out.println("wchodze");
                int no = Integer.parseInt(currentStringNo);

                final String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                *//*db.collection("users")
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
                                                                Task<QuerySnapshot> taskGetUsers = db.collection("users")
                                                                        .get();
                                                                try {
                                                                    QuerySnapshot docs = Tasks.await(taskGetUsers);
                                                                } catch (ExecutionException e) {
                                                                    e.printStackTrace();
                                                                } catch (InterruptedException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                if(taskGetUsers.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(taskGetUsers.getResult())) {
                                                                        if (!Objects.requireNonNull(documentSnapshot.getString("userID")).equals(uid)) {
                                                                            Task<QuerySnapshot> taskGetTree =documentSnapshot.getReference()
                                                                                    .collection("treeSnap")
                                                                                    .get();
                                                                            try {
                                                                                QuerySnapshot docsTree = Tasks.await(taskGetTree);
                                                                            } catch (ExecutionException e) {
                                                                                e.printStackTrace();
                                                                            } catch (InterruptedException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            if (taskGetTree.isSuccessful()) {
                                                                                for (QueryDocumentSnapshot docs2 : Objects.requireNonNull(taskGetTree.getResult())) {
                                                                                    if (!Objects.requireNonNull(docs2.getString("userID")).equals(uid)) {
                                                                                        Task<QuerySnapshot>docs2.getReference()
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
                                                                                                                                                                        *//**//*.addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                            @Override
                                                                                                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                                                Toast.makeText(getApplicationContext(), "Node deleted", Toast.LENGTH_SHORT).show();
                                                                                                                                                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                                                                                                                                finish();
                                                                                                                                                                            }
                                                                                                                                                                        });*//**//*
                                                                                                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                            @Override
                                                                                                                                                                            public void onSuccess(Void aVoid) {
                                                                                                                                                                                if (task.isSuccessful()) {

                                                                                                                                                                                }

                                                                                                                                                                            }
                                                                                                                                                                        });
                                                                                                                                                            }

                                                                                                                                                                *//**//*db.collection("users")
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
                                                                                                                                                                        });*//**//*
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
                                                                            }
                                                                        }
                                                                    }
                                                                }

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
                                                                                                                                                                            *//**//*.addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                                                                @Override
                                                                                                                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                                                    Toast.makeText(getApplicationContext(), "Node deleted", Toast.LENGTH_SHORT).show();
                                                                                                                                                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                                                                                                                                                    finish();
                                                                                                                                                                                }
                                                                                                                                                                            });*//**//*
                                                                                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                                                                @Override
                                                                                                                                                                                public void onSuccess(Void aVoid) {
                                                                                                                                                                                    if (task.isSuccessful()) {

                                                                                                                                                                                    }

                                                                                                                                                                                }
                                                                                                                                                                            });
                                                                                                                                                                }

                                                                                                                                                                *//**//*db.collection("users")
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
                                                                                                                                                                        });*//**//*
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
                        });*//*
            }*/
//        });
    }

    public void searchAndUpdateParents(View view) {
        final String numberToUpdate = number.getText().toString();

        int no = Integer.parseInt(numberToUpdate);


        /*for (ArrayList<Node> nodes : arrayListOfNodeList) {
            System.out.println("newArray");
            for (Node node : nodes)
                System.out.println("\t" + node.toJson(node.getNumberofParent()));
        }*/
        ArrayList<Node> thisTreeNodes = FirebaseDecorator.pullNodesArray(mAuth, db);
        Node checkedNode = Tree.findNodeByNumber(thisTreeNodes, no);
        Tree thisTree = Tree.treeFromNodesArray(thisTreeNodes);
        if (checkedNode != null) {
            if (thisTree.getNodeParentsAmount(checkedNode) < 2) {
                boolean isTreeEdited = false;
                ArrayList<ArrayList<Node>> arrayListOfNodeList = FirebaseDecorator.getArrayListsFromAllTrees(mAuth, db);
                ArrayList<Node> nodesToSend = new ArrayList<>();
                for (ArrayList<Node> nodes : arrayListOfNodeList) {
                    if (thisTree.getNodeParentsAmount(checkedNode) < 2) {
                        Tree subjectedTree = Tree.treeFromNodesArray(nodes);
                        ArrayList<Node> parentList = Tree.searchForParents(checkedNode, subjectedTree.getGraph());
                        if (parentList != null) {
                            for (Node parentToInsert : parentList) {
                                parentToInsert.setNumber(thisTree.getMaxNodeNumber() + 1);
                                parentToInsert.setNumberofParent(checkedNode.getNumber());
                                thisTree.AddPatron(checkedNode, parentToInsert);
                                nodesToSend.add(parentToInsert);
                                isTreeEdited = true;
                            }
                        }
                    }
                    else break;
                }
                if (isTreeEdited) {
//                    FirebaseDecorator.deleteNodes(mAuth, db);
                    FirebaseDecorator.pushNodes(mAuth, db, nodesToSend);
                }
            }
        }

        /*Map<String, String> attributes = new HashMap<>();
        attributes.put(NodeKeys.NAME, "edited");
        attributes.put(NodeKeys.LAST_NAME, "");
        attributes.put(NodeKeys.DATE_OF_BIRTH, "");
        attributes.put(NodeKeys.DATE_OF_DEATH, "");
        attributes.put(NodeKeys.EDUCATION, "");
        attributes.put(NodeKeys.WORK, "");
        attributes.put(NodeKeys.LOCATION, "");
        attributes.put(NodeKeys.DESCRIPTION, "");

        ArrayList<Node> nodesToAdd = FirebaseDecorator.pullNodesArray(mAuth, db);
        Tree treeToAdd = Tree.treeFromNodesArray(nodesToAdd);

        int no = Integer.parseInt(numberToEdit);

        Node beforeNode = Tree.findNodeByNumber(nodesToAdd, no);
        Node newNode = new Node(attributes, beforeNode.getNumber());
        newNode.setNumberofParent(beforeNode.getNumberofParent());

        treeToAdd.EditPatron(beforeNode, newNode);
        FirebaseDecorator.deleteNodes(mAuth, db);
        ArrayList<Node> nodesToPush = treeToAdd.toNodeArrayList();
        FirebaseDecorator.pushNodes(mAuth, db, nodesToPush);

        *//*ArrayList<Node> nodesToSend = new ArrayList<>();
        nodesToSend.add(newNode);

        FirebaseDecorator.pushNodes(mAuth, db, nodesToSend);*/
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
