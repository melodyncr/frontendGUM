package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class VideoDemonstrations extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    RecyclerView recyclerView;
    ArrayList<String> video_list = new ArrayList<>();
    ArrayList<String> description_list = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference videosDoc;
    Spinner spinner;
    public String categoryStr = "";
    public String TAG = "Video Demo";
    String delay;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_demonstration);
        spinner = findViewById(R.id.spinnerVideo);
        recyclerView = (RecyclerView) findViewById(R.id.videosListRecyclerView_Lvl);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(VideoDemonstrations.this));
        videosDoc = db.collection("Videos").document(LandingPage.fitnessLevel);
        query_for_levels();
        spinner.setOnItemSelectedListener(this);
        String[] levels = new String[]{"Select a Category","Beginner", "Intermediate","Advance"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, levels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    void query_for_levels() {
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
                        Log.d(TAG, video_list.toString() + "\n" + description_list.toString());
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        categoryStr = parent.getItemAtPosition(position).toString();
        if(categoryStr.equals("Select a Category")){
            //do nothing, no query is executed.
        }else {
            videosDoc = db.collection("Videos").document(categoryStr);
            query_for_levels();
        }
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
