package com.example.navalfight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class NewGameActivity extends AppCompatActivity {

    public final static String DIFFICULTY_KEY = "DIFFICULTY";

    private final static String[] difficultyArray = {"Простой", "Лёгкий", "Сложный", "Мастер", "Нечестный"};
    private String difficulty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        Button new_game = findViewById(R.id.start_b);
        new_game.setOnClickListener(l -> {
            Intent intent = new Intent(NewGameActivity.this, GameActivity.class);
            intent.putExtra(DIFFICULTY_KEY, difficulty);
            startActivity(intent);
        });
        Spinner spinner = findViewById(R.id.ai_difficulty);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, difficultyArray);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(2);
        AdapterView.OnItemSelectedListener itemSelectedListener =
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        // Получаем выбранный объект
                        difficulty = (String) parent.getItemAtPosition(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }
}