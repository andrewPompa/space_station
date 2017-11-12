package pl.edu.uj.prir.space.station.transfer.chain.commands;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;
import pl.edu.uj.prir.space.station.transfer.chain.AirlockCommand;

import java.util.logging.Logger;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public class ExternalDoorOpenCommand extends AirlockCommand {
    @Override
    public void execute(MoonBaseAirlock moonBaseAirlock) {
        logExecutionCommand(moonBaseAirlock, "openExternalDoors");
        moonBaseAirlock.openExternalDoors();
    }
}
