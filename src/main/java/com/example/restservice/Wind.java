package com.example.restservice;

import java.util.Random;

public class Wind {
    private final Direction dir;
    private final float speed;

    public Wind(Direction dir, float speed) {
        this.dir = dir;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return String.format("%s at %.1f m/s", dir.name(), speed);
    }

    public Direction getDir() {
        return dir;
    }

    public float getSpeed() {
        return speed;
    }

    public enum Direction {
        N,E,S,W,NE,SE,SW,NW;

        public static Direction randomDir(Random rnd)  {
            return values()[rnd.nextInt(values().length)];
        }
    }
}
