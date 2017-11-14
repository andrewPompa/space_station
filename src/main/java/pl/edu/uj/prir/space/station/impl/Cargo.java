package pl.edu.uj.prir.space.station.impl;

import pl.edu.uj.prir.space.station.CargoInterface;
import pl.edu.uj.prir.space.station.Direction;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 12.11.17.
 */
public class Cargo implements CargoInterface {
    private static int counter = 0;
    private final int size;
    private final Direction direction;

    public Cargo(int size, Direction direction) {
        this.size = size;
        this.direction = direction;
        ++counter;
    }


    @Override
    public String toString() {
        return String.format("Cargo[id: %d, size: %d, direction: %s]",
                counter,
                getSize(),
                direction);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
