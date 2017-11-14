package pl.edu.uj.prir.space.station.transfer.chain.state;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 13.11.17.
 */
public class EmptyAirlockState extends MoonBaseAirlockState {
    public EmptyAirlockState() {
        this(null);
    }

    public EmptyAirlockState(MoonBaseAirlockState stateBeforeExecution) {
        super(null);
    }

    @Override
    public void execute(MoonBaseAirlock moonBaseAirlock) {

    }

    @Override
    public String getStateDescription() {
        return "EMPTY AIRLOCK STATE";
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

    @Override
    protected MoonBaseAirlockState getStateToRevert() {
        return this;
    }
}
