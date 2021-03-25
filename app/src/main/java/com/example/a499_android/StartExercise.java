package com.example.a499_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import com.example.a499_android.utility.ScalableVideoView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Timer;
import java.util.TimerTask;

public class StartExercise extends AppCompatActivity {

    ScalableVideoView videoView;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise);

        videoView = findViewById(R.id.scalableVideoView);
        // video files will later follow this naming format: "avatarName_workoutName.mp4"
        // create string based off of current avatar and selected workout
        String videoName = "counter_swing"; // do not include file extension in name

        Context mContext = getApplicationContext();
        int videoResId = mContext.getResources().getIdentifier(videoName, "raw", mContext.getPackageName());  // retrieve the resource id number for a video located in the raw directory
        videoView.setVideoPath("android.resource://"+mContext.getPackageName()+"/"+videoResId);  // tell video view what file to play

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);    // loop video
                videoView.start();  // start video
            }
        });

        // create timer for activity completion (add visual timer later)
        timer = new Timer();
        timer.schedule(new TimerTask()  {
            @Override
            public void run()   {   // switch activity intent after timer expires
                Intent intent = new Intent(StartExercise.this, LandingPage.class);
                startActivity(intent);
                finish();
            }
        }, 20000);  // this is 20 seconds for testing

    } // End of onCreate()

} // End of Class