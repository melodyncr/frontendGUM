package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class JoinGroup extends AppCompatActivity {

    private final String TAG = "JoinGroup";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference groups = db.collection("Groups");
    DocumentReference groupDocRef;
    private String inviteCode;
    private String groupName;
    private String leader;
    private String groupPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Join Group");
        Button searchGroupBtn = findViewById(R.id.searchGroupBtn);
        Button joinGroupBtn = findViewById(R.id.joinFoundGroup);
        TextView results = findViewById(R.id.searchResults);
        results.setVisibility(View.INVISIBLE);
        joinGroupBtn.setVisibility(View.INVISIBLE);

        searchGroupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText inviteCodeText = findViewById(R.id.groupInviteCode);
                inviteCode = inviteCodeText.getText().toString();
                groupDocRef = groups.document(inviteCode);
                //Toast.makeText(JoinGroup.this, "Invite code is " + iC.length(), Toast.LENGTH_SHORT).show();
                if(inviteCode.length() == 6) {
                        groupDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    if (doc.exists()) {
                                        joinGroupBtn.setVisibility(View.VISIBLE);
                                        results.setVisibility(View.VISIBLE);
                                        Map<String, Object> data = doc.getData();
                                        Iterator it = data.entrySet().iterator();
                                        while(it.hasNext()){
                                            Map.Entry pair = (Map.Entry)it.next();
                                            if(pair.getKey().toString().equals("Leader Name")){
                                                leader = pair.getValue().toString();
                                            }
                                            if(pair.getKey().toString().equals("Group Name")){
                                                groupName = pair.getValue().toString();
                                            }
                                            if(pair.getKey().toString().equals("Password")){
                                                groupPassword = pair.getValue().toString();
                                            }
                                            it.remove(); // avoids a ConcurrentModificationException
                                        }
                                        results.setText("Group Name " + groupName + "\nLeader Name " + leader);
                                    } else {
                                        Toast.makeText(JoinGroup.this, "This group doesn't exist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                } else {
                    Toast.makeText(JoinGroup.this, "Invite code must be 6 digits long."
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

        joinGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JoinGroup.this, JoinGroup2.class);
                intent.putExtra("Invite Code",inviteCode);
                intent.putExtra("Group Name", groupName);
                intent.putExtra("Leader Name", leader);
                intent.putExtra("Group Password", groupPassword);
                startActivity(intent);
                finish();
            }
        });
    }
}
