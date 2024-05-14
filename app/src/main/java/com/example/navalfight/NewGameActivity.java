package com.example.navalfight;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class NewGameActivity extends AppCompatActivity {

    public final static String DIFFICULTY_KEY = "DIFFICULTY";
    public final static String SHIPS_KEY = "SHIPS";

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

    private final static Integer[] SHIP_COUNTS = {
            4,
            3,
            2,
            1
    };
    private @StringRes int difficultyID;

    private GameMapFragment gameMapFragment;
    private Ship placeableShip;
    private final List<Ship> placedShips = new ArrayList<>();

    private Spinner shipTypeSpinner;
    private ArrayAdapter<String> shipTypeSpinnerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        initDifficultySpinner();
        initShipsSpinner();
        initMap();

        Button new_game = findViewById(R.id.start_b);
        new_game.setOnClickListener(l -> {
            /*
            if (Arrays.stream(SHIP_COUNTS).anyMatch(c -> c > 0))
                return;
             */
            Intent intent = new Intent(NewGameActivity.this, GameActivity.class);
            intent.putExtra(DIFFICULTY_KEY, difficultyID);
            Log.i("NAVAL_LOG_I", Arrays.toString(placedShips.toArray()));
            intent.putParcelableArrayListExtra(SHIPS_KEY, (ArrayList<? extends Parcelable>) placedShips);
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

    private List<String> getShipsSpinnerTitles(){
        List<String> shipsTitleArray = new ArrayList<>();
        for (int i = 0; i < SHIP_COUNTS.length; i++) {
            int shipsCount = SHIP_COUNTS[i];
            if (shipsCount < 1)
                shipsCount = 0;
            shipsTitleArray.add(getString(SHIP_TYPES[i].getID()) + " : " + shipsCount);
        }
        return shipsTitleArray;
    }

    private void initShipsSpinner() {

        Spinner spinner = findViewById(R.id.ship_sellector);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getShipsSpinnerTitles());
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
        shipTypeSpinner = spinner;
        shipTypeSpinnerAdapter = adapter;

        Button place_ship = findViewById(R.id.place_ship_button);
        place_ship.setOnClickListener(l -> {
            int i = spinner.getSelectedItemPosition();
            if (SHIP_COUNTS[i] < 1){
                return;
            }


            Point[] placeableShipDecks = placeableShip.getDecksLocations();
            Set<Point> marginPoints = getAllMarginPoints();
            for (Point deck : placeableShipDecks) {
                if (marginPoints.contains(deck))
                    return;
            }

            placedShips.add(placeableShip.clone());
            SHIP_COUNTS[i]--;
            if (SHIP_COUNTS[i] < 1)
                placeableShip = null;
            updateShipsSpinner();
            drawShips();
        });
    }

    private void updateShipsSpinner(){
        int i = shipTypeSpinner.getSelectedItemPosition();
        shipTypeSpinnerAdapter.clear();
        shipTypeSpinnerAdapter.addAll(getShipsSpinnerTitles());
        shipTypeSpinnerAdapter.notifyDataSetChanged();
        shipTypeSpinner.setSelection(i);
    }

    private void initMap() {
        FragmentContainerView container = findViewById(R.id.map_container);
        gameMapFragment = container.getFragment();
        gameMapFragment.setOnCellClickedListener((point) -> {
            if (placeableShip != null) {
                placeableShip.setLocation(point);
                drawShips();
            } else {
                Ship selectedShip = null;
                for (Ship ship : placedShips) {
                    if (Arrays.asList(ship.getDecksLocations()).contains(point)) {
                        selectedShip = ship;
                        break;
                    }
                }
                if (selectedShip != null) {
                    int index = selectedShip.getTypeIndex();
                    placedShips.remove(selectedShip);
                    SHIP_COUNTS[index]++;
                    shipTypeSpinner.setSelection(index);
                    placeableShip = selectedShip;
                    updateShipsSpinner();
                    drawShips();
                }
            }
        });
    }

    private void drawShips() {
        List<Point> otherShipsDecks = new ArrayList<>();
        List<Point> allowedDecks = new ArrayList<>();
        List<Point> notAllowedDecks = new ArrayList<>();

        for (Ship ship : placedShips) {
            List<Point> decks = Arrays.asList(ship.getDecksLocations());
            otherShipsDecks.addAll(decks);
        }
        if (placeableShip != null){
            Point[] placeableShipDecks = placeableShip.getDecksLocations();
            Set<Point> marginPoints = getAllMarginPoints();

            for (Point deck : placeableShipDecks){
                if (marginPoints.contains(deck))
                    notAllowedDecks.add(deck);
                else
                    allowedDecks.add(deck);
            }
        }
        gameMapFragment.clear();
        gameMapFragment.drawShips(otherShipsDecks);
        gameMapFragment.drawPlaceableShip(allowedDecks, notAllowedDecks);
    }

    private Set<Point> getAllMarginPoints(){
        Set<Point> marginPoints = new HashSet<>();

        for (Ship ship : placedShips){
            List<Point> decks = Arrays.asList(ship.getDecksLocations());
            Optional<Point> optionalPoint;
            int min_x = 0, max_x = 0;
            optionalPoint = decks.stream().min(Comparator.comparingInt(p -> p.x));
            if (optionalPoint.isPresent())
                min_x = optionalPoint.get().x;
            optionalPoint = decks.stream().max(Comparator.comparingInt(p -> p.x));
            if (optionalPoint.isPresent())
                max_x = optionalPoint.get().x;

            int min_y = 0, max_y = 0;
            optionalPoint = decks.stream().min(Comparator.comparingInt(p -> p.y));
            if (optionalPoint.isPresent())
                min_y = optionalPoint.get().y;
            optionalPoint = decks.stream().max(Comparator.comparingInt(p -> p.y));
            if (optionalPoint.isPresent())
                max_y = optionalPoint.get().y;

            for (int x = min_x - 1; x <= max_x + 1; x++){
                for (int y = min_y - 1; y <= max_y + 1; y++)
                    marginPoints.add(new Point(x, y));
            }
        }
        return marginPoints;
    }
}