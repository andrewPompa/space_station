package pl.edu.uj.prir.space.station;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public class CargoOrder {
    private static int counter = 0;
    private final CargoInterface cargo;
    private final Direction direction;

    public CargoOrder(CargoInterface cargo, Direction direction) {
        this.cargo = cargo;
        this.direction = direction;
        ++counter;
    }

    public CargoOrder(CargoOrder cargoOrder) {
        this(cargoOrder.getCargo(), cargoOrder.getDirection());

    }

    @Override
    public String toString() {
        return String.format("Cargo[id: %d, size: %d, direction: %s]",
                counter,
                getSize(),
                cargo.getDirection());
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

    private Direction getDirection() {
        return direction;
    }
}
