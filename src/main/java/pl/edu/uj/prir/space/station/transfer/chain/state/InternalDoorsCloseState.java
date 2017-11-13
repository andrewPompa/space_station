package pl.edu.uj.prir.space.station.transfer.chain.state;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 13.11.17.
 */
public class InternalDoorsCloseState extends MoonBaseAirlockState {
    public InternalDoorsCloseState(MoonBaseAirlockState stateBeforeExecution) {
        super(stateBeforeExecution);
    }

    @Override
    protected void execute(MoonBaseAirlock moonBaseAirlock) {
        logExecution(moonBaseAirlock, "internal close");
        moonBaseAirlock.openExternalDoors();
    }

    @Override
    protected boolean setInternalDoorsOpen() {
        if (!stateBeforeExecution.isInternalDoorsOpen()) {
            throw new IllegalStateException("cannot close closed doors!");
        }
        return false;
    }

    @Override
    protected boolean setExternalDoorsOpen() {
        return false;
    }

    @Override
    protected boolean setCargoInside() {
        return stateBeforeExecution.isCargoInside();
    }
}
