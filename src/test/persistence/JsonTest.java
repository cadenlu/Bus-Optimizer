package persistence;

import model.Route;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkRoute(String routeName, List<Integer> times, Route route) {
        assertEquals(routeName, route.getRouteName());
        assertEquals(times, route.getTimes());
    }
}
