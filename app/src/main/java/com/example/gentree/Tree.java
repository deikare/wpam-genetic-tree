package com.example.gentree;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Tree {
    private Graph<Node, DefaultEdge> graph;

    public Tree() {
        graph = new DefaultDirectedGraph<>(DefaultEdge.class);
    }

    public Tree(Graph<Node, DefaultEdge> graph) {
        this.graph = graph;
    }

    public void AddPatron(Node child, Node patron) {
        if (graph.vertexSet().isEmpty()) {
            this.graph.addVertex(patron);
        }
        else {
            this.graph.addVertex(patron);
            this.graph.addEdge(child, patron);
        }
    }

    public Graph<Node, DefaultEdge> getGraph() {
        return graph;
    }

    public void setGraph(Graph<Node, DefaultEdge> graph) {
        this.graph = graph;
    }

    public void EditPatron(Node beforeNode, Node afterNode) {
//        beforeNode.setAttributes(afterNode.getAttributes());
        graph.addVertex(afterNode);
        for (DefaultEdge edge : graph.incomingEdgesOf(beforeNode))
            graph.addEdge(graph.getEdgeSource(edge), afterNode);
        for (DefaultEdge edge : graph.outgoingEdgesOf(beforeNode))
            graph.addEdge(afterNode, graph.getEdgeTarget(edge));
        graph.removeVertex(beforeNode);
    }

    public void RemovePatron(Node toRemove) {
        Iterator<Node> iterator = new DepthFirstIterator<>(graph, toRemove);
        /*DefaultEdge begEdge = null;
        for (var edge : graph.incomingEdgesOf(toRemove)) {
            begEdge = edge;
        }
        Node beforeNode = graph.getEdgeSource(begEdge);*/
        while (iterator.hasNext()) {
            graph.removeVertex(iterator.next());
        }

    }

    public String toJson() {
        String result = "{\n";
        result += "\t\"Smiths\": {\n";
        for (Node vertex: graph.vertexSet()) {
            int parentNumber = 0;
            if (vertex.getNumber() != 0) {
                for (DefaultEdge edge : graph.incomingEdgesOf(vertex))
                    parentNumber = graph.getEdgeSource(edge).getNumber();
            }
            String vertexString = vertex.toJson(parentNumber);
            /*if (vertex.getNumber() != 0) {
                vertexString = vertexString.substring(0, vertexString.length() - 2);
                vertexString += ", \"parent\": ";
                for (var edge : graph.incomingEdgesOf(vertex)) { //to sie wykona tylko raz, bo mam pewnosc, ze jest tylko jeden edge wchodzacy
                    vertexString += graph.getEdgeSource(edge).getNumber() + " ";
                }
                vertexString += "}";
            }*/
            result += "\t\t" + vertexString;
            result += ",\n";
            //tu dodac parent: int
        }
        result = result.substring(0, result.length() - 2) + "\n";

        result += "\t}\n}";
        return result;

    }

    public static ArrayList<Node> searchForParents(Node lookFor, Graph<Node, DefaultEdge> graph) {
        Node fromGraph = lookFor.containsNode(graph);
        if (fromGraph != null) {
            Set<DefaultEdge> edges = graph.outgoingEdgesOf(fromGraph);
            ArrayList<Node> nodes = new ArrayList<>(edges.size());
            for (DefaultEdge edge : edges) {
                nodes.add(graph.getEdgeTarget(edge));
            }
            return nodes;
        }
        return null;
    }

    public static Tree TreeFromJson(String json) {
        Tree newTree = new Tree();
        String []nodesFromJSON = json.split("\\r?\\n");
        ArrayList<Node> nodeList = new ArrayList<>();
        for (int i = 2; i < nodesFromJSON.length - 2; ++i) {
            nodesFromJSON[i] = nodesFromJSON[i].trim();
            nodesFromJSON[i] = nodesFromJSON[i].replace("},", "}");
            nodeList.add(Node.nodeFromJSON(nodesFromJSON[i]));
        }
        nodeList.sort(new Comparator<Node>() {
            @Override
            public int compare(Node node, Node t1) {
                return node.getNumberofParent() - t1.getNumberofParent();
            }
        });
        for (Node node : nodeList) {
            Node child = null;
            if (node.getNumber() != 0)
                child = findNodeByNumber(nodeList, node.getNumberofParent());
            newTree.AddPatron(child, node);
        }
        return newTree;
    }

    public static Node findNodeByNumber(ArrayList<Node> nodes, int number) {
        for (Node node : nodes)
            if (number == node.getNumber())
                return node;
        return null;
    }

}
