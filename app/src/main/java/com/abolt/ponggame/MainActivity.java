package com.abolt.ponggame;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {
    private Button g_button;
    private Button r_button;
    private TextView g_text;
    private TextView g_mess;
    private TextView g_countdown;
    private TextView highscoretext;
    private int highscore;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final int Request_code_quiz = 1;
    public static final String KEY_HIGHSCORE = "keyHighscore";
    private MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        highscoretext = findViewById(R.id.text_score);
        Intent i = getIntent();
        loadHighscore();
        int get_score = i.getIntExtra("Score", -1);

        if (get_score > highscore) {
            update_highscore(get_score);
        }
        if(get_score >= 0) {
            Intent inten = new Intent(MainActivity.this, EndActivity.class);
            inten.putExtra("Score_end", get_score);
            startActivity(inten);
        }
        music = MediaPlayer.create(MainActivity.this,R.raw.freearcaderetro);
        music.setLooping(true);
        music.start();

        g_button = findViewById(R.id.g_start);
        r_button = findViewById(R.id.g_rules);
        g_mess = findViewById(R.id.g_message);
        g_text = findViewById(R.id.g_name);

        g_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music.isPlaying()) {
                    music.stop();
                }
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        r_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //music.stop();
                Intent intent = new Intent(MainActivity.this, RuleActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        music.start();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if(music.isPlaying())
            music.pause();
        else
            return;
    }
    private void loadHighscore () {
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highscore = prefs.getInt(KEY_HIGHSCORE, 0);
        highscoretext.setText("Highscore: " + highscore);
    }
    private void update_highscore ( int new_high){
        highscore = new_high;
        highscoretext.setText("Highscore: " + highscore);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE, highscore);
        editor.apply();
    }
}

