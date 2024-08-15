package aroundtheeurope.tripservice.controller;

import aroundtheeurope.tripservice.Services.TripRetrieveService.ITripRetrieveService;
import aroundtheeurope.tripservice.Services.TripSearchService.ITripSearchService;
import aroundtheeurope.tripservice.model.dto.FoundTrip;
import aroundtheeurope.tripservice.model.dto.FoundTripPreview;
import aroundtheeurope.tripservice.model.dto.TripRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller class to handle requests related to trips, including finding, previewing,
 * and retrieving trip details. This class exposes REST endpoints for clients to interact with.
 */
@RestController
@RequestMapping("/api/v1/trips")
public class TripsController {

    private final ITripSearchService tripSearchService;
    private final ITripRetrieveService tripRetrieveService;

    /**
     * Constructor for TripsController, initializing the trip search and retrieve services.
     *
     * @param tripSearchService the service used to search for trips
     * @param tripRetrieveService the service used to retrieve trip details
     */
    @Autowired
    public TripsController(
            ITripSearchService tripSearchService,
            ITripRetrieveService tripRetrieveService
    ) {
        this.tripSearchService = tripSearchService;
        this.tripRetrieveService = tripRetrieveService;
    }

    /**
     * Endpoint to find trips based on a trip request.
     *
     * @param tripRequest the details of the trip being requested
     * @return a ResponseEntity containing the search results or an error status
     */
    @PostMapping
    public ResponseEntity<String> findTrips(@RequestBody TripRequest tripRequest) {
        try {
            // Delegates the trip search to the service layer
            return tripSearchService.findTrip(tripRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint to retrieve a preview of found trips for a specific user.
     *
     * @param userId the unique identifier of the user
     * @return a ResponseEntity containing a list of trip previews or an error status
     */
    @GetMapping("/preview")
    public ResponseEntity<List<FoundTripPreview>> getPreview(@RequestParam UUID userId) {
        try {
            // Retrieves the trip previews for the specified user
            List<FoundTripPreview> result = tripRetrieveService.retrievePreview(userId);
            if (result.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Endpoint to retrieve detailed trip information based on user ID or request ID.
     *
     * @param userId the unique identifier of the user (optional)
     * @param requestId the unique identifier of the trip request (optional)
     * @return a ResponseEntity containing a list of found trips or an error status
     */
    @GetMapping
    public ResponseEntity<List<FoundTrip>> getTrips(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID requestId
    ) {
        List<FoundTrip> foundTrips;

        // Retrieves trips based on the provided request ID or user ID
        if (userId != null) {
            foundTrips = tripRetrieveService.retrieveByUser(userId);
        } else if (requestId != null) {
            foundTrips = tripRetrieveService.retrieveByRequest(requestId);
        } else {
            // Returns a 400 Bad Request status if neither userId nor requestId is provided
            return ResponseEntity.badRequest().body(null);
        }

        if (foundTrips.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // Returns the found trips with a 200 OK status
        return ResponseEntity.ok().body(foundTrips);
    }
}
