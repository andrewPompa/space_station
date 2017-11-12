package pl.edu.uj.prir.space.station.transfer.chain;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public abstract class AirlockCommand {
    private final Logger logger = Logger.getLogger(AirlockCommand.class.getName());

    protected void logExecutionCommand(MoonBaseAirlock moonBaseAirlock, String command) {
        logger.log(Level.INFO,
                "executing {0} command for {1}",
                new Object[]{command, moonBaseAirlock.toString()});
    }
    protected abstract void execute(MoonBaseAirlock moonBaseAirlock);
}
