package com.example.nabneeteduapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }
    public void picClicked(View view){
        String abc = view.getResources().getResourceName(view.getId());


        Log.i("abc",abc);

        String imageName = abc.substring(abc.length()-6);
        Log.i("imageName",imageName);

        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("imageName",imageName);
        intent.putExtra("level",Integer.parseInt(imageName.substring(3,4)));
        startActivity(intent);

    }
}