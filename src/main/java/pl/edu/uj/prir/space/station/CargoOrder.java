package pl.edu.uj.prir.space.station;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public class CargoOrder {
    private final CargoInterface cargo;
    private final Direction direction;

    public CargoOrder(CargoInterface cargo, Direction direction) {
        this.cargo = cargo;
        this.direction = direction;
    }

    public CargoInterface getCargo() {
        return cargo;
    }

    public int getSize() {
        return cargo.getSize();
    }

    public boolean fromInside() {
        return direction == Direction.OUTSIDE;
    }
}
