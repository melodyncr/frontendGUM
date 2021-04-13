package com.example.a499_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.a499_android.utility.TidbitsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Tidbits extends AppCompatActivity {

    private final String TAG = "Tidbits.java";

    ActionBar actionBar;
    CollectionReference tidbits;
    List<String> tidBitsList;
    List<String> tidbitsIdList;
    RecyclerView recyclerView;
    EditText newTidtbit;
    Button addNewTidbitBtn;
    ImageButton closeTidbitPopup;

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
            public void onSuccess(List<String> tbIds, List<String> tidbits) {
                if(tidbits.size() >= 0) {
                    tidBitsList = tidbits;
                    tidbitsIdList = tbIds;
                    Log.d("In onCreate, IDs: ", String.valueOf(tidbitsIdList));
                    Log.d("In onCreate, Values: ", String.valueOf(tidBitsList));
                    TidbitsAdapter tidbitsAdapter = new TidbitsAdapter(Tidbits.this, tidbitsIdList, tidBitsList);
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
                List<String> listOfIds = new ArrayList<String>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String tidbitVal = document.getString("Tidbit");
                        listOfIds.add(document.getId());
                        listOfTidbits.add(tidbitVal);
                    }
                    firestoreCallback.onSuccess(listOfIds, listOfTidbits);
                } else {
                    Log.d(TAG, "=====================");
                }
            }
        });
    }

    private interface FirestoreCallback {
        void onSuccess(List<String> tidbits, List<String> tbIdList);
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
                onButtonShowPopupWindowClick();
                Log.d("Add Tidbit", "Clicked");
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonShowPopupWindowClick() {
        final WindowManager w = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final Display d = w.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        d.getMetrics(dm);
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.tidbits_popup, null);
        View layout = getLayoutInflater().inflate(R.layout.tidbits_popup, null);
        int width = (int) (dm.widthPixels * 0.9);
        int height = (int) (dm.heightPixels * 0.8);
        Log.d("Width and height...", width + " : " + height);
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        newTidtbit = popupView.findViewById(R.id.newTidbitText);
        addNewTidbitBtn = popupView.findViewById(R.id.addNewTidbitBtn);
        closeTidbitPopup = popupView.findViewById(R.id.closeTidbitPopup);

        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        addNewTidbitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newTidbitTxt = newTidtbit.getText().toString();

                if (newTidbitTxt.length() == 0) {
                    Toast.makeText(Tidbits.this,
                            "Please Enter a tidbit",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, String> newTidbit = new HashMap<>();
                    newTidbit.put("Tidbit", newTidbitTxt);
                    tidbits.add(newTidbit).addOnSuccessListener(
                            new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    tidbitsIdList.add(documentReference.getId());
                                    tidBitsList.add(newTidbitTxt);
                                    Log.d(TAG, "Successfully added new Tidbit");
                                    Toast.makeText(Tidbits.this,
                                            "New Tidbit Added",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    recyclerView.refreshDrawableState();
                                    popupWindow.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Tidbits.this,
                                    "Something went wrong!",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            Log.e("Error: ", e.getMessage());
                        }
                    });
                }
            }
        });

        closeTidbitPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }
}