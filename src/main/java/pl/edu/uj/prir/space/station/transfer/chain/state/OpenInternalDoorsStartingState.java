package pl.edu.uj.prir.space.station.transfer.chain.state;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 14.11.17.
 */
public class OpenInternalDoorsStartingState extends StartingMoonBaseAirlockState {
    public OpenInternalDoorsStartingState(MoonBaseAirlockState stateBeforeExecution) {
        super(stateBeforeExecution);
    }

    public OpenInternalDoorsStartingState() {
        super(null);
    }

    @Override
    public void execute(MoonBaseAirlock moonBaseAirlock) {

    }

    @Override
    public String getStateDescription() {
        return "INTERNAL_OPEN";
    }

    @Override
    protected boolean setInternalDoorsOpen() {
        return true;
    }

    @Override
    protected boolean setExternalDoorsOpen() {
        return false;
    }

    @Override
    protected boolean setCargoInside() {
        return false;
    }

    @Override
    protected MoonBaseAirlockState getStateToRevert() {
        return this;
    }
}
