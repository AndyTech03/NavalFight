package com.example.navalfight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String difficulty_title = extras.getString(NewGameActivity.DIFFICULTY_KEY);
            assert difficulty_title != null;
            AIDifficulty difficulty = AIDifficulty.fromTitle(difficulty_title);
            TextView ageView = findViewById(R.id.difficulty_text);
            ageView.setText(difficulty.toString());
        }

    }
}