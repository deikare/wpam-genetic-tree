package com.example.gentree;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import de.blox.graphview.BaseGraphAdapter;
import de.blox.graphview.Graph;
import de.blox.graphview.GraphView;
import de.blox.graphview.ViewHolder;
import de.blox.graphview.tree.BuchheimWalkerAlgorithm;
import de.blox.graphview.tree.BuchheimWalkerConfiguration;

public class PrintNodesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_nodes);
        GraphView graphView = findViewById(R.id.graph);

        final Graph graph = new Graph();


        ArrayList<Node> nodesArrayList = (ArrayList<Node>)getIntent().getSerializableExtra("treeNodes");
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
//        String result = node.getAttributes().get(NodeKeys.NAME);
//        System.out.println(result);
//        return "" + node.getAttributes().get(NodeKeys.NAME);
        int dob = Integer.parseInt(Objects.requireNonNull(node.getAttributes().get(NodeKeys.DATE_OF_BIRTH)));
        int dod;
        String result = "" + node.getAttributes().get(NodeKeys.NAME) + " " + node.getAttributes().get(NodeKeys.LAST_NAME) + "\n"
                + dob + " - ";
        if (node.getAttributes().containsKey(NodeKeys.DATE_OF_DEATH)) {
            dod = Integer.parseInt(Objects.requireNonNull(node.getAttributes().get(NodeKeys.DATE_OF_DEATH)));
            result += dod;
        }
        else result += "?";

        return result;
    }
}
