package pl.edu.uj.prir.space.station.transfer.chain;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public class Chain {
    private Deque<AirlockCommand> commandDeque;
    private final MoonBaseAirlock moonBaseAirlock;

    public Chain(MoonBaseAirlock moonBaseAirlock) {
        this.moonBaseAirlock = moonBaseAirlock;
        commandDeque = new ArrayDeque<>();
    }

    public static Chain begin(MoonBaseAirlock moonBaseAirlock) {
        return new Chain(moonBaseAirlock);
    }

    public Chain first(AirlockCommand command) {
        commandDeque.addFirst(command);
        return this;
    }

    public Chain next(AirlockCommand command) {
        commandDeque.add(command);
        return this;
    }

    public boolean execute() {
        if (commandDeque.isEmpty()) {
            return false;
        }
        commandDeque.poll().execute(moonBaseAirlock);
        return true;
    }
}
