package com.example.a499_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button toLoginBtn, toCreateAccountBtn, selectScheduleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtons();

        toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLoginIntent = new Intent(MainActivity.this, LoginActivity.class); //or whatever the login activity is called
                startActivity(toLoginIntent);
            }
        });

        toCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toCreateAccountBtn = new Intent(MainActivity.this, CreateAccount.class);
                startActivity(toCreateAccountBtn);
            }
        });

    }

    public void startLandingPageActivity(View view){
        Intent intent = LandingPage.getIntent(this, "");
        startActivity(intent);
    }

    private void setButtons() {
        toLoginBtn = findViewById(R.id.toLoginBtn);
        toCreateAccountBtn = findViewById(R.id.toCreateAccountBtn);
    }
}