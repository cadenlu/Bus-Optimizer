package persistence;

import model.Route;
import model.Routes;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/fakeFile.json");
        try {
            Routes routes = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyRoutes() {
        JsonReader reader = new JsonReader("./data/testReaderEmpty.json");
        try {
            Routes routes = reader.read();
            assertEquals(0, routes.getRouteList().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralRoutes() {
        JsonReader reader = new JsonReader("./data/testReaderGeneral.json");

        ArrayList<Integer> fourTimes = new ArrayList<Integer>();
        ArrayList<Integer> bLineTimes = new ArrayList<Integer>();
        fourTimes.add(20);
        bLineTimes.add(30);
        bLineTimes.add(50);

        try {
            Routes routes = reader.read();
            List<Route> routeList = routes.getRouteList();
            assertEquals(2, routeList.size());
            checkRoute("the 4", fourTimes, routeList.get(0));
            checkRoute("99 B-line", bLineTimes, routeList.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}