package aroundtheeurope.tripservice.Services.TripSearchService;

import aroundtheeurope.tripservice.model.dto.TripRequest;
import org.springframework.http.ResponseEntity;

/**
 * Interface for defining the contract for trip search services.
 * Implementations of this interface will handle finding and storing to database trip options
 * based on the provided trip request details.
 */
public interface ITripSearchService {

    /**
     * Finds a trip based on the given TripRequest and stores it to the database.
     *
     * @param tripRequest the details of the trip being requested
     * @return a ResponseEntity containing a string message (e.g., success or error message)
     */
    ResponseEntity<String> findTrip(TripRequest tripRequest);
}
