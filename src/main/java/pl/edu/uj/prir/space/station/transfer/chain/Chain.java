package pl.edu.uj.prir.space.station.transfer.chain;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public class Chain {
    private Queue<AirlockCommand> commandQueue;
    private final MoonBaseAirlock moonBaseAirlock;

    public Chain(MoonBaseAirlock moonBaseAirlock) {
        this.moonBaseAirlock = moonBaseAirlock;
        commandQueue = new ArrayDeque<>();
    }

    public static Chain begin(MoonBaseAirlock moonBaseAirlock) {
        return new Chain(moonBaseAirlock);
    }

    public Chain next(AirlockCommand command) {
        commandQueue.add(command);
        return this;
    }

    public boolean execute() {
        if (commandQueue.isEmpty()) {
            return false;
        }
        commandQueue.poll().execute(moonBaseAirlock);
        return true;
    }
}
