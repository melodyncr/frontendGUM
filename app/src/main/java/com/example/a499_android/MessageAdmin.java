package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    ArrayList<String> priorityList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference messageDoc;
    DocumentReference usersList;
    public String TAG = "Message Admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_admin);

        if(AdminMsgList.fromAdmin){
            // document the user selected
            messageDoc = db.collection("Messages").document(AdminMsgList.userNameSelected);
        }else {
            // get the document that has all the messages from the loggin user
            messageDoc = db.collection("Messages").document(LoginActivity.loggedUserName);
        }
        usersList = db.collection("Users_List").document("List");
        query1();
        ImageView sendBtn = findViewById(R.id.sendMsgbtn);
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
                    query2();
                    messageDoc.update("messages", obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //makes sure message is sent before intenting
                            Toast.makeText(MessageAdmin.this, "Message sent!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MessageAdmin.this, MessageAdmin.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }
    void query1(){
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
                        // gets the list of messages and puts them in the recycler view
                        recyclerView = (RecyclerView) findViewById(R.id.messages_recycler_view);
                        Log.d(TAG, messageList.toString() + LoginActivity.loggedUserName);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MessageAdmin.this));
                        MessageAdapter messageAdapter = new MessageAdapter(messageList);
                        recyclerView.setAdapter(messageAdapter);
                        if(AdminMsgList.fromAdmin) { query2(); }
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


    // if this is the admin, we will remove the user from the priority list if the user is in it
    void query2() {
        usersList.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            if (pair.getKey().toString().equals("priority_list")) { priorityList = (ArrayList<String>) document.get("priority_list"); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        if(AdminMsgList.fromAdmin) {
                            Log.d(TAG, priorityList.toString() + "Before");
                            for (int i = 0; i < priorityList.size(); i++) {
                                if (AdminMsgList.userNameSelected.equals(priorityList.get(i))) {
                                    priorityList.remove(i);
                                }
                            }
                            Object priorityListObj = priorityList;
                            usersList.update("priority_list", priorityListObj);
                            Log.d(TAG, priorityList.toString() + "After");
                        }else{
                            boolean add_to_list = true;
                            Log.d(TAG, priorityList.toString() + "before loggin in");
                            for (int i = 0; i < priorityList.size(); i++) {
                                if (LoginActivity.loggedUserName.equals(priorityList.get(i))) {
                                    i =priorityList.size();
                                    add_to_list = false;
                                }
                            }
                            if(add_to_list){
                                priorityList.add(LoginActivity.loggedUserName);
                                Object priorityListObj = priorityList;
                                usersList.update("priority_list", priorityListObj);
                            }
                            Log.d(TAG, priorityList.toString() + "After");
                        }
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
