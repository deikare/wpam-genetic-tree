package com.example.gentree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
    }

    public void logout(View view) {
        System.out.println("logout");
    }

    public void readTree(View view) {
        System.out.println("readTree");
    }

    public void addMember(View view) {
        System.out.println("addMember");
    }

    public void deleteMember(View view) {
        System.out.println("deleteMember");
    }

    public void editMember(View view) {
        System.out.println("editMember");
    }

    public void goForParentLooking(View view) {
        System.out.println("goForParentLooking");
    }

    public void goToMenu(View view) {
    }
}
