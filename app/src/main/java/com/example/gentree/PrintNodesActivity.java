package com.example.gentree;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;

import java.util.Iterator;

import de.blox.graphview.BaseGraphAdapter;
import de.blox.graphview.Graph;
import de.blox.graphview.GraphView;
import de.blox.graphview.ViewHolder;
import de.blox.graphview.tree.BuchheimWalkerAlgorithm;
import de.blox.graphview.tree.BuchheimWalkerConfiguration;

public class PrintNodesActivity extends AppCompatActivity {
    private int nodeCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_nodes);
        GraphView graphView = findViewById(R.id.graph);

        // example tree
        final Graph graph = new Graph();
        /*final de.blox.graphview.Node node1 = new de.blox.graphview.Node(getNodeText());
        final de.blox.graphview.Node node2 = new de.blox.graphview.Node(getNodeText());
        final de.blox.graphview.Node node3 = new de.blox.graphview.Node(getNodeText());*/

        String treeJSON = getIntent().getStringExtra("tree");
        Tree printTree = Tree.TreeFromJson(treeJSON);
        Node base = Tree.findNodeByNumber(printTree.toNodeArrayList(), 0);


        Iterator<Node> iterator = new DepthFirstIterator<>(printTree.getGraph(), base);

        while (iterator.hasNext()) {
            Node node = iterator.next();
            for (DefaultEdge edge : printTree.getGraph().outgoingEdgesOf(node)) {
                final de.blox.graphview.Node src = new de.blox.graphview.Node(getNodeText(printTree.getGraph().getEdgeSource(edge)));
                final de.blox.graphview.Node dst = new de.blox.graphview.Node(getNodeText(printTree.getGraph().getEdgeTarget(edge)));
                graph.addEdge(src, dst);
            }
        }


        // you can set the graph via the constructor or use the adapter.setGraph(Graph) method
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
    }

    private String getNodeText(Node node) {
        return "" + node.getNumber();
    }
}
