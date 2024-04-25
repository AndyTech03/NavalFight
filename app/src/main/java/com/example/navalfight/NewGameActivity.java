package com.example.navalfight;

import androidx.annotation.StringRes;
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

    private final static AIDifficulty[] DIFFICULTY_ARRAY = {
            AIDifficulty.Ease,
            AIDifficulty.Normal,
            AIDifficulty.Master,
            AIDifficulty.Cheating
    };

    private final static ShipType[] SHIP_TYPES = {
            ShipType.Boat,
            ShipType.Frigate,
            ShipType.Cruiser,
            ShipType.Battleship
    };
    private @StringRes int difficultyID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        initDifficultySpinner();
        initShipsSpinner();

        Button new_game = findViewById(R.id.start_b);
        new_game.setOnClickListener(l -> {
            Intent intent = new Intent(NewGameActivity.this, GameActivity.class);
            intent.putExtra(DIFFICULTY_KEY, difficultyID);
            startActivity(intent);
        });
    }

    private void initDifficultySpinner() {
        final String[] difficultyTitleArray = new String[] {
                getString(AIDifficulty.Ease.getID()),
                getString(AIDifficulty.Normal.getID()),
                getString(AIDifficulty.Master.getID()),
                getString(AIDifficulty.Cheating.getID())
        };

        Spinner spinner = findViewById(R.id.ai_difficulty);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, difficultyTitleArray);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(1);
        AdapterView.OnItemSelectedListener itemSelectedListener =
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        difficultyID = DIFFICULTY_ARRAY[position].getID();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private void initShipsSpinner() {
        final int count = SHIP_TYPES.length;
        final String[] shipsTitleArray = new String[count];
        for (int i = 0; i < count; i++){
            shipsTitleArray[i] = getString(SHIP_TYPES[i].getID());
        }

        Spinner spinner = findViewById(R.id.ship_sellector);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, shipsTitleArray);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        AdapterView.OnItemSelectedListener itemSelectedListener =
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }
}