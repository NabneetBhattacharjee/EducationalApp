package com.example.nabneeteduapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    DBHelper dbHelper;
    private static final int NAME_LENGTH = 10;
    private static final int LEVEL_LENGTH = 7;
    private static final int SCORE_LENGTH = 8;
    private static final int DATE_LENGTH = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        dbHelper = new DBHelper(this);
        TextView headerRow = findViewById(R.id.headerRow);
        String tempStr = fixedLengthString("Name",NAME_LENGTH)
                + fixedLengthString("Level",LEVEL_LENGTH)
                + fixedLengthString("Score",SCORE_LENGTH)
                + fixedLengthString("Date",DATE_LENGTH);
        headerRow.setText(tempStr);
        ArrayAdapter<String> scoreAdapter = new ArrayAdapter<>(this,R.layout.item);
        ListView playerList = findViewById(R.id.playerList);
        playerList.setAdapter(scoreAdapter);

        Cursor cursor = dbHelper.getAllPlayers();
        if(cursor!=null){
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    String nameStr = cursor.getString(cursor.getColumnIndex(DBHelper.USERNAME_COL));
                    nameStr = nameStr.trim();
                    nameStr = fixedLengthString(nameStr,NAME_LENGTH);
                    String levelStr = cursor.getString(cursor.getColumnIndex(DBHelper.LEVEL_COL));
                    levelStr = levelStr.trim();
                    levelStr = fixedLengthString(levelStr,LEVEL_LENGTH);
                    String scoreStr = cursor.getString(cursor.getColumnIndex(DBHelper.SCORE_COL));
                    scoreStr = scoreStr.trim();
                    scoreStr = fixedLengthString(scoreStr,SCORE_LENGTH);
                    String dateStr = cursor.getString(cursor.getColumnIndex(DBHelper.DATE_COL));
                    dateStr = dateStr.trim();
                    dateStr = fixedLengthString(dateStr,DATE_LENGTH);

                    scoreAdapter.add(nameStr + levelStr + scoreStr + dateStr);
                }while(cursor.moveToNext());
            }
        }
   }

    private String fixedLengthString(String string, int length) {
        return String.format("%1$-" + length + "s", string);
    }

}