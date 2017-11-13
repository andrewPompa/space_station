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
    private final MoonBaseAirlock moonBaseAirlock;

    public CargoTransferChain(MoonBaseAirlock moonBaseAirlock) {
        this.moonBaseAirlock = moonBaseAirlock;
        commandDeque = new ArrayDeque<>();
    }

    public static CargoTransferChain begin(MoonBaseAirlock moonBaseAirlock) {
        return new CargoTransferChain(moonBaseAirlock);
    }
    public CargoTransferChain first(MoonBaseAirlockState state) {
        commandDeque.addFirst(state);
        return this;
    }
    public CargoTransferChain next(MoonBaseAirlockState state) {
        commandDeque.add(state);
        return this;
    }
    public CargoTransferChain nextEmptyState() {
        commandDeque.add(new EmptyAirlockState());
        return this;
    }
    public CargoTransferChain nextInsertCargoState() {
        commandDeque.add(new InjectCargoState(commandDeque.peek()));
        return this;
    }
    public CargoTransferChain nextEjectCargoState() {
        commandDeque.add(new EjectCargoState(commandDeque.peek()));
        return this;
    }
    public CargoTransferChain nextInternalDoorsOpenState() {
        commandDeque.add(new InternalDoorsOpenState(commandDeque.peek()));
        return this;
    }
    public CargoTransferChain nextInternalDoorsCloseState() {
        commandDeque.add(new InternalDoorsCloseState(commandDeque.peek()));
        return this;
    }
    public CargoTransferChain nextExternalDoorsOpenState() {
        commandDeque.add(new ExternalDoorsOpenState(commandDeque.peek()));
        return this;
    }

    public CargoTransferChain nextExternalDoorsCloseState() {
        commandDeque.add(new ExternalDoorsCloseState(commandDeque.peek()));
        return this;
    }

    public MoonBaseAirlockState getCurrent() {
        return commandDeque.peek();
    }

    public boolean execute() {
        if (commandDeque.isEmpty()) {
            return false;
        }
        commandDeque.poll().execute(moonBaseAirlock);
        return true;
    }
}
