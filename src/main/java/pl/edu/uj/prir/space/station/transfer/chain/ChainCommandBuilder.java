package pl.edu.uj.prir.space.station.transfer.chain;

import pl.edu.uj.prir.space.station.MoonBaseAirlock;
import pl.edu.uj.prir.space.station.transfer.chain.commands.*;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public final class ChainCommandBuilder {
    private ChainCommandBuilder() {
    }

    public static Chain buildMoveCargoOutsideChain (MoonBaseAirlock moonBaseAirlock) {
       return Chain.begin(moonBaseAirlock)
                .next(ChainCommandBuilder.buildInternalDoorsOpenCommand())
                .next(ChainCommandBuilder.buildInsertCargoCommand())
                .next(ChainCommandBuilder.buildInternalDoorsCloseCommand())
                .next(ChainCommandBuilder.buildExternalDoorsOpenCommand())
               .next(ChainCommandBuilder.buildEjectCargoCommand())
               ;
    }
    public static Chain buildMoveCargoInsideChain (MoonBaseAirlock moonBaseAirlock) {
        return Chain.begin(moonBaseAirlock)
                .next(ChainCommandBuilder.buildExternalDoorsOpenCommand())
                .next(ChainCommandBuilder.buildInsertCargoCommand())
                .next(ChainCommandBuilder.buildExternalDoorsCloseCommand())
                .next(ChainCommandBuilder.buildInternalDoorsOpenCommand())
                .next(ChainCommandBuilder.buildEjectCargoCommand());
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
