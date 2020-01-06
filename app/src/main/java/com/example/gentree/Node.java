package com.example.gentree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Node<T> {
//    //private PermittedUsersTable permittedUsersTable;
//    private ArrayList<String> userIDs; // table of users
//    private Map<String, String> attributes;
//    //private ArrayList<Node> parents;
//    //private ArrayList<Node> children;
//    //private ArrayList<Node>  currentGeneration;
//
//    public Node(ArrayList<String> userIDs, Map<String, String> attributes) {
//        this.userIDs = userIDs;
//        this.attributes = attributes;
//    }
//}

    public interface NodeStatus {
        String USER = "user_node";
        String NOT_USER = "not_user_node";
    }
    private T data;

    private List<Node<T>> children;
    private List<Node<T>> parents;
    private Node<T> partner = null;
    private String status;


    public Node(T data, String status) {
        this.data = data;
        this.status = status;
        children = new ArrayList<>();
        parents = new ArrayList<>();
    }

    public boolean addChild(Node<T> Child) {
        if (partner != null) {
            partner.addChild(Child);
            Child.addParent(partner);
        }
        Child.addParent(this);
        return this.children.add(Child);
    }

    public boolean removeChild(Node<T> Child) {
        return this.children.remove(Child);
    }

    public boolean addParent(Node<T> Parent) {
        return this.parents.add(Parent);
    }

    public boolean removeParent(Node<T> Parent) {
        return this.parents.remove(Parent);
    }

    public boolean removePartner() {
        if (this.partner != null) {
            this.partner = null;
            return true;
        }
        return false;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public void setChildren(List<Node<T>> children) {
        this.children = children;
    }

    public List<Node<T>> getParents() {
        return parents;
    }

    public void setParents(List<Node<T>> parents) {
        this.parents = parents;
    }

    public Node<T> getPartner() {
        return partner;
    }

    public void setPartner(Node<T> partner) {
        this.partner = partner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
