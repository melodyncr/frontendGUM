package com.example.a499_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AnimationTest extends AppCompatActivity {

    public static final String EXTRA = "AnimationTest EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_animation);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button start = (Button)findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView walk = (ImageView)findViewById(R.id.imageView2);
                walk.setImageResource(R.drawable.walking);
                AnimationDrawable walkingMan = (AnimationDrawable)walk.getDrawable();
                walkingMan.start();
            }
        });
    }

    // Intent Factory
    public static Intent getIntent(Context context, String val){
        Intent intent = new Intent(context, AnimationTest.class);
        intent.putExtra(EXTRA, val);
        return intent;
    }

}
