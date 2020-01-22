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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jgrapht.graph.DefaultEdge;

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
    }

    public void searchAndUpdateParents(View view) {
        final String numberToUpdate = number.getText().toString();

        ArrayList<Node> thisTreeNodes = (ArrayList<Node>)getIntent().getSerializableExtra("treeNodes");
        if (thisTreeNodes == null)
            thisTreeNodes = FirebaseDecorator.pullNodesArray(mAuth, db);
        Tree thisTree = Tree.treeFromNodesArray(thisTreeNodes);

        if (!TextUtils.isDigitsOnly(numberToUpdate)) {
            number.setError("Not a number");
            return;
        }
        int no = Integer.parseInt(numberToUpdate);
        if(no < 0 || no > thisTree.getMaxNodeNumber()) {
            number.setError("Id is from 0 to " + thisTree.getMaxNodeNumber());
            return;
        }

        Node checkedNode = Tree.findNodeByNumber(thisTreeNodes, no);

        if (checkedNode != null) {
            System.out.println(checkedNode.toJson(checkedNode.getNumberofParent()));
            if (thisTree.getNodeParentsAmount(checkedNode) < 2) {
                boolean isTreeEdited = false;
                ArrayList<ArrayList<Node>> arrayListOfNodeList = FirebaseDecorator.getArrayListsFromAllTrees(mAuth, db);

                ArrayList<Node> nodesToSend = new ArrayList<>();

                ArrayList<Node> parentsOfCheckedNode = new ArrayList<>();
                for (DefaultEdge edge : thisTree.getGraph().outgoingEdgesOf(checkedNode)) {
                    Node parent = thisTree.getGraph().getEdgeTarget(edge);
                    parentsOfCheckedNode.add(parent);
                }

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
        ArrayList<Node> nodesToPassFurther = thisTree.toNodeArrayList();
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
