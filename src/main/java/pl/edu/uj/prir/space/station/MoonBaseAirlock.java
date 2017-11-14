package pl.edu.uj.prir.space.station;

import pl.edu.uj.prir.space.station.transfer.chain.CargoTransferChain;
import pl.edu.uj.prir.space.station.transfer.chain.CargoTransferChainBuilder;
import pl.edu.uj.prir.space.station.transfer.chain.state.MoonBaseAirlockState;

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
    private CargoTransferChain cargoTransferChain;
    private CargoOrder cargoOrder;
    private MoonBaseAirlockState currentState;
    private final ReentrantReadWriteLock stateLock;

    public MoonBaseAirlock(int id, AirlockInterface airlock) {
        this.id = id;
        this.airlock = airlock;
        this.stateLock = new ReentrantReadWriteLock();
        cargoTransferChain = CargoTransferChainBuilder.buildEmptyCargoTransferChain(this);
        listenForAirLockChanges(airlock);
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

    public CargoOrder rejectSmallerCargoAndTransferNew(CargoOrder newCargoOrder) {
        if (cargoOrder == null) {
            throw new IllegalStateException("No cargo to transfer!");
        }
        logger.log(Level.INFO, "Cargo rejected from transferring: {0}", cargoOrder.toString());

        final CargoOrder rejectedOrder = new CargoOrder(cargoOrder);
        stateLock.readLock().lock();
        try {
            cargoTransferChain = CargoTransferChainBuilder.buildCargoChain(
                    this,
                    cargoOrder,
                    cargoTransferChain);
        } finally {
            stateLock.readLock().unlock();
        }
        cargoOrder = newCargoOrder;
        return rejectedOrder;
    }

    public void transferCargo(CargoOrder cargoOrder) {
        stateLock.readLock().lock();
        try {
            cargoTransferChain = CargoTransferChainBuilder.buildCargoChain(
                    this,
                    cargoOrder,
                    cargoTransferChain);
        } finally {
            stateLock.readLock().unlock();
        }
        this.cargoOrder = cargoOrder;
        cargoTransferChain.execute();
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
            if (!cargoTransferChain.getCurrent().isEmptyWithAllDoorsClosed()) {
                return 0;
            }
            if (cargo.fromInside()) {
                return (cargoTransferChain.getCurrent().isEmptyWithOpenInternalAndClosedExternalDoors()) ? -1 : 1;
            }
            return (cargoTransferChain.getCurrent().isEmptyWithClosedInternalAndOpenExternalDoors()) ? -1 : 1;
        } finally {
            stateLock.readLock().unlock();
        }
    }

    private void makeTransferCargoStep(AirlockInterface.Event event) {

        final boolean isChainInProgress = cargoTransferChain.execute();
        if (!isChainInProgress) {
            logger.info("Processing finished");
            cargoOrder = null;
            setChanged();
            notifyObservers();
        }
    }

    private void listenForAirLockChanges(AirlockInterface airlock) {
        airlock.setEventsListener(this::makeTransferCargoStep);
    }

    private boolean isCargoInside() {
        return cargoTransferChain.getCurrent().isCargoInside();

    }

    private String getStateDescription() {
        stateLock.readLock().lock();
        try {
            return cargoTransferChain.getCurrent().getStateDescription();
        } finally {
            stateLock.readLock().unlock();
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("MoonBaseAirlock[id: %d, size: %d state: %s]", getId(), getSize(), getStateDescription());
    }
}
