package aroundtheeurope.tripservice.Services.TripSearchService;

import aroundtheeurope.tripservice.Repositories.FoundTripRepository;
import aroundtheeurope.tripservice.Repositories.TripRequestRepository;
import aroundtheeurope.tripservice.model.dto.DepartureInfo;
import aroundtheeurope.tripservice.model.entity.FoundTripEntity;

import aroundtheeurope.tripservice.Services.TripSearchService.DFS.DFS;
import aroundtheeurope.tripservice.Services.TripSearchService.Heuristic.HeuristicAlgorithm;
import aroundtheeurope.tripservice.model.dto.TripRequest;
import aroundtheeurope.tripservice.model.entity.TripRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

/**
 * Service implementation for finding routes between airports.
 */
@Service
public class TripSearchService implements ITripSearchService {

    private final HeuristicAlgorithm heuristicAlgorithm;
    private final DFS dfs;
    private final FoundTripRepository foundTripRepository;
    private final TripRequestRepository tripRequestRepository;

    /**
     * Constructor for RouteFinderImpl.
     *
     * @param aStarAlgorithm service to apply algorithm on input
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

    @Override
    public ResponseEntity<String> findTrip(TripRequest tripRequest) {

        TripRequestEntity tripRequestEntity = new TripRequestEntity(tripRequest);
        tripRequestEntity = tripRequestRepository.save(tripRequestEntity);

        List<List<DepartureInfo>> paths = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        TripFindingStrategy strategy;
        if (tripRequest.getTimeLimitSeconds() < 100) {
            strategy = dfs;
        } else {
            strategy = heuristicAlgorithm;
        }

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
            FoundTripEntity foundTrip = FoundTripEntity.createFoundTripEntity(
                    path,
                    tripRequest.getUserId(),
                    tripRequestEntity.getId()
            );
            foundTrips.add(foundTrip);
        }

        if (!foundTrips.isEmpty()) {
            foundTripRepository.saveAll(foundTrips);
        }

        if (foundTrips.isEmpty()) {
            return ResponseEntity.status(NO_CONTENT).body("No trips found");
        }

        return ResponseEntity.ok("The trips have been found");
    }
}
