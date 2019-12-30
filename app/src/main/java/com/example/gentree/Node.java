package com.example.gentree;

import java.util.ArrayList;
import java.util.Map;

public class Node {
    //private PermittedUsersTable permittedUsersTable;
    private ArrayList<String> userIDs; // table of users
    private Map<String, String> attributes;
    //private ArrayList<Node> parents;
    //private ArrayList<Node> children;
    //private ArrayList<Node>  currentGeneration;

    public Node(ArrayList<String> userIDs, Map<String, String> attributes) {
        this.userIDs = userIDs;
        this.attributes = attributes;
    }
}
