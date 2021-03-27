package com.example.a499_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        wiredUp();

        addEditTidbits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLanding.this, Tidbits.class));
            }
        });

        viewResponsesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminLanding.this, ViewResponses.class));
            }
        });

    }

    private void wiredUp() {
        addEditTidbits = findViewById(R.id.addEditTidbits);
        viewResponsesBtn = findViewById(R.id.viewResponsesBtn);
    }
}