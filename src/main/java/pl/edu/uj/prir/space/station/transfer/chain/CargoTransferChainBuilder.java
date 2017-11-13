package pl.edu.uj.prir.space.station.transfer.chain;

import pl.edu.uj.prir.space.station.CargoOrder;
import pl.edu.uj.prir.space.station.MoonBaseAirlock;
import pl.edu.uj.prir.space.station.transfer.chain.state.MoonBaseAirlockState;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public final class CargoTransferChainBuilder {
    private CargoTransferChainBuilder() {
    }

    public static CargoTransferChain buildEmptyCargoTransferChain(MoonBaseAirlock moonBaseAirlock) {
        return CargoTransferChain.begin(moonBaseAirlock).nextEmptyState();
    }

    public static CargoTransferChain buildMoveCargoOutsideChain(MoonBaseAirlock moonBaseAirlock, MoonBaseAirlockState currentState) {
        final CargoTransferChain cargoTransferChain = CargoTransferChain.begin(moonBaseAirlock);
        if (currentState.isEmptyWithAllDoorsClosed()) {
            cargoTransferChain.nextInternalDoorsOpenState();
        } else if (currentState.isExternalDoorsOpen()) {
            cargoTransferChain
                    .nextExternalDoorsCloseState()
                    .nextInternalDoorsOpenState();
        }
        return cargoTransferChain
                .nextInsertCargoState()
                .nextInternalDoorsCloseState()
                .nextExternalDoorsOpenState()
                .nextEjectCargoState();

    }

    public static CargoTransferChain buildMoveCargoInsideChain(MoonBaseAirlock moonBaseAirlock, MoonBaseAirlockState currentState) {
        final CargoTransferChain cargoTransferChain = CargoTransferChain.begin(moonBaseAirlock);
        if (currentState.isEmptyWithAllDoorsClosed()) {
            cargoTransferChain.nextExternalDoorsOpenState();
        } else if (currentState.isInternalDoorsOpen()) {
            cargoTransferChain
                    .nextInternalDoorsCloseState()
                    .nextExternalDoorsOpenState();
        }
        return cargoTransferChain
                .nextInsertCargoState()
                .nextExternalDoorsCloseState()
                .nextInternalDoorsOpenState()
                .nextEjectCargoState();

    }

    public static CargoTransferChain buildCargoChain(MoonBaseAirlock moonBaseAirlock, CargoOrder cargoOrder, CargoTransferChain cargoTransferChain) {
        return (cargoOrder.fromInside()) ?
                CargoTransferChainBuilder.buildMoveCargoOutsideChain(
                        moonBaseAirlock,
                        cargoTransferChain.getCurrent()
                ) :
                CargoTransferChainBuilder.buildMoveCargoInsideChain(
                        moonBaseAirlock,
                        cargoTransferChain.getCurrent());
    }

    public static CargoTransferChain buildCargoChainFromRejectedChain(MoonBaseAirlock moonBaseAirlock, CargoOrder cargoOrder, CargoTransferChain cargoTransferChain) {
        CargoTransferChain newCargoTransferChain = CargoTransferChain.begin(moonBaseAirlock).next(cargoTransferChain.getCurrent().revert());
        return buildCargoChain(moonBaseAirlock, cargoOrder, newCargoTransferChain);
    }
}
