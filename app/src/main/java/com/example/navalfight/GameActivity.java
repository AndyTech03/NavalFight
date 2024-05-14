package com.example.navalfight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private GameMapFragment gameMapFragment;
    List<Ship> ships;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FragmentContainerView container = findViewById(R.id.map_container);
        gameMapFragment = container.getFragment();
        gameMapFragment.setOnCellClickedListener((point) -> {});

        Log.i("NAVAL_LOG_I", gameMapFragment.toString());

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            int difficulty_id = extras.getInt(NewGameActivity.DIFFICULTY_KEY);
            AIDifficulty difficulty = AIDifficulty.fromID(difficulty_id);
            TextView ageView = findViewById(R.id.difficulty_text);
            ageView.setText(getString(difficulty.getID()));

            ships =  extras.getParcelableArrayList(NewGameActivity.SHIPS_KEY);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        assert ships != null;
        Log.i("NAVAL_LOG_I", ships.toString());

        List<Point> otherShipsDecks = new ArrayList<>();

        for (Ship ship : ships) {
            List<Point> decks = Arrays.asList(ship.getDecksLocations());
            otherShipsDecks.addAll(decks);
        }
        gameMapFragment.clear();
        gameMapFragment.drawShips(otherShipsDecks);
    }
}