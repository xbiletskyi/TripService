package aroundtheeurope.findroute.Services;

import aroundtheeurope.findroute.Models.DepartureInfo;
import aroundtheeurope.findroute.Models.FoundTrip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Service implementation for finding routes between airports.
 */
@Service
public class RouteFinderImpl implements RouteFinder{

    private final RetrieveDeparturesService departureService;

    // Heuristic factor weights injected from properties
    @Value("${RouteFinder.heuristic.priceFactorWeight}")
    double priceFactorWeight;

    @Value("${RouteFinder.heuristic.uniqueCitiesFactorWeight}")
    double uniqueCitiesFactorWeight;

    @Value("${RouteFinder.heuristic.lengthFactorWeight}")
    double lengthFactorWeight;

    /**
     * Constructor for RouteFinderImpl.
     *
     * @param departureService the service used to retrieve departures
     */
    @Autowired
    public RouteFinderImpl(RetrieveDeparturesService departureService) {
        this.departureService = departureService;
    }

    /**
     * Finds routes between airports within the given criteria.
     *
     * @param origin the IATA code of the departure airport
     * @param destination the IATA code of the destination airport
     * @param departureAt the departure date
     * @param maxStay the maximum days between two flights
     * @param budget the maximum amount of money spent on trips
     * @param timeLimit the time limit in seconds for finding routes
     * @param schengenOnly if true, only includes flights within the Schengen Area
     * @return the list of found trips
     */
    @Override
    public List<FoundTrip> findRoute(String origin,
                                     String destination,
                                     String departureAt,
                                     int maxStay,
                                     double budget,
                                     int timeLimit,
                                     boolean schengenOnly) {

        // List to store all possible paths found
        List<List<DepartureInfo>> paths = new ArrayList<>();

        // Create a single thread executor to run the DFS algorithm
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Submit the DFS task to the executor and get a Future object
        Future<?> future = executor.submit(() -> {
//            DFS(origin, destination, departureAt, maxStay, budget, paths, schengenOnly);
            List<List<DepartureInfo>> result = AStarAlgorithm(origin, destination, departureAt, maxStay, budget, schengenOnly);

            synchronized (paths) {
                paths.addAll(result);
            }
        });

        try {
            // Wait for the task to complete within the given time limit
            future.get(timeLimit, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            // If the time limit is exceeded, cancel the task
            future.cancel(true);
        } catch (InterruptedException | ExecutionException e) {
            // Handle possible interruptions and execution exceptions
            e.printStackTrace();
        } finally {
            // Shut down the executor service
            executor.shutdown();
        }

        // List to store the found trips
        List<FoundTrip> foundTrips = new ArrayList<>();
        for (List<DepartureInfo> path : paths) {
            FoundTrip foundTrip = FoundTrip.createFoundTrip(path);
            foundTrips.add(foundTrip);
        }

        // Sort the found trips by the number of unique cities in descending order and then by total price
        foundTrips.sort(
                Comparator.comparingInt(FoundTrip::getUniqueCities).reversed()
                        .thenComparingDouble(FoundTrip::getTotalPrice)
        );

        return foundTrips;
    }

    /**
     * Depth-First Search algorithm to explore possible paths within the given time limit and criteria.
     *
     * @param origin the starting airport
     * @param destination the destination airport
     * @param departureAt the departure date
     * @param daysRange the range of days to explore
     * @param maxPrice the maximum price for the trip
     * @param paths the list to store found paths
     * @param schengenOnly if true, only includes flights within the Schengen Area
     */
    private void DFS(String origin, String destination, String departureAt, int daysRange, double maxPrice,
                     List<List<DepartureInfo>> paths, boolean schengenOnly) {
        // Stack to manage the nodes to visit
        Stack<Node> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        // Push the initial node onto the stack
        stack.push(new Node(origin, departureAt, 0, new ArrayList<>()));

        // Continue searching while there are nodes to visit and the thread is not interrupted
        while (!stack.isEmpty() && !Thread.currentThread().isInterrupted()) {
            Node currentNode = stack.pop();
            String currentNodeKey = currentNode.airport + ":" + currentNode.date;

            // Skip if the node has already been visited
            if (visited.contains(currentNodeKey)) {
                continue;
            }
            // Mark the node as visited
            visited.add(currentNodeKey);

            // List to store all departures within the given date range
            List<DepartureInfo> allDepartures = departureService.retrieveDepartures(origin, departureAt, daysRange, schengenOnly);
            // Sort departures by price in descending order
            allDepartures.sort(Comparator.comparing(DepartureInfo::getPrice).reversed());

            for (DepartureInfo departureInfo : allDepartures) {
                double newPrice = departureInfo.getPrice() + currentNode.price;
                // Check if the new price is within the budget
                if (newPrice < maxPrice) {
                    // Create a new trip path and add the current departure to it
                    List<DepartureInfo> newTrip = new ArrayList<>(currentNode.trip);
                    newTrip.add(departureInfo);

                    // Check if the destination is reached and the path has more than two steps
                    if (departureInfo.getDestinationAirportCode().equals(destination) && currentNode.trip.size() > 2) {
                        paths.add(newTrip);
                    }
                    // Push the next node onto the stack
                    stack.push(new Node(departureInfo.getDestinationAirportCode(), departureInfo.getDepartureAt(),
                            newPrice, newTrip));
                }
            }
        }
    }

    private List<List<DepartureInfo>> AStarAlgorithm(String origin, String destination, String departureAt,
                                                     int daysRange, double maxPrice, boolean schengenOnly){
        List<List<DepartureInfo>> paths = new ArrayList<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getHeuristic));
        Set<String> visited = new HashSet<>();
        Set<String> uniqueCities = new HashSet<>();
        Map<String, Double> gScore = new HashMap<>();
        gScore.put(origin + ":" + destination, 0.0);
        openSet.add(new Node(origin, departureAt, 0, new ArrayList<>(), 0, 0));

