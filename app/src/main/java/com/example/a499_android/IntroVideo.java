package com.example.a499_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class IntroVideo extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> video = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference videosDoc;
    Timer timer;
    public String TAG = "Intro Video";
    String delay;
    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_video);
        videosDoc = db.collection("Videos").document("IntroVideo");
        init_firebase();
        continueBtn = findViewById(R.id.introContBttn);
        continueBtn.setVisibility(View.INVISIBLE);
        // create timer for activity completion (add visual timer later)
    }
    void init_firebase(){
        videosDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals("intro_video")){ video = (ArrayList<String>) document.get("intro_video"); }
                            if(pair.getKey().toString().equals("description")){ description = (ArrayList<String>) document.get("description"); }

                            if(pair.getKey().toString().equals("delay")){ delay = (String) document.get("delay");}
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        recyclerView = (RecyclerView) findViewById(R.id.videosListRecyclerView);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(IntroVideo.this));
                        Log.d(TAG, video.toString());
                        Log.d(TAG, description.toString());

                        VideoAdapter videosAdapter = new VideoAdapter(video,description);
                        recyclerView.setAdapter(videosAdapter);
                        Log.d(TAG,  "delay" + delay);

                        timer = new Timer();
                        int delay_int = Integer.parseInt(delay);// note the delay is on firebase
                        timer.schedule(new TimerTask()  {
                            @Override
                            public void run()   {   // switch activity intent after timer expires
                                Intent intent = new Intent(IntroVideo.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                //continueBtn.setVisibility(View.VISIBLE);
                                /*AlertDialog.Builder alert = new AlertDialog.Builder(IntroVideo.this);
                                alert.setTitle("Orientation");
                                alert.setMessage("Would you like the link to the orientation");
                                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Go to page here
                                        Intent intent = new Intent(IntroVideo.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        new Intent(IntroVideo.this, LoginActivity.class);
                                        //
                                    }
                                });
                                alert.create().show();*/
                            }
                        }, delay_int);// one minute delay
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //return weeklyQuestionsList;
    }

}
