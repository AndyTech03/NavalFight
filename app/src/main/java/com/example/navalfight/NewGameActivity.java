package com.example.navalfight;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    GameMapFragment gameMapFragment;
    private Ship placeableShip;
    private final List<Ship> placedShips = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        initDifficultySpinner();
        initShipsSpinner();
        initMap();

        Button new_game = findViewById(R.id.start_b);
        new_game.setOnClickListener(l -> {
            Intent intent = new Intent(NewGameActivity.this, GameActivity.class);
            intent.putExtra(DIFFICULTY_KEY, difficultyID);
            startActivity(intent);
        });

        findViewById(R.id.roll_right_b).setOnClickListener(l ->{
            placeableShip.rotateForward();
            drawShips();
        });

        findViewById(R.id.roll_left_b).setOnClickListener(l ->{
            placeableShip.rotateBackward();
            drawShips();
        });

        findViewById(R.id.up_b).setOnClickListener(l -> {
            placeableShip.move(Direction.North);
            drawShips();
        });
        findViewById(R.id.right_b).setOnClickListener(l -> {
            placeableShip.move(Direction.East);
            drawShips();
        });
        findViewById(R.id.down_b).setOnClickListener(l -> {
            placeableShip.move(Direction.South);
            drawShips();
        });
        findViewById(R.id.left_b).setOnClickListener(l -> {
            placeableShip.move(Direction.West);
            drawShips();
        });
    }

    private void initDifficultySpinner() {
        final String[] difficultyTitleArray = new String[]{
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
        for (int i = 0; i < count; i++) {
            shipsTitleArray[i] = getString(SHIP_TYPES[i].getID());
        }

        Spinner spinner = findViewById(R.id.ship_sellector);
        Button place_ship = findViewById(R.id.place_ship_button);
        place_ship.setOnClickListener(l -> {
            placedShips.add(placeableShip.clone());
            spinner.setSelection(spinner.getSelectedItemPosition());
            drawShips();
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, shipsTitleArray);
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        AdapterView.OnItemSelectedListener itemSelectedListener =
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        Point old_location = null;
                        if (placeableShip != null){
                            old_location = placeableShip.getLocation();
                        }
                        placeableShip = null;
                        placeableShip = new Ship(SHIP_TYPES[position]);
                        if (old_location != null)
                            placeableShip.setLocation(old_location);
                        drawShips();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
        spinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private void initMap() {
        FragmentContainerView container = findViewById(R.id.map_container);
        gameMapFragment = container.getFragment();
        gameMapFragment.setOnCellClickedListener((point) -> {
            if (placeableShip != null) {
                placeableShip.setLocation(point);
                drawShips();
            }
        });
    }

    private void drawShips() {
        Point[] placeableShipDecks = placeableShip.getDecksLocations();
        List<Point> otherShipsDecks = new ArrayList<>();
        List<Point> allowedDecks = new ArrayList<>();
        List<Point> notAllowedDecks = new ArrayList<>();

        for (Ship ship : placedShips){
            otherShipsDecks.addAll(Arrays.asList(ship.getDecksLocations()));
        }
        for (Point deck : placeableShipDecks){
            if (otherShipsDecks.contains(deck))
                notAllowedDecks.add(deck);
            else
                allowedDecks.add(deck);
        }
        gameMapFragment.clear();
        gameMapFragment.drawShips(otherShipsDecks);
        gameMapFragment.drawPlaceableShip(allowedDecks, notAllowedDecks);
    }
}