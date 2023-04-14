package com.example.a499_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.a499_android.utility.ScalableVideoView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class StartExercise extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference videos = db.collection("Videos");
    DocumentReference docRef = db.collection("Videos").document();
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    private YouTubePlayerView youTubePlayerView;
    int i = 0;  //tracks time for progress bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise);

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

//        ------------------------- SET UP VIDEO VIEW -------------------------

//        videoView = findViewById(R.id.scalableVideoView);
        // video files will later follow this naming format: "avatarName_workoutName.mp4"
        // create string based off of current avatar and selected workout
//        String videoName = "counter_swing"; // do not include file extension in name
        String videoName = SelectWorkout.selectedWorkout; // do not include file extension in name

        Context mContext = getApplicationContext();
//        int videoResId = mContext.getResources().getIdentifier(videoName, "raw", mContext.getPackageName());  // retrieve the resource id number for a video located in the raw directory
//        videoView.setVideoPath("android.resource://"+mContext.getPackageName()+"/"+videoResId);  // tell video view what file to play
//        Log.d("TAG", "Current context is " + mContext.getFilesDir().getAbsoluteFile().getAbsolutePath());
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);    // loop video
//                videoView.start();  // start video
//            }
//        });

//        docRef.getDocument("path/to/youtube_link");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String youtubeLink = dataSnapshot.getValue(String.class);
//                // Insert the YouTube link into the XML file using the steps from the previous answer
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle the error
//            }
//        });
        try{
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File("activity_start_exercise.xml"));
            doc.getElementsByTagName("videoId");
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtubeplayerId);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
//                videos.document();
                String videoId = "t2uU6yaZXn8";
                youTubePlayer.loadVideo(videoId, 0);
            }
        });

//        ------------------------- TIMER AND PROGRESS BAR -------------------------

        long mMilliseconds = 20000; //length of all timers

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