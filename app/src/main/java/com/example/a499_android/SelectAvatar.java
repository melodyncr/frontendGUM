package com.example.a499_android;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a499_android.utility.SaveSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SelectAvatar extends AppCompatActivity {

    public static final String TAG = "Select Avatar: ";
    private PopupWindow confirmTxt;
    private LayoutInflater layoutInflater;
    private ImageButton ex1, ex2, ex3;
    // TextViews for unlocked or locked avatars
    private TextView tv1, tv2, tv3;
    private Button backButton, yesBuyBtn, noBuyBtn;
    private TextView ptsView, confirmQuestion;
    List<String> unlockedAvatars;
    private long fitnessPts; // fitness points
    DocumentReference userDocRef;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Select Avatar");
        actionBar.setDisplayHomeAsUpEnabled(true);

        wiredUp();

        final WindowManager w = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final Display d = w.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        d.getMetrics(dm);

        String username = SaveSharedPreference.getUserName(SelectAvatar.this);
        // Access a Cloud Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userDocRef = db.collection("Users").document(username);
        readData(new FirestoreCallback() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if (document.exists()) {
                    unlockedAvatars = (List<String>) document.getData().get("UnlockedAvatars");
                    if (unlockedAvatars.contains("nature.png")) {
                        tv1.setText("Nature Avatar Unlocked");
                    }
                    if (unlockedAvatars.contains("alien.png")) {
                        tv2.setText("Alien Avatar Unlocked");
                    }else{
                        tv2.setText("Alien Avatar Locked");
                    }
                    if (unlockedAvatars.contains("galactic.png")) {//will change later
                        tv3.setText("Galactic Avatar Unlocked");
                    } else {
                        tv3.setText("Galactic Avatar Locked");
                    }
                    Log.d("Unlocked Avatars", String.valueOf(unlockedAvatars));
                    fitnessPts = (long) document.getData().get("Points");
                    Log.d("Longevity Points", String.valueOf(fitnessPts));
                    ptsView.setText(String.valueOf(fitnessPts));
//                    actionBar.setSubtitle("FP: " + String.valueOf(fitnessPts));
                } else {
                    Toast.makeText(SelectAvatar.this, "Unable to Load User Data", Toast.LENGTH_SHORT).show();
                }
            }
        }, userDocRef);

//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent landingPageIntent = new Intent(SelectAvatar.this, LandingPage.class);
//                startActivity(landingPageIntent);
//            }
//        });

        /**
         * Options
         */

        ex1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImage("nature.png");
            }
        });

        ex2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isUnlocked("alien.png")) {
                    setImage("alien.png");
                } else {
                    onButtonShowPopupWindowClick(view, "alien.png", 100);// the green avatar costs 100 points
                }
            }
        });

        ex3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUnlocked("galactic.png")) {
                    setImage("galactic.png");
                } else {
                    onButtonShowPopupWindowClick(view, "galactic.png", 300); // the yellow avatar costs 300 points
                }
            }
        });

    }

    private boolean isUnlocked(String avatarUrl) {
        if (unlockedAvatars.contains(avatarUrl)) {
            return true;
        } else {
            //Toast.makeText(SelectAvatar.this, "You have not unlocked this avatar", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void setImage(String imageName) {
        userDocRef.update("AvatarUrl", imageName).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SelectAvatar.this,
                        "Image set",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SelectAvatar.this,
                        "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readData(FirestoreCallback firestoreCallback, DocumentReference userDocRef) {
        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        firestoreCallback.onSuccess(document);
                    }
                } else {
                    Log.d(TAG, "ERROR: ", task.getException());
                }
            }
        });
    }

    private interface FirestoreCallback {
        void onSuccess(DocumentSnapshot document);
    }

    private void wiredUp() {
        ex1 = findViewById(R.id.example1);
        ex2 = findViewById(R.id.example2);
        ex3 = findViewById(R.id.example3);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        ptsView = findViewById(R.id.selectAvatarPts);
    }

    public void onButtonShowPopupWindowClick(View view, String avatarUrl, int cost) {
        final WindowManager w = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        final Display d = w.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        d.getMetrics(dm);
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        int width = (int) (dm.widthPixels * 0.8);
        int height = (int) (dm.heightPixels * 0.5);
        Log.d("Width and height...", width + " : " + height);
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        yesBuyBtn = popupView.findViewById(R.id.popupYesBtn);
        noBuyBtn = popupView.findViewById(R.id.popupNoBtn);
        confirmQuestion = popupView.findViewById(R.id.confirmText);
        confirmQuestion.setText("Do you want to purchase this avatar for " + cost + "?");
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        yesBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click", "YES works");
                if (fitnessPts < cost) {
                    Toast.makeText(SelectAvatar.this,
                            "Not enough points to purchase",
                            Toast.LENGTH_SHORT).show();
                } else {
                    unlockedAvatars.add(avatarUrl);
                    userDocRef.update("UnlockedAvatars", unlockedAvatars);
                    fitnessPts = fitnessPts - cost;
                    userDocRef.update("Points", fitnessPts);
                    ptsView.setText(String.valueOf(fitnessPts));
                    //.setSubtitle("FP: " + String.valueOf(fitnessPts));
                    setImage(avatarUrl);
                    popupWindow.dismiss();
                }
            }
        });

        noBuyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click", "NO works");
                popupWindow.dismiss();
            }
        });
    }
}