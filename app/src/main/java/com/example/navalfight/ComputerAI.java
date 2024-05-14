package com.example.navalfight;

import android.graphics.Point;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComputerAI {
    private static final Random RANDOM = new Random();
    private List<Shoot> history;
    private List<Ship> destroyedShips;
    private final AIDifficulty difficulty;

    private final static int SIZE_X = GameMapFragment.MAP_SIZE.getWidth();
    private final static int SIZE_Y = GameMapFragment.MAP_SIZE.getHeight();

    private Hashtable<Point, Integer> masterMatrix;

    public ComputerAI(AIDifficulty difficulty) {
        history = new ArrayList<>();
        destroyedShips = new ArrayList<>();
        this.difficulty = difficulty;
    }

    private Point easeTarget() {
        int x = RANDOM.nextInt(SIZE_X);
        int y = RANDOM.nextInt(SIZE_Y);
        return new Point(x, y);
    }

    private Point masterTarget() {
        masterMatrix = new Hashtable<>();
        for (int y = 0; y < SIZE_Y; y++) {
            for (int x = 0; x < SIZE_X; x++) {
                masterMatrix.put(new Point(x, y), 0);
            }
        }
        List<Point> notAllowedPoints = new ArrayList<>();
        for (Shoot shoot : history) {
            if (shoot.getResult() == Shoot.Result.Hit &&
                    destroyedShips.stream().noneMatch(s -> s.getDecksLocations().stream()
                            .anyMatch(d -> shoot.getTarget().equals(d))))
                continue;
            notAllowedPoints.add(shoot.getTarget());
        }
        CountDownLatch latch = new CountDownLatch(4);
        for (int length = 1; length <= 4; length++) {
            final int finalLength = length;
            final int destroyed = (int) destroyedShips.stream()
                    .filter(s -> s.getLength() == finalLength).count();
            Thread thread = new Thread(() -> {
                final int count = 5 - finalLength - destroyed;
                if (count <= 0) {
                    latch.countDown();
                    return;
                }
                Log.i("NAVAL_LOG_I", "Len: " + finalLength + ", count: " + count);

                Hashtable<Point, Integer> matrix = new Hashtable<>();
                for (int y = 0; y < SIZE_Y; y++) {
                    for (int x = 0; x < SIZE_X; x++) {
                        matrix.put(new Point(x, y), 0);
                    }
                }
                final List<Point> finalNotAllowedPoints = notAllowedPoints;
                final Ship ship = new Ship(ShipType.fromInt(finalLength - 1));
                for (int x = 0; x < SIZE_X; x++)
                    for (int y = 0; y < SIZE_Y; y++)
                        for (int dir = 0; dir < (finalLength == 1 ? 1 : 4); dir++) {
                            Direction direction = Direction.fromInt(dir);
                            Point location = new Point(x, y);
                            if (!ship.locationIsValid(location, direction))
                                continue;
                            ship.setAll(location, direction);
                            List<Point> decks = ship.getDecksLocations();
                            if (decks.stream().anyMatch(d ->
                                    finalNotAllowedPoints.stream().anyMatch(d::equals)))
                                continue;
                            int extra = (int) decks.stream().filter(d -> history.stream()
                                    .anyMatch(s -> d.equals(s.getTarget())))
                                    .count() * 5;
                            for (Point point : decks) {
                                if (history.stream().noneMatch(s -> point.equals(s.getTarget())))
                                    matrix.replace(point, matrix.get(point) + count * (extra + 1));
                            }
                        }

                onMasterThreadFinish(matrix);
                latch.countDown();
            });
            thread.start();
        }
        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }
        int max;
        Optional<Integer> optional = masterMatrix.values().stream().max(Integer::compareTo);
        if (optional.isPresent())
            max = optional.get();
        else {
            max = 0;
        }
        List<Point> points = masterMatrix.keySet().stream().filter(p -> masterMatrix.get(p) == max)
                .collect(Collectors.toList());
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < SIZE_Y; y++){
            for (int x = 0; x < SIZE_X; x++){
                builder.append(masterMatrix.get(new Point(x, y)));
                builder.append("\t");
            }
            builder.append("\n");
        }
        builder.append("\nmax: ").append(max);
        Log.i("NAVAL_LOG_I", builder.toString());
        return points.get(RANDOM.nextInt(points.size()));
    }

    private final Lock lock = new ReentrantLock();

    private void onMasterThreadFinish(Dictionary<Point, Integer> result) {
        lock.lock();
        for (int y = 0; y < SIZE_Y; y++)
            for (int x = 0; x < SIZE_X; x++) {
                Point point = new Point(x, y);
                Integer oldValue = masterMatrix.get(point);
                masterMatrix.replace(point, oldValue + result.get(point));
            }
        lock.unlock();
    }

    public Point getTarget() {
        switch (difficulty) {
            case Ease:
                return easeTarget();
            case Normal:
                return easeTarget();
            case Master:
                return masterTarget();
            case Cheating:
                return easeTarget();
        }
        throw new IllegalStateException("Неизвесная сложность ИИ!");
    }

    public void onShoot(Shoot shoot) {
        history.add(shoot);
    }

    public void onDestroy(List<Shoot> marginShoots, Ship destroyed) {
        history.addAll(marginShoots);
        destroyedShips.add(destroyed);
    }
}
