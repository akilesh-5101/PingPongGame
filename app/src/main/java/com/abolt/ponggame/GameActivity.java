package com.abolt.ponggame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.abolt.ponggame.views.CustomView;

import static java.lang.Thread.sleep;

public class GameActivity extends AppCompatActivity implements CustomView.GameButtonClickListener {
    public int t;
    public static final String Score = "Score";
    public Button power;
    public Button nothanks;
    public MediaPlayer music_empire;

    @Override
    public void OnGamePower_Finish(int score, int con) {
        if(score >= 0) {
            if(music_empire.isPlaying()){
                music_empire.stop();
            }
            t = score;
            Intent i = new Intent(GameActivity.this, MainActivity.class);
            i.putExtra(Score, t);
            startActivity(i);
        }
        if(con == 1){
            power.setVisibility(View.VISIBLE);
            nothanks.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        t = 0;
        Intent in = getIntent();
        power = findViewById(R.id.g_powerup);
        nothanks = findViewById(R.id.g_no_need);
        CustomView game_view = (CustomView) findViewById(R.id.game);
        game_view.setGameButtonListener((CustomView.GameButtonClickListener) GameActivity.this);
        music_empire = MediaPlayer.create(GameActivity.this,R.raw.empireofangels);
        music_empire.setLooping(true);
        music_empire.start();
        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game_view.slider_l += game_view.slider_l/13;
                game_view.slider_paint.setColor(Color.RED);
                game_view.slider.right = game_view.slider.left + game_view.slider_l;
                power.setVisibility(View.GONE);
                nothanks.setVisibility(View.GONE);
            }
        });
        nothanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                power.setVisibility(View.GONE);
                nothanks.setVisibility(View.GONE);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        music_empire.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(music_empire.isPlaying())
            music_empire.pause();
        else
            return;
    }
}

