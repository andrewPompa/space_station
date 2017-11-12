package pl.edu.uj.prir.space.station.impl;

import pl.edu.uj.prir.space.station.AirlockInterface;
import pl.edu.uj.prir.space.station.CargoInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 12.11.17.
 */
public class Airlock implements AirlockInterface {
    private static final long WAIT_TIME = 1000L;
    private final Logger logger = Logger.getLogger( MoonBase.class.getName() );
    private static final Executor executors = Executors.newCachedThreadPool();
    private final List<EventsListenerInterface> eventsListenerList;
    private final int size;

    public Airlock(int size) {
        this.size = size;
        eventsListenerList = new ArrayList<>();
    }

    @Override
    public void openInternalAirtightDoors() {
        executors.execute(() -> {
            try {
                Thread.sleep(WAIT_TIME );
            } catch (InterruptedException e) {
               logger.log(Level.ALL, "[ERROR] interrupt during openInternalAirtightDoors", e);
            }
            notifyListeners(Event.INTERNAL_AIRTIGHT_DOORS_OPENED);
        });
    }

    @Override
    public void closeInternalAirtightDoors() {
        executors.execute(() -> {
            try {
                Thread.sleep(WAIT_TIME );
            } catch (InterruptedException e) {
                logger.log(Level.ALL, "[ERROR] interrupt during closeInternalAirtightDoors", e);
            }
            notifyListeners(Event.INTERNAL_AIRTIGHT_DOORS_CLOSED);
        });
    }

    @Override
    public void openExternalAirtightDoors() {
        executors.execute(() -> {
            try {
                Thread.sleep(WAIT_TIME );
            } catch (InterruptedException e) {
                logger.log(Level.ALL, "[ERROR] interrupt during openExternalAirtightDoors", e);
            }
            notifyListeners(Event.EXTERNAL_AIRTIGHT_DOORS_OPENED);
        });;
    }

    @Override
    public void closeExternalAirtightDoors() {
        executors.execute(() -> {
            try {
                Thread.sleep(WAIT_TIME );
            } catch (InterruptedException e) {
                logger.log(Level.ALL, "[ERROR] interrupt during closeExternalAirtightDoors", e);
            }
            notifyListeners(Event.EXTERNAL_AIRTIGHT_DOORS_CLOSED);
        });
    }

    @Override
    public void insertCargo(CargoInterface cargo) {
        executors.execute(() -> {
            try {
                Thread.sleep(WAIT_TIME );
            } catch (InterruptedException e) {
                logger.log(Level.ALL, "[ERROR] interrupt during insertCargo", e);
            }
            notifyListeners(Event.CARGO_INSIDE);
        });
    }

    @Override
    public void ejectCargo() {
        executors.execute(() -> {
            try {
                Thread.sleep(WAIT_TIME );
            } catch (InterruptedException e) {
                logger.log(Level.ALL, "[ERROR] interrupt during ejectCargo", e);
            }
            notifyListeners(Event.AIRLOCK_EMPTY);
        });
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setEventsListener(EventsListenerInterface eventsListener) {
        eventsListenerList.add(eventsListener);
    }

    private void notifyListeners(final Event event) {
        eventsListenerList.forEach(eventsListenerInterface -> eventsListenerInterface.newAirlockEvent(event));
    }
}
