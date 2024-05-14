package com.example.navalfight;

import android.graphics.Point;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerAI {
    private static final Random RANDOM = new Random();
    private List<Shoot> history;
    private List<Ship> destroyedShips;
    private final AIDifficulty difficulty;

    public ComputerAI(AIDifficulty difficulty){
        history = new ArrayList<>();
        destroyedShips = new ArrayList<>();
        this.difficulty = difficulty;
    }

    public Point getTarget(){
        int x = RANDOM.nextInt(GameMapFragment.MAP_SIZE.getWidth());
        int y = RANDOM.nextInt(GameMapFragment.MAP_SIZE.getHeight());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
        return new Point(x, y);
    }

    public void onShoot(Shoot shoot){
        history.add(shoot);
    }

    public void onDestroy(List<Shoot> marginShoots, Ship destroyed) {
        history.addAll(marginShoots);
        destroyedShips.add(destroyed);
    }
}
