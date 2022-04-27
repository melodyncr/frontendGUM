package com.example.a499_android;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class JoinGroup2 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_groups_page_2);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Join Group");
    }
}
