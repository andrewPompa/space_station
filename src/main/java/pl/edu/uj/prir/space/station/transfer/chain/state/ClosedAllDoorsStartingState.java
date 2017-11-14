package pl.edu.uj.prir.space.station.transfer.chain.state;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 14.11.17.
 */
public class ClosedAllDoorsStartingState extends StartingMoonBaseAirlockState {
    public ClosedAllDoorsStartingState(MoonBaseAirlockState stateBeforeExecution) {
        super(stateBeforeExecution);
    }

    public ClosedAllDoorsStartingState() {
        super(null);
    }

    @Override
    public void execute(MoonBaseAirlock moonBaseAirlock) {

    }

    @Override
    public String getStateDescription() {
        return "CLOSED_ALL";
    }

    @Override
    protected boolean setInternalDoorsOpen() {
        return false;
    }

    @Override
    protected boolean setExternalDoorsOpen() {
        return false;
    }

    @Override
    protected boolean setCargoInside() {
        return false;
    }
}
