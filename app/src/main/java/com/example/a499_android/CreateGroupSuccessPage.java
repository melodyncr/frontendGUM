package com.example.a499_android;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class CreateGroupSuccessPage extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_groups_success);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create Group");
    }
}
