package pl.edu.uj.prir.space.station.transfer.chain.state;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 13.11.17.
 */
public class InjectCargoState extends MoonBaseAirlockState {
    public InjectCargoState(MoonBaseAirlockState stateBeforeExecution) {
        super(stateBeforeExecution);
    }

    @Override
    public void execute(MoonBaseAirlock moonBaseAirlock) {
        logExecution(moonBaseAirlock, getStateDescription());
        moonBaseAirlock.insertCargo();
    }

    @Override
    public String getStateDescription() {
        return "injecting cargo";
    }

    @Override
    protected boolean setInternalDoorsOpen() {
        return stateBeforeExecution.isInternalDoorsOpen();
    }

    @Override
    protected boolean setExternalDoorsOpen() {
        return stateBeforeExecution.isExternalDoorsOpen();
    }

    @Override
    protected boolean setCargoInside() {
        if (stateBeforeExecution.isCargoInside()) {
            throw new IllegalStateException("cargo already inside!");
        }
        return true;
    }

    @Override
    protected MoonBaseAirlockState getStateToRevert() {
        return new EjectCargoState(this);
    }
}
