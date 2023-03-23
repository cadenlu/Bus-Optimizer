package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import model.Event;
import model.EventLog;
import model.Route;
import model.Routes;
import org.json.*;

// Form of JSON reader follows from JsonSerializationDemo example
// Represents a reader that reads routes from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads routes from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Routes read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseRoutes(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses routes from JSON object and returns it
    private Routes parseRoutes(JSONObject jsonObject) {
        Routes routes = new Routes();
        addRoutes(routes, jsonObject);
        EventLog.getInstance().logEvent(new Event("Finished loading routes from file"));
        return routes;
    }

    // MODIFIES: routes
    // EFFECTS: parses route(s) from JSON object and adds them to routes
    private void addRoutes(Routes routes, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("routeList");
        for (Object json : jsonArray) {
            JSONObject nextRoute = (JSONObject) json;
            addRoute(routes, nextRoute);
        }
    }

    // MODIFIES: routes
    // EFFECTS: parses route from JSON object and adds it to routes
    private void addRoute(Routes routes, JSONObject jsonObject) {
        ArrayList<Integer> times = new ArrayList<>();

        String routeName = jsonObject.getString("routeName"); // gets name of route
        JSONArray jsonArray = jsonObject.getJSONArray("times"); // gets times of route
        for (Object json : jsonArray) {
            Integer nextTime = (Integer) json;
            times.add(nextTime);
        }
        Route route = new Route(routeName, times);
        routes.addRoute(route);
    }
}
