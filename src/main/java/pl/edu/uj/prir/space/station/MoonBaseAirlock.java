package pl.edu.uj.prir.space.station;

import pl.edu.uj.prir.space.station.transfer.chain.Chain;
import pl.edu.uj.prir.space.station.transfer.chain.ChainCommandBuilder;

import java.util.Observable;
import java.util.logging.Logger;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 11.11.17.
 */
public class MoonBaseAirlock extends Observable {
    private final Logger logger = Logger.getLogger(MoonBaseAirlock.class.getName());
    private final int id;
    private final AirlockInterface airlock;
    private MoonBaseAirlockState state;
    private Chain chain;
    private CargoOrder cargoOrder;

    public MoonBaseAirlock(int id, AirlockInterface airlock) {
        this.id = id;
        this.airlock = airlock;
        listenForAirLockChanges(airlock);
    }

    public int getSize() {
        return airlock.getSize();
    }

    public boolean canTakeCargo(CargoOrder cargo) {
//        todo: implement when can I take cargo :)
        if (cargo.getSize() > airlock.getSize()) {
            return false;
        }
        if (cargo.getSize() < airlock.getSize() && cargoOrder == null) {
            return true;
        }
        if (cargoOrder.getSize() == airlock.getSize()) {
            return false;
        }
        if (state == MoonBaseAirlockState.FULL) {
            return false;
        }
        return true;
//        TODO: zaimpelementować stany
    }

    public void transferCargo(CargoOrder cargoOrder) {
        chain = prepareCargoTransferChain(cargoOrder);
        this.cargoOrder = cargoOrder;
//        prepareAirlockToCargoTransfer();
    }

    private void prepareAirlockToCargoTransfer() {

    }

    public void insertCargo() {
        airlock.insertCargo(cargoOrder.getCargo());
    }

    public void ejectCargo() {
        airlock.ejectCargo();
    }

    public void openInternalDoors() {
        airlock.openInternalAirtightDoors();
    }

    public void closeInternalDoors() {
        airlock.closeInternalAirtightDoors();
    }

    public void openExternalDoors() {
        airlock.openExternalAirtightDoors();
    }

    public void closeExternalDoors() {
        airlock.closeExternalAirtightDoors();
    }

    public int calculateSortPositionByOpenDoorAndCargoDirection(CargoOrder cargo) {
        if (state == MoonBaseAirlockState.EMPTY_ALL_CLOSED) {
            return 0;
        }
        if (cargo.fromInside()) {
            return (state == MoonBaseAirlockState.EMPTY_INTERNAL_OPEN) ? -1 : 1;
        }
        return (state == MoonBaseAirlockState.EMPTY_INTERNAL_OPEN) ? 1 : -1;
    }

    private Chain prepareCargoTransferChain(CargoOrder cargoOrder) {
        final Chain transferChain = (cargoOrder.fromInside()) ?
                ChainCommandBuilder.buildMoveCargoOutsideChain(this) :
                ChainCommandBuilder.buildMoveCargoInsideChain(this);

        if (state == MoonBaseAirlockState.EMPTY_INTERNAL_CLOSED && cargoOrder.fromInside()) {
            transferChain.first(ChainCommandBuilder.buildExternalDoorsCloseCommand());
        } else if (state == MoonBaseAirlockState.EMPTY_INTERNAL_OPEN && !cargoOrder.fromInside()) {
            transferChain.first(ChainCommandBuilder.buildInternalDoorsCloseCommand());
        }
        return transferChain;
    }

    private void makeTransferCargoStep(AirlockInterface.Event event) {
        final boolean isChainFinished = chain.execute();
        if (!isChainFinished) {
            logger.info("Processing finished");
            chain = null;
            cargoOrder = null;
            setCurrentEvent(event);
            notifyObservers();
        }
    }

    private void setCurrentEvent(AirlockInterface.Event event) {
        if (event == AirlockInterface.Event.INTERNAL_AIRTIGHT_DOORS_CLOSED) {
            state = MoonBaseAirlockState.EMPTY_INTERNAL_CLOSED;
        } else if (event == AirlockInterface.Event.INTERNAL_AIRTIGHT_DOORS_OPENED) {
            state = MoonBaseAirlockState.EMPTY_INTERNAL_CLOSED;
        }
        state = MoonBaseAirlockState.EMPTY_ALL_CLOSED;
    }

    private void listenForAirLockChanges(AirlockInterface airlock) {
        airlock.setEventsListener(this::makeTransferCargoStep);
    }

    private void airlockEmpty() {
        logger.info("MoonBaseAirlock is empty");
        notifyObservers();
    }
    

    public int getId() {
        return id;
    }
}
