package pl.edu.uj.prir.space.station.transfer.chain;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public interface AirlockCommand {
    void execute(MoonBaseAirlock moonBaseAirlock);
}
