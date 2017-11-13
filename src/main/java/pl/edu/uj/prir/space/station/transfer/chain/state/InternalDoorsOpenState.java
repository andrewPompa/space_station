package pl.edu.uj.prir.space.station.transfer.chain.state;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 13.11.17.
 */
public class InternalDoorsOpenState extends MoonBaseAirlockState {
    public InternalDoorsOpenState(MoonBaseAirlockState stateBeforeExecution) {
        super(stateBeforeExecution);
    }

    @Override
    public void execute(MoonBaseAirlock moonBaseAirlock) {
        logExecution(moonBaseAirlock, "open internal");
        moonBaseAirlock.openInternalDoors();
    }

    @Override
    protected boolean setInternalDoorsOpen() {
        if (stateBeforeExecution.isInternalDoorsOpen()) {
            throw new IllegalStateException("cannot open open doors!");
        }
        return true;
    }

    @Override
    protected boolean setExternalDoorsOpen() {
        if (stateBeforeExecution.isExternalDoorsOpen()) {
            throw new IllegalStateException("external doors cannot be open when trying to open internal!");
        }
        return false;
    }

    @Override
    protected boolean setCargoInside() {
        return stateBeforeExecution.isCargoInside();
    }
}
