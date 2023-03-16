package com.example.a499_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.a499_android.utility.SaveSharedPreference;
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
     //Testing
    // main landing page, to create an account or login to an account
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtons();

        if(SaveSharedPreference.getUserName(MainActivity.this).length() > 0) {
            Log.d("Pref", SaveSharedPreference.getUserName(MainActivity.this) + " is found");
            Intent toLandingPageIntent = new Intent(MainActivity.this, LandingPage.class);
            startActivity(toLandingPageIntent);
        } else {
            Log.d("Pref", "username not saved. Not logged in");
        }

        toCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toCreateAccountBtn = new Intent(MainActivity.this, Agreement.class);
                startActivity(toCreateAccountBtn);
            }
        });

        //This doesn't work. toLoginBtn is not a button, its a TextView
/*
        toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLoginIntent = new Intent(MainActivity.this, LoginActivity.class); //or whatever the login activity is called
                startActivity(toLoginIntent);
            }
        });
*/

    }

    private void setButtons() {
//        toLoginBtn = findViewById(R.id.toLoginBtn);
        toCreateAccountBtn = findViewById(R.id.toCreateAccountBtn);
    }

    public void toLogin(View view) {
        Intent toLoginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(toLoginIntent);
    }
}