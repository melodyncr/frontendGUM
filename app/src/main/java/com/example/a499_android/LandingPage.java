package com.example.a499_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LandingPage extends AppCompatActivity {

    public static final String EXTRA = "LandingPage EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //connect to db and retrieve user info
            //username, points

    }

    // Intent Factory
    public static Intent getIntent(Context context, String val){
        Intent intent = new Intent(context, LandingPage.class);
        intent.putExtra(EXTRA, val);
        return intent;
    }

    //maybe retrieve schedule and pass it through intent first
//        public void startEditScheduleActivity(View view){
//            Intent intent = EditSchedule.getIntent(this, "")
//        }

}
