package com.example.restservice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Wind {
    private final String template = "%s at %.1f m/s";
    private Direction dir;
    private float speed;

    public Wind(Direction dir, float speed) {
        this.dir = dir;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return String.format(template, dir.name(), speed);
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
