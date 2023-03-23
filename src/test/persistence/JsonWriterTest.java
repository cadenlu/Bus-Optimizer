package persistence;

import model.Route;
import model.Routes;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Routes routes = new Routes();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmpty() {
        try {
            Routes routes = new Routes();
            JsonWriter writer = new JsonWriter("./data/testWriterEmpty.json");
            writer.open();
            writer.write(routes);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmpty.json");
            routes = reader.read();
            assertEquals(0, routes.getRouteList().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneral() {
        try {
            Routes routes = new Routes();

            ArrayList<Integer> fourTimes = new ArrayList<Integer>();
            ArrayList<Integer> bLineTimes = new ArrayList<Integer>();
            fourTimes.add(20);
            bLineTimes.add(30);
            bLineTimes.add(50);

            routes.addRoute(new Route("the 4", fourTimes));
            routes.addRoute(new Route("99 B-line", bLineTimes));

            JsonWriter writer = new JsonWriter("./data/testWriterGeneral.json");
            writer.open();
            writer.write(routes);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneral.json");
            routes = reader.read();
            List<Route> routeList = routes.getRouteList();
            assertEquals(2, routeList.size());
            checkRoute("the 4", fourTimes, routeList.get(0));
            checkRoute("99 B-line", bLineTimes, routeList.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}