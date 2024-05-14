package com.example.navalfight;

public enum Direction {
    North,
    East,
    South,
    West;

    public static Direction fromInt(int i) {
        switch (i){
            case 0:
                return North;
            case 1:
                return East;
            case 2:
                return South;
            case 3:
                return West;
        }
        throw new IllegalStateException("Неверный номер: " + i);
    }

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

    public int toInt() {
        switch (this){
            case North:
                return 0;
            case East:
                return 1;
            case South:
                return 2;
            case West:
                return 3;
        }
        throw new IllegalStateException("Неверный енам: " + this);
    }
}
