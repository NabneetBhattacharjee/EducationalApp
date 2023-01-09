package com.example.nabneeteduapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    TextView time,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        userName = findViewById(R.id.name_Text);
        time = findViewById(R.id.time_Text);
        userName.setText(getIntent().getStringExtra("userName"));
        time.setText(String.valueOf(getIntent().getIntExtra("time",1)));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(ResultActivity.this,SettingActivity.class);
                startActivity(i);
            }
        },4000);
    }
}