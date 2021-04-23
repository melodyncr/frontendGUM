package com.example.a499_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.a499_android.utility.ScalableVideoView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class StartExercise extends AppCompatActivity {

    ScalableVideoView videoView;
    int i = 0;  //tracks time for progress bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise);

//        ------------------------- SET UP VIDEO VIEW -------------------------

        videoView = findViewById(R.id.scalableVideoView);
        // video files will later follow this naming format: "avatarName_workoutName.mp4"
        // create string based off of current avatar and selected workout
//        String videoName = "counter_swing"; // do not include file extension in name
        String videoName = SelectWorkout.selectedWorkout; // do not include file extension in name

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

//        ------------------------- TIMER AND PROGRESS BAR -------------------------

        long mMilliseconds = 10000; //length of all timers

        //format time for countdown display
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        //Countdown Timer
        TextView countDown = (TextView) findViewById(R.id.timer);;

        //Progress Bar
        ProgressBar mProgressBar;
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        mProgressBar.setProgress(i);

        // create timer for activity completion
        Timer timer = new Timer();
        timer.schedule(new TimerTask()  {
            @Override
            public void run()   {   // switch activity intent after timer expires
                Intent intent = new Intent(StartExercise.this, FinishedExerciseActivity.class);
                startActivity(intent);
                finish();
            }
        }, mMilliseconds);

        //Counter Down for Timer Display
        CountDownTimer mCountDownTimer = new CountDownTimer(mMilliseconds, 1000) {
            public void onTick(long millisUntilFinished) {
                countDown.setText(mSimpleDateFormat.format(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                countDown.setText(mSimpleDateFormat.format(0));
            }
        };

        //Progress Bar Countdown Timer
        CountDownTimer mProgressCountDownTimer = new CountDownTimer(mMilliseconds, 100) {
            @Override
            public void onFinish() {
                i++;
                mProgressBar.setProgress(100);
            }

            public void onTick(long millisUntilFinished) {
                i++;
                mProgressBar.setProgress((int)i*100/((int)mMilliseconds/100));
            }
        };

        //Start countdown timers
        mCountDownTimer.start();
        mProgressCountDownTimer.start();

    } // End of onCreate()

} // End of Class