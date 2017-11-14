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
    public void execute(MoonBaseAirlock moonBaseAirlock) {
        logExecution(moonBaseAirlock, getStateDescription());
        moonBaseAirlock.closeInternalDoors();
    }

    @Override
    public String getStateDescription() {
        return "internal close";
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

    @Override
    protected MoonBaseAirlockState getStateToRevert() {
        return new InternalDoorsOpenState(this);
    }
}
