package com.example.a499_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.savedstate.SavedStateRegistryOwner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

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
    private ImageButton ex1, ex2, ex3;
    private Button backButton;
    List<String> unlockedAvatars;
    DocumentReference userDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_avatar);
        wiredUp();

        String username = SaveSharedPreference.getUserName(SelectAvatar.this);
        // Access a Cloud Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userDocRef = db.collection("Users").document(username);
        readData(new FirestoreCallback() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if(document.exists()) {
                    unlockedAvatars = (List<String>) document.getData().get("UnlockedAvatars");
                    Log.d("Unlocked Avatars", String.valueOf(unlockedAvatars));
                } else {
                    Toast.makeText(SelectAvatar.this, "Unable to Load User Data", Toast.LENGTH_SHORT).show();
                }
            }
        }, userDocRef);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent landingPageIntent = new Intent(SelectAvatar.this, LandingPage.class);
                startActivity(landingPageIntent);
            }
        });

        /**
         *
         */

        ex1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImage("dead.png");
            }
        });
        ex2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUnlocked("gum_.png")) {
                    setImage("gum_.png");
                } else {
                    Log.d("","");
                }
            }
        });

        ex3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUnlocked("thinkingemoji.png")) {
                    setImage("thinkingemoji.png");
                } else {
                    Log.d("","");
                }
            }
        });
    }

    private boolean isUnlocked(String avatarUrl) {
        if (unlockedAvatars.contains(avatarUrl)) {
            return true;
        } else {
            Toast.makeText(SelectAvatar.this,
                    "You have not unlocked this avatar",
                    Toast.LENGTH_SHORT).show();
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
        backButton = findViewById(R.id.backBtn);
        ex1 = findViewById(R.id.example1);
        ex2 = findViewById(R.id.example2);
        ex3 = findViewById(R.id.example3);
    }
}