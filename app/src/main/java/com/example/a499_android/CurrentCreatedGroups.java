package com.example.a499_android;

import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class CurrentCreatedGroups extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference groups = db.collection("Groups");
    private DocumentReference groupDocRef;
    private ArrayList<String> members;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_created_group);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Current Groups");
        String inviteCode = getIntent().getExtras().getString("InviteCode");
        Log.d("What is invite Code", inviteCode);
        groupDocRef = groups.document(inviteCode);
        ListView membersView = findViewById(R.id.created_members_view);
        Button inviteCodeBtn = findViewById(R.id.invite_code_btn);

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
                            }
                            it.remove();
                        }
                        adapter(membersView, members);
                    }
                }
            }
        });

        inviteCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CurrentCreatedGroups.this);
                    alert.setTitle("This is your groups invite code");
                    alert.setMessage(inviteCode);
                    alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
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
            view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    /*AlertDialog.Builder alert = new AlertDialog.Builder(CreatedJoinedGroup.this);
                    alert.setTitle("Remove User From Group?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Go to page here
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alert.create().show();*/
                }
            });
        }
    }


}
