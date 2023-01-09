package com.example.nabneeteduapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LandingActivity extends AppCompatActivity {
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        dbHelper = new DBHelper(this);
        //test_db();
    }
    public void goClicked(View view){
        EditText nameText = findViewById(R.id.nameText);
        String username = nameText.getText().toString().trim();
        if(username.length() == 0){
            Toast.makeText(this,"Please enter username",Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("nabneeteduapp_items", MODE_PRIVATE);
        sharedPreferences.edit().clear().putString("username",username).apply();
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }

    private void test_db(){
        /*dbHelper.insertPlayer("Tony",50,1,"c");
        dbHelper.insertPlayer("Nabneet",70,1,"c");
        dbHelper.insertPlayer("Sarah",40,1,"c");
        dbHelper.insertPlayer("Tony",60,2,"c");
        dbHelper.insertPlayer("Nabneet",60,2,"c");
        dbHelper.insertPlayer("Sarah",50,2,"c");*/
        //test getPlayer
        /*Cursor cursor = dbHelper.getPlayer("Tony");
        if(cursor!=null){
            cursor.moveToFirst();
            do{
                String username = cursor.getString(cursor.getColumnIndex(DBHelper.USERNAME_COL));
                String score = cursor.getString(cursor.getColumnIndex(DBHelper.SCORE_COL));
                String level = cursor.getString(cursor.getColumnIndex(DBHelper.LEVEL_COL));
                String image = cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE_COL));
                String date = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_COL));
                Log.i("player",username+" "+score+" "+level+" "+image+" "+date);
            }while(cursor.moveToNext());
        }*/
        //test getAllPlayers
        Cursor cursor=dbHelper.getAllPlayers();
        if(cursor!=null){
            cursor.moveToFirst();
            do{
                String username = cursor.getString(cursor.getColumnIndex(DBHelper.USERNAME_COL));
                String score = cursor.getString(cursor.getColumnIndex(DBHelper.SCORE_COL));
                String level = cursor.getString(cursor.getColumnIndex(DBHelper.LEVEL_COL));
                String image = cursor.getString(cursor.getColumnIndex(DBHelper.IMAGE_COL));
                String date = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_COL));
                Log.i("player",username+" "+score+" "+level+" "+image+" "+date);
            }while(cursor.moveToNext());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu:
                Intent i = new Intent(this,ScoreActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}