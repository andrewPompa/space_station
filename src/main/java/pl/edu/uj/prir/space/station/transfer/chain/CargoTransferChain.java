package pl.edu.uj.prir.space.station.transfer.chain;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;
import pl.edu.uj.prir.space.station.transfer.chain.state.*;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public class CargoTransferChain {
    private Deque<MoonBaseAirlockState> commandDeque;
    private MoonBaseAirlockState currentState;
    private final MoonBaseAirlock moonBaseAirlock;

    public CargoTransferChain(MoonBaseAirlock moonBaseAirlock) {
        this.moonBaseAirlock = moonBaseAirlock;
        commandDeque = new ArrayDeque<>();
    }
    public static CargoTransferChain begin(MoonBaseAirlock moonBaseAirlock) {
        return new CargoTransferChain(moonBaseAirlock);
    }

    public CargoTransferChain startingWithClosedAllDoorsState() {
        commandDeque.add(new ClosedAllDoorsStartingState());
        return this;
    }
    public CargoTransferChain startingWithInternalDoorsOpenState() {
        commandDeque.add(new OpenInternalDoorsStartingState());
        return this;
    }
    public CargoTransferChain startingWithExternalDoorsOpenState() {
        commandDeque.add(new OpenExternalDoorsStartingState());
        return this;
    }
    public CargoTransferChain nextInsertCargoState() {
        commandDeque.add(new InjectCargoState(commandDeque.peekLast()));
        return this;
    }
    public CargoTransferChain nextEjectCargoState() {
        commandDeque.add(new EjectCargoState(commandDeque.peekLast()));
        return this;
    }
    public CargoTransferChain nextInternalDoorsOpenState() {
        commandDeque.add(new InternalDoorsOpenState(commandDeque.peekLast()));
        return this;
    }
    public CargoTransferChain nextInternalDoorsCloseState() {
        commandDeque.add(new InternalDoorsCloseState(commandDeque.peekLast()));
        return this;
    }
    public CargoTransferChain nextExternalDoorsOpenState() {
        commandDeque.add(new ExternalDoorsOpenState(commandDeque.peekLast()));
        return this;
    }

    public CargoTransferChain nextExternalDoorsCloseState() {
        commandDeque.add(new ExternalDoorsCloseState(commandDeque.peekLast()));
        return this;
    }

    public MoonBaseAirlockState getCurrent() {
        return currentState == null ? commandDeque.peek() : currentState;
    }

    public boolean execute() {
        if (commandDeque.isEmpty()) {
            return false;
        }
        currentState = commandDeque.poll();
        if (currentState.isStartingState()) {
            currentState = commandDeque.poll();
            if (commandDeque.isEmpty()) {
                return false;
            }
        }
        currentState.execute(moonBaseAirlock);
        return true;
    }
}
