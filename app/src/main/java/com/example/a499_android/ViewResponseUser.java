package com.example.a499_android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ViewResponseUser extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference pastResponses;
    TextView responseText;
    TextView questionText;
    ArrayList<String> responseList = new ArrayList<>();
    String TAG = "View Response User";
    Button finishBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_user_response);
            responseText = findViewById(R.id.questionTextSelectedUser);
            questionText = findViewById(R.id.questionSelectedUser);
            questionText.setText("From " + AdminMsgList.userNameSelected + "\n" + ViewResponseR.questionSelected);
            finishBtn = findViewById(R.id.finishResponseUserBtn);
            pastResponses = db.collection("Surveys").document(SelectASurvey.document_response);
            getPastResponses();
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    void getPastResponses(){
        pastResponses.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = document.getData();
                        Iterator it = data.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if(pair.getKey().toString().equals(AdminMsgList.userNameSelected)){ responseList = (ArrayList<String>) document.get(AdminMsgList.userNameSelected); }
                            it.remove(); // avoids a ConcurrentModificationException
                        }
                        Log.d(TAG, responseList.toString());
                        if(responseList.size() ==0){
                            responseText.setText("User has not responded to Survey!");
                        }else{
                            responseText.setText("Response\n" + responseList.get(GetResponseListQuery.index_charts));
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        //return surveyList;
    }
}
