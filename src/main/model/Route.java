package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.List;


// Represents a transit route having a route name and a list of times (minutes)
public class Route implements Writable {
    private String routeName;
    private List<Integer> times;

    // REQUIRES: routeName does not have zero length
    // EFFECTS: constructs route with a name and a list of times taken
    public Route(String routeName, List<Integer> times) {
        this.routeName = routeName;
        this.times = times;
    }

    // MODIFIES: this
    // EFFECTS: adds a time to a route
    public void addTime(int time) {
        this.times.add(time);
        EventLog.getInstance().logEvent(new Event("Time of " + time + " added to " + this.routeName));
    }

    // REQUIRES: list of times is not empty
    // EFFECTS: finds the average time in a route
    public int findAvgTime() {
        int total = 0;
        for (int time : this.times) {
            total += time;
        }
        int avg = total / this.times.size();
        EventLog.getInstance().logEvent(new Event("Calculated average time of "
                + avg + " for the route " + this.routeName));
        return avg;
    }

    // MODIFIES: this
    // EFFECTS: renames a route's name to a new name
    public void renameRoute(String newName) {
        this.routeName = newName;
        EventLog.getInstance().logEvent(new Event("Renamed route " + this.routeName + " to " + newName));
    }

    // EFFECTS: finds how many times we have taken a route
    public int timesTaken() {
        EventLog.getInstance().logEvent(new Event("Found route "
                + this.routeName + " was taken " + this.times.size() + " times"));
        return this.times.size();
    }

    // getters
    public String getRouteName() {
        return this.routeName;
    }

    public List<Integer> getTimes() {
        return this.times;
    }


    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("routeName", routeName);
        json.put("times", timesToJson());
        return json;
    }

    // EFFECTS: returns times in this list of times as a JSON array
    private JSONArray timesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Integer time : times) {
            jsonArray.put(time);
        }
        return jsonArray;
    }
}

