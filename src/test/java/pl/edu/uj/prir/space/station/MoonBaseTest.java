package pl.edu.uj.prir.space.station;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pl.edu.uj.prir.space.station.impl.Airlock;
import pl.edu.uj.prir.space.station.impl.Cargo;
import pl.edu.uj.prir.space.station.impl.MoonBase;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static java.lang.Thread.sleep;

/**
 * Copyright: Format C
 *
 * @author michal jazowski on 12.11.17.
 */
public class MoonBaseTest {
    private MoonBase moonBase;

    @BeforeTest
    public void setAirlocksConfiguration() {
//        todo: dokończyć testy
        moonBase = new MoonBase();
        final ArrayList<AirlockInterface> airlockList = new ArrayList<>();
        airlockList.add(new Airlock(3));
        airlockList.add(new Airlock(4));
        airlockList.add(new Airlock(2));
        airlockList.add(new Airlock(4));
        airlockList.add(new Airlock(5));
        moonBase.setAirlocksConfiguration(airlockList);
    }

    @Test
    public void oneCargoTransferOutsideTest() throws InterruptedException {
        CargoInterface cargo = new Cargo(2, Direction.OUTSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(5200L);
    }

    @Test
    public void oneCargoTransferInsideTest() throws InterruptedException {
        CargoInterface cargo = new Cargo(2, Direction.INSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(5200L);
    }

    @Test
    public void transferCargoToInsideAndThenFromInsideTest() throws InterruptedException {
        CargoInterface cargo = new Cargo(2, Direction.INSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(5500L);
        cargo = new Cargo(2, Direction.OUTSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(5500L);
    }

    @Test
    public void transferCargoToInsideAndTheToInsideAgainTest() throws InterruptedException {
        CargoInterface cargo = new Cargo(2, Direction.INSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(5500L);
        cargo = new Cargo(2, Direction.INSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(5500L);
    }

    @Test
    public void transferManyCargoesTest() throws InterruptedException {
        CargoInterface cargo = new Cargo(2, Direction.INSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(333L);
        cargo = new Cargo(3, Direction.OUTSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(333L);
        cargo = new Cargo(3, Direction.INSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(5500L);
    }

    @Test
    public void findBestAirlockToCargoTest() throws InterruptedException {
        CargoInterface cargo;
        cargo = new Cargo(4, Direction.INSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        cargo = new Cargo(4, Direction.OUTSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(5200L);
        cargo = new Cargo(4, Direction.OUTSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(5200L);
    }

    @Test
    public void transferManyCargoesOverAirlockSizeTest() throws InterruptedException {
        moonBase = new MoonBase();
        final ArrayList<AirlockInterface> airlockList = new ArrayList<>();
        airlockList.add(new Airlock(2));
        airlockList.add(new Airlock(3));
        moonBase.setAirlocksConfiguration(airlockList);

        IntStream.range(0, 5).forEach(i -> {
            CargoInterface cargo = new Cargo(2, Direction.INSIDE);
            moonBase.cargoTransfer(cargo, cargo.getDirection());
        });
        sleep(18500L);
    }
//    todo: test nie przechodzi

    @Test
    public void moonBaseTestSmallerCargoReject() throws InterruptedException {
        moonBase = new MoonBase();
        final ArrayList<AirlockInterface> airlockList = new ArrayList<>();
        airlockList.add(new Airlock(3));
        moonBase.setAirlocksConfiguration(airlockList);

        CargoInterface cargo = new Cargo(2, Direction.OUTSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(250L);
        cargo = new Cargo(3, Direction.OUTSIDE);
        moonBase.cargoTransfer(cargo, cargo.getDirection());
        sleep(10000L);
    }
}
