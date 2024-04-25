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
            int difficulty_id = extras.getInt(NewGameActivity.DIFFICULTY_KEY);
            AIDifficulty difficulty = AIDifficulty.fromID(difficulty_id);
            TextView ageView = findViewById(R.id.difficulty_text);
            ageView.setText(getString(difficulty.getID()));
        }

    }
}