package com.example.navalfight;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameManager {

    public interface OnShootListener {
        void onEvent(Shoot shoot);
    }

    public interface OnDestroyListener {
        void onEvent(List<Shoot> marginShoots, Ship destroyed);
    }

    private final ComputerAI ai;

    OnShootListener playerShootListener;
    OnShootListener computerShootListener;
    OnDestroyListener playerDestroyedListener;
    OnDestroyListener computerDestroyedListener;

    private final List<Ship> playerShips, computerShips;
    private final List<Shoot> playerShoots, computerShoots;

    public GameManager(List<Ship> playerShips, List<Ship> computerShip, AIDifficulty difficulty) {
        this.playerShips = playerShips;
        this.computerShips = computerShip;
        playerShoots = new ArrayList<>();
        computerShoots = new ArrayList<>();
        ai = new ComputerAI(difficulty);
    }

    public void setOnPlayerShootListener(OnShootListener listener) {
        playerShootListener = listener;
    }

    public void setOnComputerShootListener(OnShootListener listener) {
        computerShootListener = listener;
    }

    public void setOnPlayerDestroyedListener(OnDestroyListener listener) {
        playerDestroyedListener = listener;
    }

    public void setOnComputerDestroyedListener(OnDestroyListener listener) {
        computerDestroyedListener = listener;
    }

    private boolean isComputerTurn = false;

    public void onPlayerTarget(Point target) {
        if (isComputerTurn)
            return;

        isComputerTurn = true;
        boolean shootAgain = onShoot(false, target);
        if (shootAgain) {
            isComputerTurn = false;
        } else {
            computerTurn();
        }
    }

    public void computerTurn() {
        boolean shootAgain;
        do {
            Point target = ai.getTarget();
            shootAgain = onShoot(true, target);
        } while (shootAgain);

        isComputerTurn = false;
    }

    private boolean onShoot(boolean computerShoot, Point target) {
        List<Ship> ships = computerShoot ? playerShips : computerShips;
        OnShootListener shootListener = computerShoot ? computerShootListener : playerShootListener;
        OnDestroyListener destroyListener = computerShoot ? computerDestroyedListener : playerDestroyedListener;
        List<Shoot> shoots = computerShoot ? computerShoots : playerShoots;
        Shoot.Result result = Shoot.Result.Miss;

        Log.i("NAVAL_LOG_I", (computerShoot ? "Компьютер выстрелил: " : "Игрок выстрелил: ") + target);

        if (shoots.stream().anyMatch(s -> s.getTarget() == target))
            result = Shoot.Result.Invalid;

        else
            for (Ship ship : ships) {
                List<Point> decks = ship.getDecksLocations();
                if (decks.contains(target)) {
                    if (decks.stream().allMatch(d -> d.equals(target) ||
                            shoots.stream().anyMatch(s -> d.equals(s.getTarget()))))
                    {
                        List<Shoot> marginShoots = NewGameActivity.addMarginPoints(ship).stream()
                                .filter(p -> !p.equals(target) &&
                                        shoots.stream().noneMatch(s -> p.equals(s.getTarget())))
                                .map(p -> new Shoot(p, Shoot.Result.Miss))
                                .collect(Collectors.toList());
                        shoots.addAll(marginShoots);
                        shoots.add(new Shoot(target, Shoot.Result.Hit));
                        if (computerShoot)
                            ai.onDestroy(marginShoots, ship);
                        destroyListener.onEvent(marginShoots, ship);
                        onDestroy();

                        Log.i("NAVAL_LOG_I", (computerShoot ? "Компьютер потопил: " : "Игрок потопил: ") + ship);
                        return true;
                    }
                    result = Shoot.Result.Hit;
                    Log.i("NAVAL_LOG_I", (computerShoot ? "Компьютер попал" : "Игрок попал"));
                    break;
                }
            }

        Shoot shoot = new Shoot(target, result);
        shoots.add(shoot);
        if (computerShoot)
            ai.onShoot(shoot);
        shootListener.onEvent(shoot);
        return result != Shoot.Result.Miss;
    }

    public void onDestroy(){
    }
}
