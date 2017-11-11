package pl.edu.uj.prir.space.station.transfer.chain;

import pl.edu.uj.prir.space.station.transfer.chain.commands.*;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public final class ChainCommandBuilder {
    private ChainCommandBuilder() {
    }

    public static AirlockCommand buildInsertCargoCommand() {
        return new InsertCargoCommand();
    }
    public static AirlockCommand buildEjectCargoCommand() {
        return new EjectCargoCommand();
    }
    public static AirlockCommand buildInternalDoorsOpenCommand() {
        return new InternalDoorOpenCommand();
    }
    public static AirlockCommand buildInternalDoorsCloseCommand() {
        return new InternalDoorCloseCommand();
    }
    public static AirlockCommand buildExternalDoorsOpenCommand() {
        return new ExternalDoorOpenCommand();
    }
    public static AirlockCommand buildExternalDoorsCloseCommand() {
        return new ExternalDoorCloseCommand();
    }
}
