package pl.edu.uj.prir.space.station.transfer.chain.commands;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;
import pl.edu.uj.prir.space.station.transfer.chain.AirlockCommand;

import java.util.logging.Logger;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public class ExternalDoorCloseCommand extends AirlockCommand {
    private final Logger logger = Logger.getLogger(ExternalDoorCloseCommand.class.getName());

    @Override
    public void execute(final MoonBaseAirlock moonBaseAirlock) {
        logExecutionCommand(moonBaseAirlock, "closeExternalDoors");
        moonBaseAirlock.closeExternalDoors();
    }
}
