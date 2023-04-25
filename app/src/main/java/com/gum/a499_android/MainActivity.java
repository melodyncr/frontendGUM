package com.gum.a499_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gum.a499_android.utility.SaveSharedPreference;

public class MainActivity extends AppCompatActivity {

    private Button toLoginBtn, toCreateAccountBtn, selectScheduleBtn;

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