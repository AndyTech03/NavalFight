package com.example.navalfight;

import androidx.annotation.StringRes;

public enum ShipType {
    Boat(R.string.boat_label),
    Frigate(R.string.frigate_label),
    Cruiser(R.string.cruiser_label),
    Battleship(R.string.battleship_label);

    private final int id;

    ShipType(@StringRes int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
