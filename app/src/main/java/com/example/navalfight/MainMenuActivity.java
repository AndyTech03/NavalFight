package com.example.navalfight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button new_game = findViewById(R.id.new_game_b);
        new_game.setOnClickListener(l -> {
            Intent intent = new Intent(MainMenuActivity.this, NewGameActivity.class);
            startActivity(intent);
        });
    }
}