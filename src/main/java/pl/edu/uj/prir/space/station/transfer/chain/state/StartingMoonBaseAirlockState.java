package pl.edu.uj.prir.space.station.transfer.chain.state;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 14.11.17.
 */
public abstract class StartingMoonBaseAirlockState extends MoonBaseAirlockState{
    public StartingMoonBaseAirlockState(MoonBaseAirlockState stateBeforeExecution) {
        super(stateBeforeExecution);
    }
}
