package com.example.nabneeteduapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private String imageName;
    private DBHelper dbHelper;
    private SoundManager soundManager;
    private int completedSoundID;
    private int level;

    private int no_of_pieces;
    private final String BUTTON_NAME_PREFIX = "btn";
    int time_to_solve_puzzle = -1;

    //an array of buttons
    Button[] btn;

    //correct sequence of IDs of buttons
    int[] correct_id_seq;

    /*this array is the working array.
      Its element's values are similar to correct_id_seq[] except diff locations*/
    int[] rand_id_seq;

    //array to keep 2 indexes of 2 elements in the array rand_id_seq from 2 clicks
    int two_indexes_to_swap_img[] = {-1, -1};

    int num_of_clicks = 0; //need 2 clicks to swap images

    Button two_buttons_to_swap[] = {null, null};

    TextView timeTextView;

    Timer T;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Puzzle Game");
        //using intent to get imageName and level by getStringExtra, getIntExtra
        Intent intent = getIntent();
        if(intent != null){
            //get level
            level = intent.getIntExtra("level",1);
            //get image name
            imageName = intent.getStringExtra("imageName");
        }
        if(level == 3 ){
            setContentView(R.layout.activity_game_level3);
            no_of_pieces = 16;
        }
        else if (level == 2){
            setContentView(R.layout.activity_game_level2);
            no_of_pieces = 9;
        }
        else if(level == 1){
            setContentView(R.layout.activity_game_level1);
            no_of_pieces = 4;
        }
        btn = new Button[no_of_pieces];
        correct_id_seq = new int[no_of_pieces];
        rand_id_seq = new int[no_of_pieces];

        dbHelper = new DBHelper(this);
        soundManager = new SoundManager(this);
        completedSoundID = soundManager.addSound(R.raw.level_completed);
        timeTextView = findViewById(R.id.timeTextView);


        for(int i = 0; i < no_of_pieces; i++){
            btn[i] = (Button) findViewById(this.getResources().getIdentifier(
                    BUTTON_NAME_PREFIX + Integer.toString(i),"id", this.getPackageName()));
        }
        play_game(10,imageName);

    }
    public void play_game(int perusal_time_by_seconds, String imageName) {

        //set the values for the correct_id_seq array
        for(int i = 0; i < no_of_pieces; i++){
            correct_id_seq[i] = this.getResources().getIdentifier(imageName
                    + Integer.toString(i),"drawable", this.getPackageName());
        }

        // based on the values of correct_id_seq, set the button background
        for(int i = 0; i < no_of_pieces; i++){
            btn[i].setBackgroundResource(correct_id_seq[i]);
        }

        for(int i = 0; i < no_of_pieces; i++){
            btn[i].setClickable(false);
        }

        //display image in an amount of perusal_time_by_seconds
        new CountDownTimer(perusal_time_by_seconds * 1000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timeTextView.setText("TIME: " + Long.toString(millisUntilFinished/1000));
            }
            @Override
            public void onFinish() {

                for(int i = 0; i < no_of_pieces; i++){
                    btn[i].setClickable(true);
                }
                make_puzzle_with_time_tick();
            }
        }.start();
    }
    public void make_puzzle_with_time_tick(){
        //construct rand_id_seq array, firstly, start with the correct sequence of ids
        rand_id_seq = Arrays.copyOf(correct_id_seq, correct_id_seq.length);

        //and then call the function rand_arr_elements to randomly swap elements
        //call 2 times for better results
        rand_arr_elements(rand_id_seq); rand_arr_elements(rand_id_seq);

        // based on the values of rand_id_seq, set the buttons' background
        for(int i = 0; i < no_of_pieces; i++){
            btn[i].setBackgroundResource(rand_id_seq[i]);
        }

        //counting time by seconds
        time_to_solve_puzzle = -1;
        T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time_to_solve_puzzle++;
                        timeTextView.setText("TIME: " + time_to_solve_puzzle);
                    }
                });
            }
        }, 1000, 1000);
    }

    public void on_click_image(View v){
        Button button = (Button)v;

        //value of temp similar to com.jc165984.puzzle:id/btn0
        String temp_str = button.getResources().getResourceName(button.getId());

        // "id/" + BUTTON_NAME_PREFIX = "id/btn"
        int id_pos = temp_str.indexOf("id/" + BUTTON_NAME_PREFIX);

        //get the button's index.For example, id/btn0 has index 0
        int index = Integer.parseInt(temp_str.substring(id_pos +
                ("id/" + BUTTON_NAME_PREFIX).length()));

        two_indexes_to_swap_img[num_of_clicks] = index;
        two_buttons_to_swap[num_of_clicks] = button;

        if (num_of_clicks == 1) {
            //2 clicks already - swap images now
            two_buttons_to_swap[0].setBackgroundResource(rand_id_seq[two_indexes_to_swap_img[1]]);
            two_buttons_to_swap[1].setBackgroundResource(rand_id_seq[two_indexes_to_swap_img[0]]);
            //update the rand_id_seq array
            int temp = rand_id_seq[two_indexes_to_swap_img[0]];
            rand_id_seq[two_indexes_to_swap_img[0]] = rand_id_seq[two_indexes_to_swap_img[1]];
            rand_id_seq[two_indexes_to_swap_img[1]] = temp;

            //check if the 2 array rand_id_seq and correct_id_seq are equal
            //if it is then the user wins
            if (Arrays.equals(correct_id_seq, rand_id_seq)) {
                if (T != null)
                    T.cancel();
                Log.i("Time = ", Integer.toString(time_to_solve_puzzle));
                for(int i = 0; i < no_of_pieces; i++){
                    btn[i].setClickable(false);
                }
                //save record
                SharedPreferences sharedPreferences = getSharedPreferences("nabneeteduapp_items",MODE_PRIVATE);
                String username = sharedPreferences.getString("username","");

                //play sound
                soundManager.play(completedSoundID, 0.5f);
                
                //save record in db
                dbHelper.insertPlayer(username,time_to_solve_puzzle,level,imageName);

                Intent intent = new Intent(this,ResultActivity.class);
                intent.putExtra("userName",username);
                intent.putExtra("time",time_to_solve_puzzle);
                startActivity(intent);
            }
        }

        num_of_clicks++;
        if (num_of_clicks == 2)
            num_of_clicks = 0;

    }

    public void rand_arr_elements(int[] arr) {
        Random random = new Random();
        int temp_index;
        int temp_obj;
        for (int i = 0; i < arr.length - 1; i++) {
            //a random number between i+1 and arr.length - 1
            temp_index = i + 1 + random.nextInt(arr.length - 1 - i);
            //swap the element at i with the element at temp_index
            temp_obj = arr[i];
            arr[i] = arr[temp_index];
            arr[temp_index] = temp_obj;
        }
    }


}