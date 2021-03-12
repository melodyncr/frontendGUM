package com.example.a499_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class SelectAvatar extends AppCompatActivity {

    private ImageButton ex1, ex2;
    private Button confirmButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);
        wiredUp();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent landingPageIntent = new Intent(SelectAvatar.this, LandingPage.class);
                startActivity(landingPageIntent);
            }
        });

        ex1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SelectAvatar.this,
                        "You have chosen ex1! The backend code will be in this",
                        Toast.LENGTH_SHORT).show();
            }
        });
        ex2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SelectAvatar.this,
                        "EX2!!!! The backend code will be in this",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void wiredUp() {
        confirmButton = findViewById(R.id.confirmBtn);
        backButton = findViewById(R.id.backBtn);
        ex1 = findViewById(R.id.example1);
        ex2 = findViewById(R.id.example2);
    }
}