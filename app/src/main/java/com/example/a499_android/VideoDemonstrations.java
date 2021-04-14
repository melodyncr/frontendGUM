package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class VideoDemonstrations extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> video_list = new ArrayList<>();
    ArrayList<String> description_list = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference videosDoc;
    Timer timer;
    public String TAG = "Video Demo";
    String delay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_demonstration);
        videosDoc = db.collection("Videos").document(LandingPage.fitnessLevel);
        init_firebase();
    }

    void init_firebase() {
        videosDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            if (pair.getKey().toString().equals("videos_list")) { video_list = (ArrayList<String>) document.get("videos_list");}
                            if (pair.getKey().toString().equals("video_description")){description_list = (ArrayList<String>) document.get("video_description");}
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        recyclerView = (RecyclerView) findViewById(R.id.videosListRecyclerView_Lvl);
                        recyclerView.setHasFixedSize(true);
                        Log.d(TAG, video_list.toString() + "\n" + description_list.toString());
                        recyclerView.setLayoutManager(new LinearLayoutManager(VideoDemonstrations.this));
                        VideoAdapter videosAdapter = new VideoAdapter(video_list,description_list);
                        recyclerView.setAdapter(videosAdapter);
                        Log.d(TAG, "delay" + delay);

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
