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
        return CargoTransferChain.beginWithEmptyState(moonBaseAirlock).startingWithClosedAllDoorsState();
    }

    public static CargoTransferChain buildMoveCargoOutsideChain(MoonBaseAirlock moonBaseAirlock, MoonBaseAirlockState currentState) {
        final CargoTransferChain cargoTransferChain = CargoTransferChain.begin(moonBaseAirlock);
        if (currentState.isEmptyWithAllDoorsClosed()) {
            cargoTransferChain
                    .startingWithClosedAllDoorsState()
                    .nextInternalDoorsOpenState();
        } else if (currentState.isExternalDoorsOpen()) {
            cargoTransferChain
                    .startingWithExternalDoorsOpenState()
                    .nextExternalDoorsCloseState()
                    .nextInternalDoorsOpenState();
        } else {
            cargoTransferChain.startingWithInternalDoorsOpenState();
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
            cargoTransferChain
                    .startingWithClosedAllDoorsState()
                    .nextExternalDoorsOpenState();
        } else if (currentState.isInternalDoorsOpen()) {
            cargoTransferChain
                    .startingWithInternalDoorsOpenState()
                    .nextInternalDoorsCloseState()
                    .nextExternalDoorsOpenState();
        } else {
            cargoTransferChain.startingWithExternalDoorsOpenState();
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
}
