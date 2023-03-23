package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class RouteTest {
    private Route testR5_99;

    @BeforeEach
    void runBefore() {
        testR5_99 = new Route("R5, 99", new ArrayList<>());
    }

    @Test
    void testConstructorR5_99() {
        assertEquals("R5, 99", testR5_99.getRouteName());
        assertEquals(new ArrayList<>(), testR5_99.getTimes());
    }

    @Test
    void testAddTime() {
        testR5_99.addTime(60);
        assertEquals(1, testR5_99.getTimes().size());
        assertEquals(60, testR5_99.getTimes().get(0));
    }

    @Test
    void testAddTwoTimes() {
        testR5_99.addTime(50);
        assertEquals(1, testR5_99.getTimes().size());
        assertEquals(50, testR5_99.getTimes().get(0));
        testR5_99.addTime(80);
        assertEquals(2, testR5_99.getTimes().size());
        assertEquals(50, testR5_99.getTimes().get(0));
        assertEquals(80, testR5_99.getTimes().get(1));
    }

    @Test
    void testAddManyTimes() {
        testR5_99.addTime(40);
        testR5_99.addTime(50);
        testR5_99.addTime(60);
        testR5_99.addTime(80);
        assertEquals(4, testR5_99.getTimes().size());
        assertEquals(40, testR5_99.getTimes().get(0));
        assertEquals(50, testR5_99.getTimes().get(1));
        assertEquals(60, testR5_99.getTimes().get(2));
        assertEquals(80, testR5_99.getTimes().get(3));
    }

    @Test
    void testFindAvgTime() {
        testR5_99.addTime(40);
        assertEquals(40, testR5_99.findAvgTime());
        testR5_99.addTime(50);
        assertEquals(45, testR5_99.findAvgTime());
        testR5_99.addTime(60);
        assertEquals(50, testR5_99.findAvgTime());
        testR5_99.addTime(80);
        assertEquals(57, testR5_99.findAvgTime()); // truncated from 57.5
    }

    @Test
    void testRenameRoute() {
        assertEquals("R5, 99", testR5_99.getRouteName());
        testR5_99.renameRoute("R5, 99 B-Line");
        assertEquals("R5, 99 B-Line", testR5_99.getRouteName());
    }

    @Test
    void testTimesTaken() {
        assertEquals(0, testR5_99.timesTaken());
        testR5_99.addTime(40);
        assertEquals(1, testR5_99.timesTaken());
        testR5_99.addTime(50);
        testR5_99.addTime(60);
        assertEquals(3, testR5_99.timesTaken());
    }
}