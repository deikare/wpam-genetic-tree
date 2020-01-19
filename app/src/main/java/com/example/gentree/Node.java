package com.example.gentree;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class Node {
    private Map<String, String> attributes; // mapa przechowujaca atrybuty usera
    private int number;
    private int numberofParent;

    public Node() {
    }

    public Node(int number) {
        this.number = number;
        attributes = new HashMap<>();
    }

    public Node(Map<String, String> attributes, int level) {
        this.attributes = attributes;
        this.number = level;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Node ) && (toString().equals((obj.toString())));
    }

    @Override
    public String toString() {
        return "Node{" +
                "attributes=" + attributes +
                ", number=" + number +
                '}';
    }

    public int getNumberofParent() {
        return numberofParent;
    }

    public void setNumberofParent(int numberofParent) {
        this.numberofParent = numberofParent;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String toJson(int parentNumber) {
        String result = "\"" + this.getNumber() + "\": { ";
        Set<Map.Entry<String, String>> attributeSet = attributes.entrySet();
        if (!attributeSet.isEmpty()) {
            for (Map.Entry<String, String> attribute : attributeSet) {
                result += "\"" + attribute.getKey() + "\": \"" + attribute.getValue() + "\", ";
            }
            if (this.getNumber() != 0)
                result += "\"parent\": " + parentNumber;
            else
                result = result.substring(0, result.length() - 2);
        }
        result += " }";
        return result;
    }

    public static Node nodeFromJSON(String nodeJSON) {
        String[] strings = nodeJSON.split("\\r?[{}]");
        for (String string : strings) {
            string = string.trim();
        }
        strings[0] = strings[0].replace("\"", "").replace(":", "").trim();
        int number = Integer.parseInt(strings[0]);
        int parentNumber = -1;
        String[] paramstrings = strings[1].split("\\r?,");
        Map<String, String> attributes = new HashMap<>();
        for (String paramstring : paramstrings) {
            String[] keyOrVal = paramstring.split("\\r?:");
            if (keyOrVal[0].contains("parent")) {
                parentNumber = Integer.parseInt(keyOrVal[1].trim());
            }
            else {
                for (String vars : keyOrVal)
                    vars = vars.trim();
                attributes.put(keyOrVal[0], keyOrVal[1]);
            }
        }

        Node newNode = new Node(attributes, number);
        newNode.setNumberofParent(parentNumber);
        return newNode;
    }

    public Node containsNode(Graph<Node, DefaultEdge> graphToCompare) {
        Set<Node> nodeSet = graphToCompare.vertexSet();
        for (Node tempNode : nodeSet) {
            if (tempNode.getAttributes().equals(this.getAttributes()))
                return tempNode;
        }
        return null;
    }
}
