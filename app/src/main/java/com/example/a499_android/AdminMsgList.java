package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class AdminMsgList extends AppCompatActivity {

    ArrayList<String> usersList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usersDoc;
    public String TAG = "Admin Landing";
    public static String userNameSelected = "";
    public static boolean fromAdmin = false;
    Button finishBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list_msg);
        finishBtn = findViewById(R.id.backBtnUserList);
        finishBtn.setVisibility(View.INVISIBLE);

        usersDoc = db.collection("Users_List").document("List");
        init_firebase();

        // create timer for activity completion (add visual timer later)
    }
    void init_firebase(){
        usersDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals("user_names")){ usersList = (ArrayList<String>) document.get("user_names"); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        RecyclerView rv = findViewById(R.id.users_list_recycler_view);
                        rv.setLayoutManager(new LinearLayoutManager(AdminMsgList.this));
                        rv.setAdapter(new Adapter());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        if(ViewResponseR.select_question){
            finishBtn.setVisibility(View.VISIBLE);
        }
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //return weeklyQuestionsList;
    }

    private class Adapter extends RecyclerView.Adapter<AdminMsgList.ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(AdminMsgList.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            holder.bind(usersList.get(position));
        }

        @Override
        public int getItemCount() {
            return usersList.size();
        }

    }
    private class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.message_item, parent, false));
        }

        public void bind(String f) {
            TextView item = itemView.findViewById(R.id.message_id);
            item.setText(f);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fromAdmin = true;
                    userNameSelected = f;
                    Intent intent;
                    if(ViewResponseR.select_question){
                        intent = new Intent(AdminMsgList.this, ViewResponseUser.class);
                    }else {
                        intent = new Intent(AdminMsgList.this, MessageAdmin.class);
                    }
                    startActivity(intent);
                }
            });
        }
    }



}
