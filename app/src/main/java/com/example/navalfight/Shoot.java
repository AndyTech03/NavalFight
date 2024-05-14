package com.example.navalfight;

import android.graphics.Point;

public class Shoot {
    public enum Result {
        Miss,
        Hit,
        Invalid,
    }
    private final Point target;
    private final Result result;

    public Shoot(Point location, Result result){
        this.target = location;
        this.result = result;
    }

    public Point getTarget() {
        return target;
    }

    public Result getResult() {
        return result;
    }
}
