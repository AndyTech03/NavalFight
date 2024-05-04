package com.example.navalfight;

import android.graphics.Point;

import androidx.annotation.NonNull;

public class Ship implements Cloneable{
    private final ShipType type;
    private Point location;
    private Direction direction;

    public Ship(ShipType type) {
        this.type = type;
        location = new Point(0, 0);
        direction = Direction.South;
    }

    public void rotateForward() {
        if (locationIsValid(location, direction.next()))
            direction = direction.next();
    }

    public void rotateBackward() {
        if (locationIsValid(location, direction.previous()))
            direction = direction.previous();
    }

    public void setLocation(Point location) {

        if (locationIsValid(location, direction))
            this.location = location;
    }

    public boolean locationIsValid(Point location, Direction direction){
        int x_max, x_min;
        x_max = x_min = location.x;
        int y_max, y_min;
        y_max = y_min = location.y;
        switch (direction) {
            case North:
                y_min -= type.getLength() - 1;
                break;
            case East:
                x_max += type.getLength() - 1;
                break;
            case South:
                y_max += type.getLength() - 1;
                break;
            case West:
                x_min -= type.getLength() - 1;
                break;
        }

        return x_min >= 0 && y_min >= 0
                && x_max < GameMapFragment.MAP_SIZE.getWidth()
                && y_max < GameMapFragment.MAP_SIZE.getHeight();
    }

    public void move(Direction direction) {
        int x_max, x_min, x;
        x = x_max = x_min = location.x;
        int y_max, y_min, y;
        y = y_max = y_min = location.y;
        switch (direction) {
            case North:
                y = y_max = y_min = y - 1;
                break;

            case East:
                x = x_max = x_min = x + 1;
                break;

            case South:
                y = y_max = y_min = y + 1;
                break;

            case West:
                x = x_max = x_min = x - 1;
                break;
        }

        switch (this.direction){
            case North:
                y_min -= type.getLength() - 1;
                break;

            case East:
                x_max += type.getLength() - 1;
                break;

            case South:
                y_max += type.getLength() - 1;
                break;

            case West:
                x_min -= type.getLength() - 1;
                break;
        }

        if (x_min < 0 || y_min < 0
                || x_max >= GameMapFragment.MAP_SIZE.getWidth()
                || y_max >= GameMapFragment.MAP_SIZE.getHeight())
            return;

        this.location = new Point(x, y);
    }

    public Point getLocation() {
        return new Point(location);
    }

    public Point[] getDecksLocations(){
        int x = location.x;
        int y = location.y;
        Point[] result = new Point[type.getLength()];

        for (int i = 0; i < type.getLength(); i++){
            result[i] = new Point(x, y);
            switch (direction){
                case North:
                    y--;
                    break;
                case East:
                    x++;
                    break;
                case South:
                    y++;
                    break;
                case West:
                    x--;
                    break;
            }
        }
        return result;
    }

    @NonNull
    @Override
    public Ship clone() {
        try {
            return (Ship) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
