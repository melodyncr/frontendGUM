package com.gum.a499_android;

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

import com.gum.a499_android.utility.SaveSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ViewCurrentGroups extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users = db.collection("Users");
    private CollectionReference groupCol = db.collection("Groups");
    private DocumentReference usersDocRef;
    private DocumentReference groupDocRef;
    private ArrayList<String> groups = new ArrayList<String>();
    private ArrayList<String> createdGroups = new ArrayList<String>();
    private ArrayList<String> joinedGroups = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_current_groups);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Current Groups");
        String uName = SaveSharedPreference.getUserName(ViewCurrentGroups.this);
        usersDocRef = users.document(uName);
        Button viewJoinedGroupsButton = findViewById(R.id.viewJoinedGroupsBtn);
        Button viewCreatedGroupsButton = findViewById(R.id.viewCreatedGroupsBtn);
        ListView groupsView = findViewById(R.id.groupsListView);

        //Get all groups that user has either created or is currently in.
        usersDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        Map<String, Object> data = doc.getData();
                        Iterator it = data.entrySet().iterator();
                        while(it.hasNext()){
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals("Groups")){
                                groups = (ArrayList<String>) doc.get("Groups");
                                Log.d("Right after retrieve", String.valueOf(groups.size()));
                                break;
                            }
                            it.remove();
                        }
                        Log.d("Before adding to jg", String.valueOf(groups.size()));
                        for(String g : groups){
                            groupDocRef = groupCol.document(g);
                            groupDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();
                                        if(doc.exists()){
                                            Map<String, Object> data = doc.getData();
                                            Iterator it = data.entrySet().iterator();
                                            //Temp variables;
                                            String temp = "";
                                            String name= "";
                                            while(it.hasNext()){
                                                Map.Entry pair = (Map.Entry)it.next();
                                                if(pair.getKey().toString().equals("Group Name")){
                                                    name = pair.getValue().toString();
                                                }
                                                if(pair.getKey().toString().equals("Leader Name")){
                                                    if(uName.equals(pair.getValue().toString())){
                                                        temp = g + ": " + name;
                                                        createdGroups.add(temp);
                                                    } else {
                                                        temp = g + ": " + name ;
                                                        joinedGroups.add(temp);
                                                    }
                                                }

                                                it.remove();
                                            }
                                            Log.d("After adding to jg", String.valueOf(joinedGroups.size()));
                                            Log.d("After adding to cg", String.valueOf(createdGroups.size()));
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        /*
        IMPORTANT: You cannot check for groups here. You must check if
        groups are filled in one of the listeners. My guess is that when AndroidStudio runs the code,
        it runs all 'normal' code first, and then it runs the listeners afterward. This means
        that groups will be empty even though you are checking for it after the above code has
        ran.
        */



        viewJoinedGroupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("Joined", String.valueOf(groups.size()));
                adapter(groupsView,joinedGroups, false);

            }
        });

        viewCreatedGroupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter(groupsView,createdGroups, true);

            }
        });
    }

    private void adapter(@NonNull ListView view, ArrayList<String> array, boolean user){
        ArrayAdapter adapt = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, array);
        view.setAdapter(adapt);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //String temp = array.get(i).substring(6);
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewCurrentGroups.this);
                alert.setTitle("Go to " + array.get(position).substring(8) + " Page?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Go to page here
                        Intent intent;
                        if(user) {
                            intent = new Intent(ViewCurrentGroups.this, CurrentCreatedGroups.class);
                        } else {
                            intent = new Intent(ViewCurrentGroups.this, CurrentJoinedGroups.class);
                        }
                        intent.putExtra("InviteCode", array.get(position).substring(0,6));
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
}
