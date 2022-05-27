package com.example.a499_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a499_android.utility.SaveSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class CurrentJoinedGroups extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users = db.collection("Users");
    private CollectionReference groups = db.collection("Groups");
    private DocumentReference usersDocRef;
    private DocumentReference groupDocRef;
    private ArrayList<String> members;
    private ArrayList<String> membersInfo; //Store member info

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_joined_groups);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Current Groups");
        String inviteCode = getIntent().getExtras().getString("InviteCode");
        Log.d("What is invite Code", inviteCode);
        groupDocRef = groups.document(inviteCode);
        ListView membersView = findViewById(R.id.joined_members_view);
        membersInfo = new ArrayList<String>();
        Button leaveGroup = findViewById(R.id.leave_group_btn);

        groupDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        Map<String, Object> data = doc.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            if (pair.getKey().toString().equals("Members")) {
                                members = (ArrayList<String>) doc.get("Members");
                                break;
                            }
                            if(pair.getKey().toString().equals("Group Name")){
                                actionBar.setTitle(pair.getValue().toString());
                            }
                            it.remove();
                        }

                        for(String m : members){
                            usersDocRef = users.document(m);
                            usersDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();
                                        if(doc.exists()){
                                            Map<String, Object> data = doc.getData();
                                            Iterator it = data.entrySet().iterator();
                                            //Temp variables;
                                            while(it.hasNext()){
                                                Map.Entry pair = (Map.Entry)it.next();
                                                if(pair.getKey().toString().equals("Points")){
                                                    membersInfo.add(m + " points: " + pair.getValue().toString());
                                                }
                                                it.remove();
                                            }
                                            adapter(membersView, membersInfo);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        leaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CurrentJoinedGroups.this);
                alert.setTitle("Are you sure?");
                alert.setMessage("You cannot join again without the invite code and password");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String uName = SaveSharedPreference.getUserName(CurrentJoinedGroups.this);
                        groupDocRef.update("Members", FieldValue.arrayRemove(uName))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                        users.document(uName).update("Groups", FieldValue.arrayRemove(inviteCode))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                        Intent intent = new Intent(CurrentJoinedGroups.this, ViewCurrentGroups.class);
                        finish();
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.create().show();
            }
        });
    }

    private void adapter(@NonNull ListView view, ArrayList<String> array){
        if(array.size() >= 1){
            ArrayAdapter adapt = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, array);
            view.setAdapter(adapt);
        }
    }
}
