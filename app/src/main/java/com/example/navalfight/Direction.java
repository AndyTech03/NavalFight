package com.example.navalfight;

public enum Direction {
    North,
    East,
    South,
    West;

    public Direction next(){
        switch (this){
            case North:
                return East;
            case East:
                return South;
            case South:
                return West;
            case West:
                return North;
        }

        throw new IllegalStateException("Неожиданный enum:" + this);
    }

    public Direction previous(){
        switch (this){
            case North:
                return West;
            case East:
                return North;
            case South:
                return East;
            case West:
                return South;
        }

        throw new IllegalStateException("Неожиданный enum:" + this);
    }
}
