package pl.edu.uj.prir.space.station.transfer.chain;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public class Chain {
    private Deque<AirlockCommand> commandList;
    private int commandCounter;
    private final MoonBaseAirlock moonBaseAirlock;

    public Chain(MoonBaseAirlock moonBaseAirlock) {
        this.moonBaseAirlock = moonBaseAirlock;
        commandCounter = 0;
        commandList = new ArrayDeque<>();
    }

    public static Chain begin(MoonBaseAirlock moonBaseAirlock) {
        return new Chain(moonBaseAirlock);
    }

    public Chain first(AirlockCommand command) {
        commandList.addFirst(command);
        return this;
    }

    public Chain next(AirlockCommand command) {
        commandList.add(command);
        return this;
    }

    public boolean execute() {
        if (commandCounter == commandList.size()) {
            return false;
        }
        commandList.peek().execute(moonBaseAirlock);
        ++commandCounter;
        return true;
    }
}
