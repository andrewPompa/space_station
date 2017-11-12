package pl.edu.uj.prir.space.station.transfer.chain.commands;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;
import pl.edu.uj.prir.space.station.transfer.chain.AirlockCommand;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public class InternalDoorOpenCommand extends AirlockCommand {

    @Override
    public void execute(final MoonBaseAirlock moonBaseAirlock) {
        logExecutionCommand(moonBaseAirlock, "openInternalDoors");
        moonBaseAirlock.openInternalDoors();
    }
}
