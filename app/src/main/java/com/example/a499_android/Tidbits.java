package com.example.a499_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.a499_android.utility.TidbitsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Tidbits extends AppCompatActivity {

    private final String TAG = "Tidbits.java";

    ActionBar actionBar;
    CollectionReference tidbits;
    List<String> tidBitsList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tidbits);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Tidbits");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        tidbits = db.collection("Tidbits");

        recyclerView = findViewById(R.id.tidbitsView);

        getTidbits(new FirestoreCallback() {
            @Override
            public void onSuccess(List<String> tidbits) {
                if(tidbits.size() >= 0) {
                    tidBitsList = tidbits;
                    Log.d("In onCreate, ", String.valueOf(tidBitsList));
                    TidbitsAdapter tidbitsAdapter = new TidbitsAdapter(Tidbits.this, tidBitsList);
                    recyclerView.setAdapter(tidbitsAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Tidbits.this));
                } else {
                    Log.d(TAG, "Something happened!");
                }
            }
        });
    }

    private void getTidbits(FirestoreCallback firestoreCallback) {

        tidbits.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<String> listOfTidbits = new ArrayList<String>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String tidbitVal = document.getString("Tidbit");
                        listOfTidbits.add(tidbitVal);
                    }
                    firestoreCallback.onSuccess(listOfTidbits);
                } else {
                    Log.d(TAG, "=====================");
                }
            }
        });
    }

    private interface FirestoreCallback {
        void onSuccess(List<String> tidbits);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tidbits, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_tidbit:
                Log.d("Add Tidbit", "Clicked");
        }

        return super.onOptionsItemSelected(item);
    }


}