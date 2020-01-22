package com.example.gentree;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import de.blox.graphview.BaseGraphAdapter;
import de.blox.graphview.Graph;
import de.blox.graphview.GraphView;
import de.blox.graphview.ViewHolder;
import de.blox.graphview.tree.BuchheimWalkerAlgorithm;
import de.blox.graphview.tree.BuchheimWalkerConfiguration;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ArrayList<Node> nodesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        GraphView graphView = findViewById(R.id.graph);

        final Graph graph = new Graph();


        //////////////TU WAZNE - linijka ponizej dziala - przekazywanie danych albo z firebase'a, albo z poprzednich View!///////////////////
        nodesArrayList = (ArrayList<Node>)getIntent().getSerializableExtra("treeNodes");
        if (nodesArrayList == null)
            nodesArrayList = FirebaseDecorator.pullNodesArray(mAuth, db);
        Tree printTree = Tree.treeFromNodesArray(Objects.requireNonNull(nodesArrayList));
        for (Node node : printTree.toNodeArrayList())
            System.out.println(node.toJson(node.getNumberofParent()));

        Node base = Tree.findNodeByNumber(printTree.toNodeArrayList(), 0);


        Iterator<Node> iterator = new DepthFirstIterator<>(printTree.getGraph(), base);

        if (printTree.getGraph().vertexSet().size() == 1) {
            final de.blox.graphview.Node src = new de.blox.graphview.Node(getNodeText(Objects.requireNonNull(base)));
            graph.addNode(src);
        }
        else {
            while (iterator.hasNext()) {
                Node node = iterator.next();

                for (DefaultEdge edge : printTree.getGraph().outgoingEdgesOf(node)) {
                    Node modelSrc = printTree.getGraph().getEdgeSource(edge);

                    Node modelDst = printTree.getGraph().getEdgeTarget(edge);

                    final de.blox.graphview.Node src = new de.blox.graphview.Node(getNodeText(modelSrc));
                    final de.blox.graphview.Node dst = new de.blox.graphview.Node(getNodeText(modelDst));

                    graph.addEdge(src, dst);
                }
            }
        }

        final BaseGraphAdapter<ViewHolder> adapter = new BaseGraphAdapter<ViewHolder>(graph) {

            @Nullable
            @Override
            public CharSequence[] getAutofillOptions() {
                return new CharSequence[0];
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.node, parent, false);
                return new SimpleViewHolder(view);
            }



            @Override
            public void onBindViewHolder(ViewHolder viewHolder, Object data, int position) {
                ((SimpleViewHolder)viewHolder).textView.setText(data.toString());
            }
        };
        graphView.setAdapter(adapter);

        // set the algorithm here
        final BuchheimWalkerConfiguration configuration = new BuchheimWalkerConfiguration.Builder()
                .setSiblingSeparation(100)
                .setLevelSeparation(300)
                .setSubtreeSeparation(300)
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_BOTTOM_TOP)
                .build();
        adapter.setAlgorithm(new BuchheimWalkerAlgorithm(configuration));
        System.out.println(printTree.toJson());


    }

    private String getNodeText(Node node) {
        int dob = Integer.parseInt(Objects.requireNonNull(node.getAttributes().get(NodeKeys.DATE_OF_BIRTH)));
        int dod;
        String result = "" + "id: " + node.getNumber() + "\n"
                + node.getAttributes().get(NodeKeys.NAME) + " " + node.getAttributes().get(NodeKeys.LAST_NAME) + "\n"
                + dob + " - ";
        if (node.getAttributes().containsKey(NodeKeys.DATE_OF_DEATH)) {
            dod = Integer.parseInt(Objects.requireNonNull(node.getAttributes().get(NodeKeys.DATE_OF_DEATH)));
            result += dod;
        }
        else result += "?";

        if (node.getAttributes().containsKey(NodeKeys.LOCATION))
            result += "\n"
                    + node.getAttributes().get(NodeKeys.LOCATION);

        return result;
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getApplicationContext(), "Logout succesful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void addNewRecord(View view) {
        startActivity(new Intent(getApplicationContext(), AddNewNodeActivity.class));
        finish();
    }

    public void goToOptions(View view) {
        Intent i = new Intent(getApplicationContext(), OptionsActivity.class);
        i.putExtra("treeNodes", nodesArrayList);
        startActivity(i);
        finish();
    }
}


