package pl.edu.uj.prir.space.station;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.edu.uj.prir.space.station.impl.Airlock;
import pl.edu.uj.prir.space.station.impl.Cargo;
import pl.edu.uj.prir.space.station.impl.MoonBase;

import java.util.ArrayList;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 12.11.17.
 */
public class MoonBaseTest {
    private MoonBase moonBase;

    @Test
    public void moonBaseTestOneCargoTransfer() throws InterruptedException {
        CargoInterface cargo = new Cargo(2, Direction.OUTSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        Thread.sleep(10000L);
    }

    @BeforeTest
    public void setAirlocksConfigurationTest() {
//        todo: poprawić observera
//        todo: poprawić stany
//        todo: dokończyć testy
        moonBase = new MoonBase();
        final ArrayList<AirlockInterface> airlockList = new ArrayList<>();
        airlockList.add(new Airlock(3));
        airlockList.add(new Airlock(3));
        airlockList.add(new Airlock(2));
        airlockList.add(new Airlock(4));
        airlockList.add(new Airlock(5));
        moonBase.setAirlocksConfiguration(airlockList);
    }
}
