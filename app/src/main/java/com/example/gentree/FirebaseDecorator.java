package com.example.gentree;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class FirebaseDecorator {

    public static void pushNode(FirebaseAuth mAuth, FirebaseFirestore db, Node toSend) {
        final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Task<QuerySnapshot> taskGetUsers = db.collection("users")
                        .whereEqualTo("userID", userID)
                        .get();
                try {
                    Tasks.await(taskGetUsers);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (taskGetUsers.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(taskGetUsers.getResult())) {
                        System.out.println("dobieram sie do usera");
                        Task<Void> getTreeSnapTask = documentSnapshot.getReference()
                                .collection("treeSnap")
                                .document("" + toSend.getNumber())
                                .set(toSend);
                        /*try {
                            Tasks.await(getTreeSnapTask);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/

                    }
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void deleteNodes(FirebaseAuth mAuth, FirebaseFirestore db) {
        final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Task<QuerySnapshot> taskUser = db.collection("users")
                        .whereEqualTo("userID", userID)
                        .get();
                try {
                    Tasks.await(taskUser);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (taskUser.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(taskUser.getResult())) {
                        System.out.println("dobieram sie do usera");
                        Task<QuerySnapshot> getTreeSnapTask = documentSnapshot.getReference()
                                .collection("treeSnap")
                                .get();
                        try {
                            Tasks.await(getTreeSnapTask);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (getTreeSnapTask.isSuccessful()) {
                            for (QueryDocumentSnapshot nodeDoc : Objects.requireNonNull(getTreeSnapTask.getResult())) {
                                Task<Void> deleteNodeDocTask = nodeDoc.getReference()
                                        .delete();
                                /*try {
                                    Tasks.await(deleteNodeDocTask);
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }*/
                            }
                        }


                    }
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCertainNodes(FirebaseAuth mAuth, FirebaseFirestore db, ArrayList<Node> nodes) {
        final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Task<QuerySnapshot> taskUser = db.collection("users")
                        .whereEqualTo("userID", userID)
                        .get();
                try {
                    Tasks.await(taskUser);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (taskUser.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(taskUser.getResult())) {
                        for (Node node : nodes) {
                            Task<Void> sendNewNodeTask = documentSnapshot.getReference()
                                    .collection("treeSnap")
                                    .document("" + node.getNumber())
                                    .delete();
                            /*try {
                                Tasks.await(sendNewNodeTask);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/
                        }


                    }
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void editGivenNode(FirebaseAuth mAuth, FirebaseFirestore db, Node toEdit) {
        final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Task<QuerySnapshot> taskUser = db.collection("users")
                        .whereEqualTo("userID", userID)
                        .get();
                try {
                    Tasks.await(taskUser);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (taskUser.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(taskUser.getResult())) {
                        Task<Void> sendNewNodeTask = documentSnapshot.getReference()
                                .collection("treeSnap")
                                .document("" + toEdit.getNumber())
                                .update("attributes", toEdit.getAttributes());

                            /*try {
                                Tasks.await(sendNewNodeTask);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/
                    }


                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void pushNodes(FirebaseAuth mAuth, FirebaseFirestore db, ArrayList<Node> nodes) {
        final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Task<QuerySnapshot> taskUser = db.collection("users")
                        .whereEqualTo("userID", userID)
                        .get();
                try {
                    Tasks.await(taskUser);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (taskUser.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(taskUser.getResult())) {
                        for (Node node : nodes) {
                            Task<Void> sendNewNodeTask = documentSnapshot.getReference()
                                    .collection("treeSnap")
                                    .document("" + node.getNumber())
                                    .set(node);
                            /*try {
                                Tasks.await(sendNewNodeTask);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/
                        }


                    }
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*db.collection("users")
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
        });*/
    }

    public static ArrayList<Node> pullNodesArray(FirebaseAuth mAuth, FirebaseFirestore db) {
        Tree newTree = new Tree();
        ArrayList<Node> nodes = new ArrayList<>();

        final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Task<QuerySnapshot> taskUser = db.collection("users")
                        .whereEqualTo("userID", userID)
                        .get();
                try {
                    Tasks.await(taskUser);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (taskUser.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(taskUser.getResult())) {
                        Task<QuerySnapshot> docsTreeSnapTask = documentSnapshot.getReference()
                                .collection("treeSnap")
                                .get();
                        try {
                            QuerySnapshot treeSnaps = Tasks.await(docsTreeSnapTask);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (docsTreeSnapTask.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshots : Objects.requireNonNull(docsTreeSnapTask.getResult())) {
                                nodes.add(queryDocumentSnapshots.toObject(Node.class));
                            }
                        }
                    }
                }
                /*nodes.sort(new Comparator<Node>() {
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
                for (Node node : nodes)
                    System.out.println(node.toJson(node.getNumberofParent()));
                System.out.println("w watku");*/
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return nodes;
    }

    static ArrayList<ArrayList<Node>> getArrayListsFromAllTrees(FirebaseAuth mAuth, FirebaseFirestore db) {
        ArrayList<ArrayList<Node>> result = new ArrayList<>();

        final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Task<QuerySnapshot> taskUser = db.collection("users")
                        .get();
                try {
                    Tasks.await(taskUser);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (taskUser.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(taskUser.getResult())) {
//                        System.out.println("kolejny user");
                        if (!documentSnapshot.get("userID").equals(userID)) {

                            ArrayList<Node> nodesFromUser = new ArrayList<>();
                            Task<QuerySnapshot> docsTreeSnapTask = documentSnapshot.getReference()
                                    .collection("treeSnap")
                                    .get();
                            try {
                                QuerySnapshot treeSnaps = Tasks.await(docsTreeSnapTask);
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            if (docsTreeSnapTask.isSuccessful()) {
                                for (QueryDocumentSnapshot queryDocumentSnapshots : Objects.requireNonNull(docsTreeSnapTask.getResult())) {
//                                    System.out.println("node: " + queryDocumentSnapshots.toObject(Node.class));
                                    nodesFromUser.add(queryDocumentSnapshots.toObject(Node.class));
                                }
                                result.add(nodesFromUser);
                            }
                        }

                    }
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}
