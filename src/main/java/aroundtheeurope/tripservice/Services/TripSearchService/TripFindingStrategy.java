package aroundtheeurope.tripservice.Services.TripSearchService;

import aroundtheeurope.tripservice.model.dto.DepartureInfo;
import aroundtheeurope.tripservice.model.dto.TripRequest;

import java.util.List;

/**
 * Interface for defining the strategy used to find trip routes between airports.
 * Implementations of this interface can define different algorithms or methods for trip searching,
 * such as depth-first search (DFS), heuristic search, etc.
 */
public interface TripFindingStrategy {

    /**
     * Finds trip paths based on the given TripRequest and stores valid paths in the provided list.
     *
     * @param tripRequest the details of the trip being requested
     * @param paths       the list to store valid trip paths found by the strategy
     */
    void findTrips(
            TripRequest tripRequest,
            List<List<DepartureInfo>> paths
    );
}
