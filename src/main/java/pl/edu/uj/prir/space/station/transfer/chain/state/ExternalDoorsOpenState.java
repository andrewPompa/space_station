package pl.edu.uj.prir.space.station.transfer.chain.state;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 13.11.17.
 */
public class ExternalDoorsOpenState extends MoonBaseAirlockState {
    public ExternalDoorsOpenState(MoonBaseAirlockState stateBeforeExecution) {
        super(stateBeforeExecution);
    }

    @Override
    public void execute(MoonBaseAirlock moonBaseAirlock) {
        logExecution(moonBaseAirlock, getStateDescription());
        moonBaseAirlock.openExternalDoors();
    }

    @Override
    public String getStateDescription() {
        return "open external";
    }

    @Override
    protected boolean setInternalDoorsOpen() {
        if (stateBeforeExecution.isInternalDoorsOpen()) {
            throw new IllegalStateException("internal doors cannot be open when trying to open external!");
        }
        return false;
    }

    @Override
    protected boolean setExternalDoorsOpen() {
        if (stateBeforeExecution.isExternalDoorsOpen()) {
            throw new IllegalStateException("cannot open open doors!");
        }
        return true;
    }

    @Override
    protected boolean setCargoInside() {
        return stateBeforeExecution.isCargoInside();
    }

    @Override
    protected MoonBaseAirlockState getStateToRevert() {
        return new ExternalDoorsCloseState(this);
    }
}