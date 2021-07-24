package com.abolt.ponggame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {
    private Button home;
    private Button play;
    private TextView score_e;
    private MediaPlayer over;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        Intent f = getIntent();
        over = MediaPlayer.create(EndActivity.this, R.raw.gameover);
        over.start();
        int score_end = f.getIntExtra("Score_end", 0);
        home = findViewById(R.id.homepage);
        score_e = findViewById(R.id.g_score);
        play = findViewById(R.id.playAgain);
        score_e.setText("Score: "+score_end);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e = new Intent(EndActivity.this, MainActivity.class);
                startActivity(e);
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(over.isPlaying()){
                    over.stop();
                }
                Intent e = new Intent(EndActivity.this, GameActivity.class);
                startActivity(e);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        over.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(over.isPlaying())
            over.pause();
        else
            return;
    }
}