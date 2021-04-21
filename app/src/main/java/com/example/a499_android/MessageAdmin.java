package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MessageAdmin extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> messageList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference messageDoc;
    public String TAG = "Message Admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_admin);

        if(AdminMsgList.fromAdmin){
            messageDoc = db.collection("Messages").document(AdminMsgList.userNameSelected);
        }else {
            messageDoc = db.collection("Messages").document(LoginActivity.loggedUserName);
        }
        init_firebase();
        Button sendBtn = findViewById(R.id.sendMsgBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText msgText = findViewById(R.id.message_text);
                if(msgText.getText().toString().equals("")){
                    Toast.makeText(MessageAdmin.this, "No blank messages!", Toast.LENGTH_SHORT).show();
                }else {
                    String message = "From " + LoginActivity.loggedUserName;
                    message = message + "\n" + msgText.getText().toString();
                    Date date = Calendar.getInstance().getTime();
                    date.getTime();
                    message = message + "\n" + "Sent at " + date.toString();
                    messageList.add(message);
                    Object obj = messageList;
                    messageDoc.update("messages", obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MessageAdmin.this, "Message sent!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MessageAdmin.this, MessageAdmin.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
        // create timer for activity completion (add visual timer later)
    }
    void init_firebase(){
        messageDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals("messages")){ messageList = (ArrayList<String>) document.get("messages"); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        recyclerView = (RecyclerView) findViewById(R.id.messages_recycler_view);
                        Log.d(TAG, messageList.toString() + LoginActivity.loggedUserName);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageAdmin.this));
                        MessageAdapter messageAdapter = new MessageAdapter(messageList);
                        recyclerView.setAdapter(messageAdapter);

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
