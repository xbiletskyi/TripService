package aroundtheeurope.tripservice.Services.TripSearchService;

import aroundtheeurope.tripservice.repository.FoundTripRepository;
import aroundtheeurope.tripservice.repository.TripRequestRepository;
import aroundtheeurope.tripservice.model.dto.DepartureInfo;
import aroundtheeurope.tripservice.model.entity.FoundTripEntity;

import aroundtheeurope.tripservice.Services.TripSearchService.DFS.DFS;
import aroundtheeurope.tripservice.Services.TripSearchService.Heuristic.HeuristicAlgorithm;
import aroundtheeurope.tripservice.model.dto.TripRequest.TripRequestIn;
import aroundtheeurope.tripservice.model.entity.TripRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * Service implementation for finding routes between airports.
 * This class uses different trip-finding strategies (e.g., DFS, heuristic algorithm)
 * based on the time limit specified in the trip request. It also handles the persistence
 * of trip requests and found trips in the database.
 */
@Service
public class TripSearchService implements ITripSearchService {

    private final HeuristicAlgorithm heuristicAlgorithm;
    private final DFS dfs;
    private final FoundTripRepository foundTripRepository;
    private final TripRequestRepository tripRequestRepository;

    /**
     * Constructor for TripSearchService.
     * Initializes the service with the necessary dependencies, including trip-finding algorithms,
     * and repositories for saving trip requests and found trips.
     *
     * @param aStarAlgorithm       the heuristic algorithm used for trip finding
     * @param dfs                  the depth-first search algorithm used for trip finding
     * @param foundTripRepository  the repository for saving found trips
     * @param tripRequestRepository the repository for saving trip requests
     */
    @Autowired
    public TripSearchService(
            HeuristicAlgorithm aStarAlgorithm,
            DFS dfs,
            FoundTripRepository foundTripRepository,
            TripRequestRepository tripRequestRepository
            ) {
        this.heuristicAlgorithm = aStarAlgorithm;
        this.dfs = dfs;
        this.foundTripRepository = foundTripRepository;
        this.tripRequestRepository = tripRequestRepository;
    }

    /**
     * Finds a trip based on the provided TripRequest.
     * This method selects a trip-finding strategy based on the time limit provided in the request,
     * executes the search, and saves the found trips to the database.
     *
     * @param tripRequest the details of the trip being requested
     * @return a ResponseEntity containing a success message or an indication that no trips were found
     */
    @Override
    public ResponseEntity<String> findTrip(TripRequestIn tripRequest) {

        // Convert the TripRequest DTO to an entity and save it to the database
        TripRequestEntity tripRequestEntity = new TripRequestEntity(tripRequest);
        tripRequestEntity = tripRequestRepository.save(tripRequestEntity);

        List<List<DepartureInfo>> paths = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Choose the trip-finding strategy based on the time limit specified in the request
        TripFindingStrategy strategy;
        if (tripRequest.getTimeLimitSeconds() < 100) {
            strategy = dfs;
        } else {
            strategy = heuristicAlgorithm;
        }

        // Execute the selected strategy in a separate thread, respecting the time limit
        Future<?> future = executor.submit(() -> {
            strategy.findTrips(
                    tripRequest,
                    paths
            );
        });

        try {
            // Wait for the task to complete within the given time limit
            future.get(tripRequest.getTimeLimitSeconds(), TimeUnit.SECONDS);
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
        List<FoundTripEntity> foundTrips = new ArrayList<>();
        for (List<DepartureInfo> path : paths) {
            // Convert each found path into a FoundTripEntity and add it to the list
            FoundTripEntity foundTrip = FoundTripEntity.createFoundTripEntity(
                    path,
                    tripRequest.getUserId(),
                    tripRequestEntity.getId()
            );
            foundTrips.add(foundTrip);
        }

        // Save all found trips to the database
        if (!foundTrips.isEmpty()) {
            foundTripRepository.saveAll(foundTrips);
        }

        // Return an appropriate response based on whether any trips were found
        if (foundTrips.isEmpty()) {
            return ResponseEntity.status(NO_CONTENT).body("No trips found");
        }

        return ResponseEntity.ok("The trips have been found");
    }
}
