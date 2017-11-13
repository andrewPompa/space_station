package pl.edu.uj.prir.space.station.transfer.chain.state;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 13.11.17.
 */
public class ExternalDoorsCloseState extends MoonBaseAirlockState {
    public ExternalDoorsCloseState(MoonBaseAirlockState stateBeforeExecution) {
        super(stateBeforeExecution);
    }

    @Override
    public void execute(MoonBaseAirlock moonBaseAirlock) {
        logExecution(moonBaseAirlock, "close external");
        moonBaseAirlock.closeExternalDoors();
    }

    @Override
    protected boolean setInternalDoorsOpen() {
        return false;
    }

    @Override
    protected boolean setExternalDoorsOpen() {
        if (!stateBeforeExecution.isExternalDoorsOpen()) {
            throw new IllegalStateException("cannot close closed doors!");
        }
        return false;
    }

    @Override
    protected boolean setCargoInside() {
        return stateBeforeExecution.isCargoInside();
    }
}
