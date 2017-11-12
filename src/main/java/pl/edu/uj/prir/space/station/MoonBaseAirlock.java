package pl.edu.uj.prir.space.station;

import pl.edu.uj.prir.space.station.airlock.state.MoonBaseAirlockCargoState;
import pl.edu.uj.prir.space.station.airlock.state.MoonBaseAirlockDoorState;
import pl.edu.uj.prir.space.station.transfer.chain.Chain;
import pl.edu.uj.prir.space.station.transfer.chain.ChainCommandBuilder;

import java.util.Observable;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
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
    private MoonBaseAirlockDoorState internalDoorState;
    private MoonBaseAirlockDoorState externalDoorState;
    private MoonBaseAirlockCargoState cargoState;
    private ReentrantReadWriteLock stateLock;
    private Chain chain;
    private CargoOrder cargoOrder;

    public MoonBaseAirlock(int id, AirlockInterface airlock) {
        this.id = id;
        this.airlock = airlock;
        listenForAirLockChanges(airlock);
        internalDoorState = MoonBaseAirlockDoorState.CLOSED;
        externalDoorState = MoonBaseAirlockDoorState.CLOSED;
        cargoState = MoonBaseAirlockCargoState.OUTSIDE;
        stateLock = new ReentrantReadWriteLock();
        setState(internalDoorState, externalDoorState, cargoState);
    }

    public int getSize() {
        return airlock.getSize();
    }

    public boolean canTakeCargo(CargoOrder cargo) {
//        jeżeli ładunek jest większy niż rozmiar śluzy
        if (cargo.getSize() > airlock.getSize()) {
            return false;
        }
//        jeżeli ładunek jest mniejszy niż rozmiar śluzy
        if (cargo.getSize() < airlock.getSize()) {
//            jeżel ładunek nie jest obsługiwany to mogę brać
            return cargoOrder == null;
        }
//        teraz mogę miec ładunek który ma taki sam rozmiar jak śluza

//        jeżeli mam już ładunek w śluzie o takim samym rozmiarze to nie chcę kolejnego
        if (cargoOrder != null && cargoOrder.getSize() == airlock.getSize()) {
            return false;
        }
//        ładunku w śluzie nie ma lub jest miniejszy niż rozmiar śluzy

//        jeżeli ładunku jeszcze nie ma śluzie to teoretycznie mogę go obsłużyć
        return canRejectSmallerCargoThanAirlockSize();
    }

    public boolean isTransferringSmallerCargoThanAirlockSize() {
        return cargoOrder != null && cargoOrder.getSize() < airlock.getSize();
    }

    public boolean canRejectSmallerCargoThanAirlockSize() {
        stateLock.readLock().lock();
        try {
            return !isCargoInside();
        } finally {
            stateLock.readLock().unlock();
        }
    }

    public CargoOrder rejectSmallerCargo() {
        if (cargoOrder == null) {
            throw new IllegalStateException("No cargo to transfer!");
        }
        logger.log(Level.INFO, "Cargo rejected from transferring: {0}", cargoOrder.toString());

        final CargoOrder rejectedOrder = new CargoOrder(cargoOrder);
        chain = null;
        cargoOrder = null;
        return rejectedOrder;
    }

    public void transferCargo(CargoOrder cargoOrder) {
        chain = prepareCargoTransferChain(cargoOrder);
        this.cargoOrder = cargoOrder;
        chain.execute();
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
        stateLock.readLock().lock();
        try {
            if (state == MoonBaseAirlockState.EMPTY_ALL_CLOSED) {
                return 0;
            }
            if (cargo.fromInside()) {
                return (state == MoonBaseAirlockState.EMPTY_INTERNAL_OPEN_EXTERNAL_CLOSED) ? -1 : 1;
            }
            return (state == MoonBaseAirlockState.EMPTY_INTERNAL_OPEN_EXTERNAL_CLOSED) ? 1 : -1;
        } finally {
            stateLock.readLock().unlock();
        }
    }

    private Chain prepareCargoTransferChain(CargoOrder cargoOrder) {
        final Chain transferChain = (cargoOrder.fromInside()) ?
                ChainCommandBuilder.buildMoveCargoOutsideChain(this) :
                ChainCommandBuilder.buildMoveCargoInsideChain(this);

        stateLock.readLock().lock();
        try {
            if (state == MoonBaseAirlockState.EMPTY_INTERNAL_CLOSED_EXTERNAL_OPEN && cargoOrder.fromInside()) {
                transferChain.first(ChainCommandBuilder.buildExternalDoorsCloseCommand());
            } else if (state == MoonBaseAirlockState.EMPTY_INTERNAL_OPEN_EXTERNAL_CLOSED && !cargoOrder.fromInside()) {
                transferChain.first(ChainCommandBuilder.buildInternalDoorsCloseCommand());
            }
        } finally {
            stateLock.readLock().unlock();
        }

        return transferChain;
    }

    private void makeTransferCargoStep(AirlockInterface.Event event) {
        setInternalDoorState(event);
        setExternalDoorState(event);
        setCargoState(event);
        setState(internalDoorState, externalDoorState, cargoState);
        final boolean isChainInProgress = chain.execute();
        if (!isChainInProgress) {
            logger.info("Processing finished");
            chain = null;
            cargoOrder = null;
            setChanged();
            notifyObservers();
        }
    }

    private void listenForAirLockChanges(AirlockInterface airlock) {
        airlock.setEventsListener(this::makeTransferCargoStep);
    }

    private void setInternalDoorState(AirlockInterface.Event event) {
        if (event == AirlockInterface.Event.INTERNAL_AIRTIGHT_DOORS_OPENED) {
            internalDoorState = MoonBaseAirlockDoorState.OPEN;
        } else if (event == AirlockInterface.Event.INTERNAL_AIRTIGHT_DOORS_CLOSED) {
            internalDoorState = MoonBaseAirlockDoorState.CLOSED;
        }
    }
    private void setExternalDoorState(AirlockInterface.Event event) {
        if (event == AirlockInterface.Event.EXTERNAL_AIRTIGHT_DOORS_OPENED) {
            externalDoorState = MoonBaseAirlockDoorState.OPEN;
        } else if (event == AirlockInterface.Event.EXTERNAL_AIRTIGHT_DOORS_CLOSED) {
            externalDoorState = MoonBaseAirlockDoorState.CLOSED;
        }
    }
    private void setCargoState(AirlockInterface.Event event) {
        if (event == AirlockInterface.Event.CARGO_INSIDE) {
            cargoState = MoonBaseAirlockCargoState.INSIDE;
        } else if (event == AirlockInterface.Event.AIRLOCK_EMPTY) {
            cargoState = MoonBaseAirlockCargoState.OUTSIDE;
        }
    }
    private void setState(MoonBaseAirlockDoorState internalDoorState, MoonBaseAirlockDoorState externalDoorState, MoonBaseAirlockCargoState cargoState) {
        stateLock.writeLock().lock();
        try {
            if (internalDoorState == MoonBaseAirlockDoorState.OPEN) {
                state = (cargoState == MoonBaseAirlockCargoState.OUTSIDE) ?
                        MoonBaseAirlockState.EMPTY_INTERNAL_OPEN_EXTERNAL_CLOSED :
                        MoonBaseAirlockState.FULL_INTERNAL_OPEN_EXTERNAL_CLOSED;
            } else if (externalDoorState == MoonBaseAirlockDoorState.OPEN) {
                state = (cargoState == MoonBaseAirlockCargoState.OUTSIDE) ?
                        MoonBaseAirlockState.EMPTY_INTERNAL_CLOSED_EXTERNAL_OPEN :
                        MoonBaseAirlockState.FULL_INTERNAL_CLOSED_EXTERNAL_OPEN;
            } else {
                state = (cargoState == MoonBaseAirlockCargoState.OUTSIDE) ?
                        MoonBaseAirlockState.EMPTY_ALL_CLOSED :
                        MoonBaseAirlockState.FULL_ALL_CLOSED;
            }
        } finally {
            stateLock.writeLock().unlock();
        }
    }


    private boolean isCargoInside() {
        return state == MoonBaseAirlockState.FULL_ALL_CLOSED ||
                state == MoonBaseAirlockState.FULL_INTERNAL_OPEN_EXTERNAL_CLOSED ||
                state == MoonBaseAirlockState.FULL_INTERNAL_CLOSED_EXTERNAL_OPEN;

    }

    private MoonBaseAirlockState getState() {
        stateLock.readLock().lock();
        try {
            return state;
        } finally {
            stateLock.readLock().unlock();
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("MoonBaseAirlock[id: %d, size: %d state: %s]", getId(), getSize(), getState());
    }
}
