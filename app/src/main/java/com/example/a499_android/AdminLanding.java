package com.example.a499_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class AdminLanding extends AppCompatActivity {

    Button addEditTidbits, viewResponsesBtn;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Admin Landing Page");
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
}