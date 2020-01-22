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

import java.lang.reflect.Array;
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
            ArrayList<Node> actualChildren = new ArrayList<>();

            for (DefaultEdge edge : thisTree.getGraph().outgoingEdgesOf(checkedNode)) {
                Node child = thisTree.getGraph().getEdgeTarget(edge);
                actualChildren.add(child);
            }

            System.out.println(checkedNode.toJson(checkedNode.getNumberofParent()));
            if (thisTree.getNodeParentsAmount(checkedNode) < 2) {
                boolean isTreeEdited = false;
                ArrayList<ArrayList<Node>> arrayListOfNodeList = FirebaseDecorator.getArrayListsFromAllTrees(mAuth, db);

                ArrayList<Node> nodesToSend = new ArrayList<>();

                for (ArrayList<Node> nodes : arrayListOfNodeList) {
                    ArrayList<Node> parentsOfCheckedNode = new ArrayList<>();
                    for (DefaultEdge edge : thisTree.getGraph().outgoingEdgesOf(checkedNode)) {
                        Node parent = thisTree.getGraph().getEdgeTarget(edge);
                        parentsOfCheckedNode.add(parent);
                    }

                    if (thisTree.getNodeParentsAmount(checkedNode) < 2) {
                        Tree subjectedTree = Tree.treeFromNodesArray(nodes);
                        ArrayList<Node> parentList = Tree.searchForParents(checkedNode, subjectedTree.getGraph());
                        if (parentList != null) {
                            for (Node parentToInsert : parentList) {
                                boolean wouldBePair = false;

                                for (Node actualChild : actualChildren) {
                                    if (actualChild.getAttributes().equals(parentToInsert.getAttributes())) {
                                        wouldBePair = true;
                                        break;
                                    }
                                }

                                if(!wouldBePair) {
                                    parentToInsert.setNumber(thisTree.getMaxNodeNumber() + 1);
                                    parentToInsert.setNumberofParent(checkedNode.getNumber());
                                    thisTree.AddPatron(checkedNode, parentToInsert);
                                    nodesToSend.add(parentToInsert);
                                    isTreeEdited = true;
                                }
                            }
                        }
                    }
                    else break;
                }
                if (isTreeEdited) {
                    FirebaseDecorator.pushNodes(mAuth, db, nodesToSend);
                    Toast.makeText(getApplicationContext(), "Node(s) added", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "No new nodes", Toast.LENGTH_SHORT).show();
                }
            }
        }


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
