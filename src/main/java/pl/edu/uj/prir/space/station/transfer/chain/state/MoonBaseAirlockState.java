package pl.edu.uj.prir.space.station.transfer.chain.state;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public abstract class MoonBaseAirlockState {
    private final Logger logger = Logger.getLogger(MoonBaseAirlockState.class.getName());
    protected final MoonBaseAirlockState stateBeforeExecution;
    private final boolean internalDoorsOpen;
    private final boolean externalDoorsOpen;
    private final boolean cargoInside;

    public MoonBaseAirlockState(MoonBaseAirlockState stateBeforeExecution) {
        this.stateBeforeExecution = stateBeforeExecution;
        this.internalDoorsOpen = setInternalDoorsOpen();
        this.externalDoorsOpen = setExternalDoorsOpen();
        this.cargoInside = setCargoInside();
    }

    public MoonBaseAirlockState getStateAfterExecution() {
        return this;
    }

    public MoonBaseAirlockState revert() {
        if (stateBeforeExecution == null) {
            throw new IllegalStateException("stateBeforeExecution is not set!");
        }
        return stateBeforeExecution;
    }

    public boolean isEmptyWithAllDoorsClosed() {
        return !isCargoInside() && !isInternalDoorsOpen() && !isExternalDoorsOpen();
    }

    public abstract void execute(MoonBaseAirlock moonBaseAirlock);

    protected void logExecution(MoonBaseAirlock moonBaseAirlock, String command) {
        logger.log(Level.INFO,
                "executing {0} command for airlock[id: {1}]",
                new Object[]{command, moonBaseAirlock.getId()});
    }

    public abstract String getStateDescription();

    protected abstract boolean setInternalDoorsOpen();

    protected abstract boolean setExternalDoorsOpen();

    protected abstract boolean setCargoInside();

    public static MoonBaseAirlockState getEmptyState() {
        return new EmptyAirlockState();
    }

    public boolean isInternalDoorsOpen() {
        return internalDoorsOpen;
    }

    public boolean isExternalDoorsOpen() {
        return externalDoorsOpen;
    }

    public boolean isCargoInside() {
        return cargoInside;
    }


    public boolean isEmptyWithOpenInternalAndClosedExternalDoors() {
        return isInternalDoorsOpen() && !isCargoInside();
    }

    public boolean isEmptyWithClosedInternalAndOpenExternalDoors() {
        return !isInternalDoorsOpen() && !isCargoInside() && !isExternalDoorsOpen();
    }

    public boolean isStartingState() {
        return this instanceof StartingMoonBaseAirlockState;
    }
}