        while(!openSet.isEmpty() && !Thread.currentThread().isInterrupted()) {
            Node currentNode = openSet.poll();
            String currentNodeKey = currentNode.airport + ":" + currentNode.date;
            if (visited.contains(currentNodeKey)) {
                continue;
            }
            visited.add(currentNodeKey);
            uniqueCities.add(currentNode.airport);

            List<DepartureInfo> allDepartures = departureService.retrieveDepartures(origin, departureAt, daysRange, schengenOnly);

            for (DepartureInfo departureInfo : allDepartures){
                double newPrice = departureInfo.getPrice() + currentNode.price;
                if (newPrice < maxPrice){
                    List<DepartureInfo> newTrip = new ArrayList<>(currentNode.trip);
                    newTrip.add(departureInfo);
                    double currentHeuristic = calculateHeuristic(departureInfo, currentNode, uniqueCities);
                    double newTotalHeuristic = currentHeuristic + currentNode.getTotalHeuristic();
                    Node nextNode = new Node(departureInfo.getDestinationAirportCode(), departureInfo.getDepartureAt(),
                            newPrice, newTrip, currentHeuristic, newTotalHeuristic);
                    String nextNodeKey = nextNode.airport + ":" + nextNode.date;
                    System.out.println("Explored node: " + nextNodeKey + " heuristic: " + currentHeuristic);
                    if (newTotalHeuristic > gScore.getOrDefault(nextNodeKey, Double.MAX_VALUE)){
                        gScore.put(nextNodeKey, newTotalHeuristic);
                        openSet.add(nextNode);
                    }
                    if (departureInfo.getDestinationAirportCode().equals(destination) && newTrip.size() > 2){
                        paths.add(newTrip);
                    }
                }
            }

        }
        return paths;
    }

    private double calculateHeuristic(DepartureInfo departureInfo, Node newNode, Set<String> uniqueCities) {
        double priceFactor = newNode.price + departureInfo.getPrice();
        double uniqueCitiesFactor = uniqueCities.contains(newNode.airport) ? uniqueCitiesFactorWeight : 0;
        double lengthFactor = uniqueCities.size();

        return priceFactorWeight / priceFactor + uniqueCitiesFactor + lengthFactor * lengthFactorWeight;
    }
    /**
     * Node class representing a step in the trip.
     */
    private static class Node {
        String airport;
        String date;
        double price;
        List<DepartureInfo> trip;
        double heuristic;
        double totalHeuristic;

        Node(String airport, String date, double price, List<DepartureInfo> trip) {
            this.airport = airport;
            this.date = formatDate(date);
            this.price = price;
            this.trip = trip;
        }

        Node(String airport, String date, double price, List<DepartureInfo> trip, double heuristic, double totalHeuristic) {
            this.airport = airport;
            this.date = formatDate(date);
            this.price = price;
            this.trip = trip;
            this.heuristic = heuristic;
            this.totalHeuristic = totalHeuristic;
        }

        private String formatDate(String dateTime) {
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
                LocalDate localDate = localDateTime.toLocalDate();
                return localDate.toString();
            } catch (DateTimeParseException e) {
                LocalDate localDate = LocalDate.parse(dateTime);
                return localDate.toString();
            }
        }

        public double getHeuristic(){
            return heuristic;
        }

        public double getTotalHeuristic(){
            return totalHeuristic;
        }
    }
}
