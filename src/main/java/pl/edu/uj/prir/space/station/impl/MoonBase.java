package pl.edu.uj.prir.space.station.impl;

import pl.edu.uj.prir.space.station.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 10.11.17.
 */
public class MoonBase implements MoonBaseInterface, Observer {
    private final Logger logger = Logger.getLogger( MoonBase.class.getName() );
    private int moonBaseAirlockCounter;
    private final List<MoonBaseAirlock> moonBaseAirlockList = new ArrayList<>();
    private final Queue<CargoOrder> cargoOrderQueue = new ConcurrentLinkedDeque<>();

    public MoonBase() {
        moonBaseAirlockCounter = 0;
    }

    @Override
    public void setAirlocksConfiguration(List<AirlockInterface> airlocks) {
        airlocks.forEach(this::registerAirLock);
    }

    @Override
    public void cargoTransfer(CargoInterface cargo, Direction direction) {
        if (!isCargoHasAppropriateSize(cargo)) {
            logger.log(Level.INFO, "Moon Base cannot handle cargo of size: {0}", cargo.getSize());
            return;
        }
        final CargoOrder cargoOrder = new CargoOrder(cargo, direction);
        logger.log(Level.INFO,
                "Inserting cargo({0}) to queue, already waiting {1} elements",
                new Object[]{cargo.toString(), cargoOrderQueue.size()});
        cargoOrderQueue.add(cargoOrder);
        serveCargoOrders();
    }

    @Override
    public void update(Observable o, Object arg) {
        MoonBaseAirlock moonBaseAirlock = (MoonBaseAirlock) o;
        logger.log(Level.INFO,
                "moonBaseAirLock({0}) finished processing",
                moonBaseAirlock.toString());
        serveCargoOrders();

    }

    private void serveCargoOrders() {
        cargoOrderQueue.forEach(cargoOrder -> {
            final Optional<MoonBaseAirlock> moonBaseAirlockOptional = getMoonBaseAirLockToTransferCargo(cargoOrder);
            if (!moonBaseAirlockOptional.isPresent()) {
                logger.log(Level.INFO,
                        "All airlocks busy, cargo({0}) waiting",
                        cargoOrder.getSize());
            } else {
                final MoonBaseAirlock moonBaseAirlock = moonBaseAirlockOptional.get();
                if (moonBaseAirlock.isTransferringSmallerCargoThanAirlockSize() &&
                    moonBaseAirlock.canRejectSmallerCargoThanAirlockSize()) {
                    final CargoOrder rejectedSmallerCargo = moonBaseAirlock.rejectSmallerCargo();
                    cargoOrderQueue.add(rejectedSmallerCargo);
                }
                logger.log(Level.INFO,
                        "Transferring, cargo({0}) by airlock: {1}",
                        new Object[]{cargoOrder.toString(), moonBaseAirlock.toString()});
                moonBaseAirlock.transferCargo(cargoOrder);
                cargoOrderQueue.remove(cargoOrder);
            }
        });
    }

    private int findBestMoonBaseAirlockForCargoOrder(MoonBaseAirlock airlock1, MoonBaseAirlock airlock2, CargoOrder cargo) {
        return getSortPositionBySize(airlock1, airlock2) + calculateSortPositionByOpenDoorAndCargoDirection(airlock1, airlock2, cargo);
    }

    private int calculateSortPositionByOpenDoorAndCargoDirection(MoonBaseAirlock airlock1, MoonBaseAirlock airlock2, CargoOrder cargo) {
        int valueForAirlock1 = airlock1.calculateSortPositionByOpenDoorAndCargoDirection(cargo);
        int valueForAirlock2 = airlock2.calculateSortPositionByOpenDoorAndCargoDirection(cargo);
        if (valueForAirlock1 < 0) {
            return -9;
        }
        if (valueForAirlock2 < 0) {
            return 9;
        }
        if (valueForAirlock1 == 0) {
            return -9;
        }
        return 0;
    }

    private int getSortPositionBySize(MoonBaseAirlock airlock1, MoonBaseAirlock airlock2) {
        return 10 * (airlock1.getSize() - airlock2.getSize());
    }

    private Optional<MoonBaseAirlock> getMoonBaseAirLockToTransferCargo(CargoOrder cargo) {
        return moonBaseAirlockList.stream()
                .filter(moonBaseAirlock -> moonBaseAirlock.canTakeCargo(cargo))
                .sorted((o1, o2) -> findBestMoonBaseAirlockForCargoOrder(o1, o2, cargo))
                .findFirst();
    }

    private boolean isCargoHasAppropriateSize(CargoInterface cargo) {
        return moonBaseAirlockList.stream()
                .anyMatch(moonBaseAirlock -> cargo.getSize() <= moonBaseAirlock.getSize());
    }

    private void registerAirLock(AirlockInterface airlockInterface) {
        ++moonBaseAirlockCounter;
        final MoonBaseAirlock moonBaseAirlock = new MoonBaseAirlock(moonBaseAirlockCounter, airlockInterface);
        moonBaseAirlock.addObserver(this);
        moonBaseAirlockList.add(moonBaseAirlock);
    }
}
