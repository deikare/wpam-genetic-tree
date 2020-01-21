package com.example.gentree;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();



    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void addNewRecord(View view) {
        startActivity(new Intent(getApplicationContext(), AddNewNodeActivity.class));
        finish();
    }

    public void readRecord(View view) {
        final TextView textView = findViewById(R.id.textView2);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        ArrayList<Node> nodes = new ArrayList<>();

        final String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        String result = "";
        nodes = FirebaseDecorator.pullNodesArray(mAuth, db);
        for (Node node : nodes) {
            System.out.println(node.toJson(node.getNumberofParent()));
            result += "{ name = " + node.getAttributes().get("name") + ", n = " + node.getNumber() + ", p = " + node.getNumberofParent() + " }\n";
        }
        textView.setText(result);

       /* System.out.println("usuwam");
        FirebaseDecorator.deleteNodes(mAuth, db);
        System.out.println("dodaje na powrot");
        FirebaseDecorator.pushNodes(mAuth, db, nodes);*/


    }

    public void deleteRecord(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        ArrayList<Node> nodes = FirebaseDecorator.pullNodesArray(mAuth, db);

        Tree toSend = Tree.treeFromNodesArray(nodes);
        String treeJSON = toSend.toJson();
        Intent i = new Intent(getApplicationContext(), PrintNodesActivity.class);
        i.putExtra("tree", treeJSON);
//        ArrayList<Node> nodes = toSend.toNodeArrayList();
//        i.putExtra("tree", nodes);
        i.putExtra("treeNodes", nodes);
        startActivity(i);
        finish();
        /*startActivity(new Intent(getApplicationContext(), DeleteRecordActivity.class));
        finish();*/
    }

    public void editRecord(View view) {
        startActivity(new Intent(getApplicationContext(), EditNodeActivity.class));
        finish();
    }

    public void searchForParents(View view) {
        startActivity(new Intent(getApplicationContext(), TryToGetParentsActivity.class));
        finish();
    }
}
