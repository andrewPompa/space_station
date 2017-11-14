package pl.edu.uj.prir.space.station.transfer.chain.state;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 14.11.17.
 */
public class OpenExternalDoorsStartingState extends StartingMoonBaseAirlockState {
    public OpenExternalDoorsStartingState(MoonBaseAirlockState stateBeforeExecution) {
        super(stateBeforeExecution);
    }

    public OpenExternalDoorsStartingState() {
        super(null);
    }

    @Override
    public void execute(MoonBaseAirlock moonBaseAirlock) {

    }

    @Override
    public String getStateDescription() {
        return "EXTERNAL_OPEN";
    }

    @Override
    protected boolean setInternalDoorsOpen() {
        return false;
    }

    @Override
    protected boolean setExternalDoorsOpen() {
        return true;
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
