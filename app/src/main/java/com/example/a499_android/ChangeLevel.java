package com.example.a499_android;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class ChangeLevel extends AppCompatActivity {
    Button changeLevel;
    RadioGroup radioGroup;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_level);

        changeLevel = findViewById(R.id.changeLevel);
        radioGroup = findViewById(R.id.radioQuestionsGroupLevel);

        changeLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = isChecked();
                if(checked){
                    // change users fitness level, we can do a simple update query and set the str to an object and update the users fitness level
                    docRef = db.collection("Users").document(LoginActivity.loggedUserName);
                    Object obj_name = LandingPage.fitnessLevel;
                    docRef.update("FitnessLvl",obj_name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ChangeLevel.this, "Level has been changed! Remember if you feel it's too difficult you can always go back!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangeLevel.this, LandingPage.class);
                            startActivity(intent);
                        }
                    });
                }else {
                    //do nothing, since a button wasn't checked
                }
            }
        });

    }

    boolean isChecked() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            return false;
        } else {
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);
            if(radioButton.getText().toString().equals("Easy")){ LandingPage.fitnessLevel = "Beginner"; }
            if(radioButton.getText().toString().equals("Moderate")){ LandingPage.fitnessLevel = "Intermediate"; }
            if(radioButton.getText().toString().equals("Vigorous")){ LandingPage.fitnessLevel = "Advance"; }
            return true;
        }
    }

}
