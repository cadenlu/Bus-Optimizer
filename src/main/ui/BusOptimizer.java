package ui;

import model.Route;
import model.Routes;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// note: form of the UI package follows from TellerApp example
public class BusOptimizer {
    private static final String JSON_STORE = "./data/busData.json";
    private Scanner input;
    private Routes routes;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the bus optimizer app
    public BusOptimizer() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runBusOptimizer();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runBusOptimizer() {
        boolean keepGoing = true;
        String command;

        init(); // initializes scanner and routes

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase(); // converts all inputs to lowercase

            if (command.equals("q")) { // quits the app
                keepGoing = false;
            } else {
                processCommand(command); // prompts user unless quit
            }
        }
        System.out.println("\n Successfully quit BusOptimizer");
    }

    private void init() {
        input = new Scanner(System.in);
        routes = new Routes();
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
        if (command.equals("c")) {
            addNewRoute();
        } else if (command.equals("r")) {
            removeARoute();
        } else if (command.equals("a")) {
            addATime();
        } else if (command.equals("m")) {
            findAAverage();
        } else if (command.equals("t")) {
            findTimesTaken();
        } else if (command.equals("s")) {
            saveRoutes();
        } else if (command.equals("l")) {
            loadRoutes();
        } else {
            System.out.println("Invalid input, please try again");
        }
    }

    private void findTimesTaken() {
        System.out.println("Which route are we finding the number of times taken of?");
        String routeName = input.next();
        if (routes.getRoute(routeName) == null) {
            System.out.println("Invalid route...");
        } else {
            int timesTaken = routes.getRoute(routeName).timesTaken();
            System.out.println("We have taken route " + routeName + " " + timesTaken + " times");
        }
    }

    // REQUIRES: list of times is not empty
    // EFFECTS: finds the average time for a route
    private void findAAverage() {
        System.out.println("Which route are we finding the average time?");
        String routeName = input.next();
        if (routes.getRoute(routeName) == null) {
            System.out.println("Invalid route...");
        } else {
            int avgTime = routes.getRoute(routeName).findAvgTime();
            System.out.println("The average time of " + routeName + " is: " + avgTime + " minutes");
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a time to a list of times in a route
    private void addATime() {
        System.out.println("Which route are we adding a time to?");
        String routeName = input.next();
        if (routes.getRoute(routeName) == null) {
            System.out.println("Invalid route...");
        } else {
            System.out.println("How long was your trip?");
            int routeTime = input.nextInt();
            if (routeTime < 0) {
                System.out.println("Invalid time...");
            } else {
                routes.getRoute(routeName).addTime(routeTime);
                System.out.println("Added time of " + routeTime + " to " + routeName);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: removes a route from BusOptimizer
    private void removeARoute() {
        System.out.println("Which route would you like to remove?");
        String routeRemoved = input.next();

        if (routes.getRoute(routeRemoved) == null) {
            System.out.println("Invalid route...");
        } else {
            routes.removeRoute(routeRemoved);
            System.out.println("Successfully removed route: " + routeRemoved);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new route to BusOptimizer
    private void addNewRoute() {
        System.out.print("Enter your route name (no spaces): ");
        String name = input.next();
        routes.addRoute(new Route(name, new ArrayList<>()));
        System.out.println("Successfully added new route: " + name);
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tc -> create a new route");
        System.out.println("\tr -> remove a route");
        System.out.println("\ta -> add a time to a route");
        System.out.println("\tm -> find a route's average time");
        System.out.println("\tt -> find times taken of a route");
        System.out.println("\ts -> save routes to file");
        System.out.println("\tl -> load routes from file");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: saves the routes to file
    private void saveRoutes() {
        try {
            jsonWriter.open();
            jsonWriter.write(routes);
            jsonWriter.close();
            System.out.println("Saved routes to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads routes from file
    private void loadRoutes() {
        try {
            routes = jsonReader.read();
            System.out.println("Loaded routes from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}