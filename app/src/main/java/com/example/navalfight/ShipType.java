package com.example.navalfight;

import androidx.annotation.StringRes;

public enum ShipType {
    Boat(R.string.boat_label, 1),
    Frigate(R.string.frigate_label, 2),
    Cruiser(R.string.cruiser_label, 3),
    Battleship(R.string.battleship_label, 4);

    private final int id;
    private final int length;

    ShipType(@StringRes int id, int length) {
        this.id = id;
        this.length = length;
    }

    public static ShipType fromInt(int i) {
        switch (i){
            case 0:
                return Boat;
            case 1:
                return Frigate;
            case 2:
                return Cruiser;
            case 3:
                return Battleship;
        }
        throw new IllegalStateException("Неверный номер: " + i);
    }

    public int getLength() { return length; }
    public int getID() {
        return id;
    }

    public int toInt() {
        return length - 1;
    }
}
