package com.gum.a499_android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gum.a499_android.utility.ScalableVideoView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class StartExerciseM extends AppCompatActivity implements SensorEventListener {

    // create variables of the two class
    private SensorManager sensorManager;
    private Sensor accerometerSensor;
    private TextView movement;
    private boolean isMeterAvailable, notFirstTime = false;
    private float currentX, currentY, currentZ, lastX, lastY, lastZ;
    private float xDifference, yDifference, zDifference;
    private float shakeThreshold = 0.2f;// the amount of motion needed to prevent audio clip to remind person to move
    private Vibrator vibrator;
    MediaPlayer move;
    ScalableVideoView videoView;
    private YouTubePlayerView youTubePlayerView;
    int i = 0;  //tracks time for progress bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movement_workout);

        movement = findViewById(R.id.moveText);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!= null){
            accerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isMeterAvailable = true;
        }else{
            movement.setText("Meter not available");
            isMeterAvailable = false;
        }
        //define media player
//        move = MediaPlayer.create(this, R.raw.move1);
//
//
//        videoView = findViewById(R.id.scalableVideoViewM);
//        // video files will later follow this naming format: "avatarName_workoutName.mp4"
//        // create string based off of current avatar and selected workout
////        String videoName = "counter_swing"; // do not include file extension in name
//        String videoName = SelectWorkout.selectedWorkout; // do not include file extension in name
//
//        Context mContext = getApplicationContext();
//        int videoResId = mContext.getResources().getIdentifier(videoName, "raw", mContext.getPackageName());  // retrieve the resource id number for a video located in the raw directory
//        videoView.setVideoPath("android.resource://"+mContext.getPackageName()+"/"+videoResId);  // tell video view what file to pla
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);    // loop video
//                videoView.start();  // start video
//            }
//        });

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtubeplayerId);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                String videoId = "Qwd25JV-jnU";
                youTubePlayer.loadVideo(videoId, 0);
            }
        });


        //        ------------------------- TIMER AND PROGRESS BAR -------------------------

        long mMilliseconds = SelectWorkout.time_mil; //length of all timers

        //format time for countdown display
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        mSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        //Countdown Timer
        TextView countDown = (TextView) findViewById(R.id.timerM);;

        //Progress Bar
        ProgressBar mProgressBar;
        mProgressBar=(ProgressBar)findViewById(R.id.progressBarM);
        mProgressBar.setProgress(i);

        // create timer for activity completion
        Timer timer = new Timer();
        timer.schedule(new TimerTask()  {
            @Override
            public void run()   {   // switch activity intent after timer expires
                Intent intent = new Intent(StartExerciseM.this, FinishedExerciseActivity.class);
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


    @Override
    public void onSensorChanged(SensorEvent event) {
        movement.setTextColor(Color.YELLOW);
        currentX = event.values[0];
        currentY = event.values[1];
        currentZ = event.values[2];

        if(notFirstTime){
             xDifference = Math.abs(lastX - currentX);
             yDifference = Math.abs(lastY - currentY);
             zDifference = Math.abs(lastZ - currentZ);

             if((xDifference > shakeThreshold && yDifference > shakeThreshold) ||
            (xDifference > shakeThreshold && zDifference > shakeThreshold) ||
                    (yDifference > shakeThreshold && zDifference > shakeThreshold)){
                     movement.setText("Great Work! Lets keep it up!");
                     if(move.isPlaying()){

                     }else{
                         Random rand = new Random();
                         int num = rand.nextInt(3);
                         if (num == 0) {
                             stopPlaying();
                             move = MediaPlayer.create(this, R.raw.good1);
                             move.start();// move 1 will be triggered
                         } else if (num == 1) {
                             stopPlaying();
                             move = MediaPlayer.create(this, R.raw.good2);
                             move.start();
                         } else {
                             stopPlaying();
                             move = MediaPlayer.create(this, R.raw.good3);
                             move.start();
                         }
                     }
                    // vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
             }else{
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                     movement.setText("Lets keep moving!");
                     vibrator.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                 }
                 Random rand = new Random();
                 int num = rand.nextInt(3);
                 if(move.isPlaying()){
                 }else {
                     if (num == 0) {
                         stopPlaying();
                         move = MediaPlayer.create(this, R.raw.move1);
                         move.start();// move 1 will be triggered
                     } else if (num == 1) {
                         stopPlaying();
                         move = MediaPlayer.create(this, R.raw.move2);
                         move.start();
                     } else {
                         stopPlaying();
                         move = MediaPlayer.create(this, R.raw.move3);
                         move.start();
                     }
                 }
             }
        }
        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;

        notFirstTime = true;
    }

    private void stopPlaying() {
        if (move != null) {
            move.stop();
            move.release();
            move= null;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume(){
        super.onResume();

        if(isMeterAvailable){
            sensorManager.registerListener(this, accerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isMeterAvailable){
            sensorManager.unregisterListener(this);
        }
    }
}
