package aroundtheeurope.tripservice.Services.TripSearchService;

import aroundtheeurope.tripservice.model.dto.TripRequest;
import org.springframework.http.ResponseEntity;

/**
 * Interface for finding routes between airports.
 */
public interface ITripSearchService {

    ResponseEntity<String> findTrip(TripRequest tripRequest);
}
