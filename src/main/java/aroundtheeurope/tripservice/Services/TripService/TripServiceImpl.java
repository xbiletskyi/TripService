package aroundtheeurope.tripservice.Services.TripService;

import aroundtheeurope.tripservice.Models.DepartureInfo;
import aroundtheeurope.tripservice.Models.FoundTrip;

import aroundtheeurope.tripservice.Services.TripService.DFS.DFS;
import aroundtheeurope.tripservice.Services.TripService.Heuristic.HeuristicAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

/**
 * Service implementation for finding routes between airports.
 */
@Service
public class TripServiceImpl implements TripService {

    private final HeuristicAlgorithm aStarAlgorithm;
    private final DFS dfs;

    /**
     * Constructor for RouteFinderImpl.
     *
     * @param aStarAlgorithm service to apply algorithm on input
     */
    @Autowired
    public TripServiceImpl(HeuristicAlgorithm aStarAlgorithm, DFS dfs) {
        this.aStarAlgorithm = aStarAlgorithm;
        this.dfs = dfs;
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

        // Submit the A* task to the executor and get a Future object
        Future<?> future;
        if (timeLimit < 100){
            future = executor.submit(() -> {
                aStarAlgorithm.findRoutes(origin, destination, departureAt, maxStay, budget, schengenOnly, paths);
            });
        }
        else {
            future = executor.submit(() -> {
                dfs.findRoutes(origin, destination, departureAt, maxStay, budget, schengenOnly, paths);
            });
        }


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
}
