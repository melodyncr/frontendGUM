package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class AddVideos extends AppCompatActivity {
    Button addVideoBtn;
    EditText videoDescription;
    EditText videoID;
    RadioGroup radioGroup;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference videosDoc;
    String TAG = "Add Videos";
    RadioButton b1, b2, b3;
    private ArrayList<String> videoDescList = new ArrayList<>();
    private ArrayList<String> videoIFrame = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_videos);

        //set objects to layout file
        addVideoBtn = findViewById(R.id.uploadVideo);
        videoDescription = findViewById(R.id.addVideoDescription);
        videoID = findViewById(R.id.addVideoCode);
        radioGroup = findViewById(R.id.radioQuestionsGroupV);
        b1 = findViewById(R.id.question1V);
        b2 = findViewById(R.id.question2V);
        b3 = findViewById(R.id.question3V);

        addVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = isChecked();
                Log.d(TAG, category + "id");
                if(category.equals("")) {
                    Toast.makeText(AddVideos.this, "Category not selected empty or not 11 characters!", Toast.LENGTH_SHORT).show();
                }else{
                    boolean uploadVideo = true;
                    if (videoDescription.getText().toString().equals("")) {
                        Toast.makeText(AddVideos.this, "Empty Description!", Toast.LENGTH_SHORT).show();
                        uploadVideo = false;
                    }
                    if (videoID.getText().toString().length() != 11) {
                        Toast.makeText(AddVideos.this, "ID is empty or not 11 characters!", Toast.LENGTH_SHORT).show();
                        uploadVideo = false;
                    }
                    if (uploadVideo) {
                        videosDoc = db.collection("Videos").document(category);
                        String i_frame_str = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"+videoID.getText().toString()+"\" frameborder=\"0\" allowfullscreen></iframe>";
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
                                            if(pair.getKey().toString().equals("video_description")){ videoDescList = (ArrayList<String>) document.get("video_description"); }
                                            if(pair.getKey().toString().equals("videos_list")){ videoIFrame = (ArrayList<String>) document.get("videos_list"); }
                                            it.remove(); // avoids a ConcurrentModificationException
                                        }
                                        videoDescList.add(videoDescription.getText().toString());
                                        videoIFrame.add(i_frame_str);
                                        Object vDescObj = videoDescList;
                                        Object vIfObj = videoIFrame;
                                        videosDoc.update("video_description",vDescObj);
                                        videosDoc.update("videos_list",vIfObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.d(TAG, "Both videos are updated!");
                                                Toast.makeText(AddVideos.this, "Video has been uploaded!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(AddVideos.this, AdminLanding.class));
                                            }
                                        });

                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                }
            }
        });


    }

    String isChecked() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(AddVideos.this, "No answer has been selected", Toast.LENGTH_SHORT).show();
            return "";
        } else {
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);
            String category = radioButton.getText().toString();
            return category;
        }
    }

}