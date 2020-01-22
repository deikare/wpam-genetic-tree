package com.example.gentree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class OptionsActivity extends AppCompatActivity {
    ArrayList<Node> nodesArrayList;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nodesArrayList = (ArrayList<Node>)getIntent().getSerializableExtra("treeNodes");
        if (nodesArrayList == null)
            nodesArrayList = FirebaseDecorator.pullNodesArray(mAuth, db);

    }

    public void logout(View view) {
        System.out.println("logout");
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void readTree(View view) {
        System.out.println("readTree");

    }

    public void addMember(View view) {
        System.out.println("addMember");
        Intent i = new Intent(getApplicationContext(), AddNewNodeActivity.class);
        i.putExtra("treeNodes", nodesArrayList);
        startActivity(i);
        finish();
    }

    public void deleteMember(View view) {
        System.out.println("deleteMember");
        Intent i = new Intent(getApplicationContext(), DeleteRecordActivity.class);
        i.putExtra("treeNodes", nodesArrayList);
        startActivity(i);
        finish();
    }

    public void editMember(View view) {
        System.out.println("editMember");
        Intent i = new Intent(getApplicationContext(), EditNodeActivity.class);
        i.putExtra("treeNodes", nodesArrayList);
        startActivity(i);
        finish();
    }

    public void goForParentLooking(View view) {
        System.out.println("goForParentLooking");
        Intent i = new Intent(getApplicationContext(), TryToGetParentsActivity.class);
        i.putExtra("treeNodes", nodesArrayList);
        startActivity(i);
        finish();
    }

    public void goToMenu(View view) {
        System.out.println("goFor");

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("treeNodes", nodesArrayList);
        startActivity(i);
        finish();
    }
}
