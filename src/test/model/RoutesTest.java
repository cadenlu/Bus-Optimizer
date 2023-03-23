package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;


public class RoutesTest {
    private Route test4;
    private Route test99;
    private static Routes testRoutes;

    @BeforeEach
    void runBefore() {
        test4 = new Route("the 4", new ArrayList<>());
        test99 = new Route("99 B-line", new ArrayList<>());
        testRoutes = new Routes();
    }

    @Test
    void testConstructor() {
        assertEquals(new ArrayList<>(), testRoutes.getRouteList());
        assertEquals(0, testRoutes.getRouteList().size());
    }

    @Test
    void testAddRoute() {
        testRoutes.addRoute(test4);
        testRoutes.addRoute(test99);

        assertEquals(2, testRoutes.getRouteList().size());
        assertEquals(test4, testRoutes.getRouteList().get(0));
        assertEquals(test99, testRoutes.getRouteList().get(1));
    }

    @Test
    void testRemoveRoute() {
        testRoutes.addRoute(test4);
        testRoutes.addRoute(test99);

        assertFalse(testRoutes.removeRoute("fakeRoute"));
        assertEquals(2, testRoutes.getRouteList().size());
        assertTrue(testRoutes.removeRoute("99 B-line"));

        assertEquals(1, testRoutes.getRouteList().size());
        assertEquals("the 4", testRoutes.getRouteList().get(0).getRouteName());

        assertTrue(testRoutes.removeRoute("the 4"));
        assertEquals(0, testRoutes.getRouteList().size());
    }

    @Test
    void getRoute() {
        testRoutes.addRoute(test4);
        testRoutes.addRoute(test99);

        assertEquals(test4, testRoutes.getRoute("the 4"));
        assertEquals(test99, testRoutes.getRoute("99 B-line"));

        assertEquals(null, testRoutes.getRoute("fakeRoute"));
    }
}
