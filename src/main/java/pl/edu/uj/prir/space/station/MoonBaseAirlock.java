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
    private final Chain moveCargoOutsideChain;
    private final Chain moveCargoInsideChain;
    private Chain chain;
    private CargoOrder cargoOrder;

    public MoonBaseAirlock(int id, AirlockInterface airlock) {
        this.id = id;
        this.airlock = airlock;


        moveCargoOutsideChain = Chain.begin(this)
                .next(ChainCommandBuilder.buildInternalDoorsOpenCommand())
                .next(ChainCommandBuilder.buildInsertCargoCommand())
                .next(ChainCommandBuilder.buildInternalDoorsCloseCommand())
                .next(ChainCommandBuilder.buildExternalDoorsOpenCommand())
                .next(ChainCommandBuilder.buildExternalDoorsCloseCommand())
                .next(ChainCommandBuilder.buildEjectCargoCommand());

        moveCargoInsideChain = Chain.begin(this)
                .next(ChainCommandBuilder.buildExternalDoorsOpenCommand())
                .next(ChainCommandBuilder.buildInsertCargoCommand())
                .next(ChainCommandBuilder.buildExternalDoorsCloseCommand())
                .next(ChainCommandBuilder.buildInternalDoorsOpenCommand())
                .next(ChainCommandBuilder.buildInternalDoorsCloseCommand())
                .next(ChainCommandBuilder.buildEjectCargoCommand());

        listenForAirLockChanges(airlock);
    }

    public int getSize() {
        return airlock.getSize();
    }

    public boolean canTakeCargo(CargoOrder cargo) {
//        todo: implement when can I take cargo :)
        return false;
//        return cargo.getSize() <= airlock.getSize();
    }

    public void transferCargo(CargoOrder cargoOrder) {
        chain = findAppropriateCargoChain(cargoOrder);
        this.cargoOrder = cargoOrder;
        makeTransferCargoStep(null);

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

    private Chain findAppropriateCargoChain(CargoOrder cargoOrder) {
        return (cargoOrder.fromInside()) ? moveCargoOutsideChain : moveCargoInsideChain;
    }

    private void makeTransferCargoStep(AirlockInterface.Event event) {
        if (!chain.execute()) {
            logger.info("Processing finished");
            notifyObservers();
        }
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
