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
import android.widget.Toast;

import com.example.a499_android.utility.SaveSharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SelectAvatar extends AppCompatActivity {

    public static final String TAG = "Select Avatar: ";
    private ImageButton ex1, ex2;
    private Button confirmButton, backButton;

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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent landingPageIntent = new Intent(SelectAvatar.this, LandingPage.class);
                startActivity(landingPageIntent);
            }
        });

        ex1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImage("dead.png");
            }
        });
        ex2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImage("test.png");
            }
        });
    }

    private void setImage(String imageName) {
//        readData(new FirestoreCallback() {
//            @Override
//            public void onSuccess(DocumentSnapshot document) {
//                if(document.exists()) {
//                    document
//                } else {
//                    Toast.makeText(SelectAvatar.this, "Unable to set image", Toast
//                    .LENGTH_SHORT).show();
//                }
//            }
//        }, userDocRef);
        userDocRef.update("AvatarImage", imageName).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        confirmButton = findViewById(R.id.confirmBtn);
        backButton = findViewById(R.id.backBtn);
        ex1 = findViewById(R.id.example1);
        ex2 = findViewById(R.id.example2);
    }
}