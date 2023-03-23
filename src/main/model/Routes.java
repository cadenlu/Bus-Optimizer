package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Routes implements Writable {
    private List<Route> routeList;

    // REQUIRES: no duplicate routes are in the list
    // EFFECTS: constructs a list of routes
    public Routes() {
        this.routeList = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a route to the list of routes
    public void addRoute(Route route) {
        routeList.add(route);
        EventLog.getInstance().logEvent(new Event("Added a new route: " + route.getRouteName()));
    }

    // MODIFIES: this
    // EFFECTS: removes a route from the list of routes
    public boolean removeRoute(String routeName) {
        boolean success = false;

        for (int i = 0; i < routeList.size(); i++) {
            if (Objects.equals(routeName, this.routeList.get(i).getRouteName())) {
                EventLog.getInstance().logEvent(new Event("Route "
                        + this.routeList.get(i).getRouteName() + " was removed"));
                this.routeList.remove(i);
                success = true;
                break;
            }
        }
        return success;
    }


    // getters
    public List<Route> getRouteList() {
        return routeList;
    }

    // EFFECTS: finds a specified route by its route name
    public Route getRoute(String routeName) {
        for (Route route : this.routeList) {
            if (Objects.equals(routeName, route.getRouteName())) {
                return route;
            }
        }
        return null;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("routeList", routesToJson());
        return json;
    }

    // EFFECTS: returns routes in this list of routes as a JSON array
    private JSONArray routesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Route route : routeList) {
            jsonArray.put(route.toJson());
        }
        return jsonArray;
    }
}