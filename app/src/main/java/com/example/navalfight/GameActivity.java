package com.example.navalfight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.graphics.Point;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private GameManager game;
    private GameMapFragment playerGameMapFragment, computerGameMapFragment;
    private List<Shoot> playerShoots, computerShoots;
    private List<Ship> playerDestroyedShips, computerDestroyedShips;

    private List<Ship> playerShips, computerShips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        playerShoots = new ArrayList<>();
        computerShoots = new ArrayList<>();
        playerDestroyedShips = new ArrayList<>();
        computerDestroyedShips = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle extras = getIntent().getExtras();
        if (extras == null)
            throw new IllegalStateException("НЕ ПОЛУЧЕНЫ ДАННЫЕ!");

        int difficulty_id = extras.getInt(NewGameActivity.DIFFICULTY_KEY);
        AIDifficulty difficulty = AIDifficulty.fromID(difficulty_id);
        TextView ageView = findViewById(R.id.difficulty_text);
        ageView.setText(getString(difficulty.getID()));

        playerShips = extras.getParcelableArrayList(NewGameActivity.SHIPS_KEY);
        computerShips = new ArrayList<>(playerShips);

        game = new GameManager(playerShips, computerShips, difficulty);
        game.setOnComputerShootListener((shoot) -> {
            computerShoots.add(shoot);
            updatePlayerMap();
        });
        game.setOnComputerDestroyedListener((marginShoots, destroyed) -> {
            computerShoots.addAll(marginShoots);
            computerDestroyedShips.add(destroyed);
            updatePlayerMap();
        });

        game.setOnPlayerShootListener((shoot) -> {
            playerShoots.add(shoot);
            updateComputerMap();
        });
        game.setOnPlayerDestroyedListener((marginShoots, destroyed) -> {
            playerShoots.addAll(marginShoots);
            playerDestroyedShips.add(destroyed);
            updateComputerMap();
        });

        FragmentContainerView container = findViewById(R.id.player_map_container);
        playerGameMapFragment = container.getFragment();
        playerGameMapFragment.setOnCellClickedListener((point) -> {});

        container = findViewById(R.id.computer_map_container);
        computerGameMapFragment = container.getFragment();
        computerGameMapFragment.setOnCellClickedListener((point) -> {
            game.onPlayerTarget(point);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updatePlayerMap();
        updateComputerMap();
    }

    private void updatePlayerMap(){
        assert playerShips != null;
        List<Point> playerShipsDecks = new ArrayList<>();

        for (Ship ship : playerShips) {
            List<Point> decks = ship.getDecksLocations();
            playerShipsDecks.addAll(decks);
        }

        playerGameMapFragment.clear();
        playerGameMapFragment.drawShips(playerShipsDecks);
        playerGameMapFragment.drawShoots(computerShoots);
        playerGameMapFragment.drawDestroyed(computerDestroyedShips);
    }

    private void updateComputerMap(){
        assert computerShips != null;
        List<Point> computerShipsDecks = new ArrayList<>();

        for (Ship ship : computerShips) {
            List<Point> decks = ship.getDecksLocations();
            computerShipsDecks.addAll(decks);
        }

        computerGameMapFragment.clear();
        computerGameMapFragment.drawShips(computerShipsDecks);
        computerGameMapFragment.drawShoots(playerShoots);
        computerGameMapFragment.drawDestroyed(playerDestroyedShips);
    }
}