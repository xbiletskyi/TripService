package aroundtheeurope.tripservice.controller;

import aroundtheeurope.tripservice.Services.RetrieveService.IRetrieveService;
import aroundtheeurope.tripservice.Services.TripSearchService.ITripSearchService;
import aroundtheeurope.tripservice.model.dto.FoundTrip;
import aroundtheeurope.tripservice.model.dto.FoundTripPreview;
import aroundtheeurope.tripservice.model.dto.TripRequest.TripRequestIn;
import aroundtheeurope.tripservice.model.dto.TripRequest.TripRequestOut;
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
    private final IRetrieveService tripRetrieveService;

    /**
     * Constructor for TripsController, initializing the trip search and retrieve services.
     *
     * @param tripSearchService the service used to search for trips
     * @param tripRetrieveService the service used to retrieve trip details
     */
    @Autowired
    public TripsController(
            ITripSearchService tripSearchService,
            IRetrieveService tripRetrieveService
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
    public ResponseEntity<String> findTrips(@RequestBody TripRequestIn tripRequest) {
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
     * Endpoint to retrieve detailed trip information based on user ID.
     *
     * @param userId the unique identifier of the user
     * @return a ResponseEntity containing a list of found trips or an error status
     */
    @GetMapping
    public ResponseEntity<List<FoundTrip>> getTripsByUser(@RequestParam(required = false) UUID userId) {
        List<FoundTrip> foundTrips = tripRetrieveService.retrieveByUser(userId);

        if (foundTrips.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(foundTrips);
    }

    /**
     * Endpoint to retrieve detailed information about specific request's result
     *
     * @param requestId the unique identifier of previous search request
     * @return a ResponseEntity containing a list of found trips by the request's parameters
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<List<FoundTrip>> getTripsByRequest(@PathVariable UUID requestId) {
        List<FoundTrip> foundTrips = tripRetrieveService.retrieveByRequest(requestId);

        if (foundTrips.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(foundTrips);
    }

    /**
     * Endpoint to retrieve requests' details issued by the user
     *
     * @param userId a unique identifier of a user
     * @return requests' details issued by the user
     */
    @GetMapping("/requests")
    public ResponseEntity<List<TripRequestOut>> getRequests(@RequestParam UUID userId) {
        List<TripRequestOut> requests = tripRetrieveService.retrieveRequestByUser(userId);
        if (requests.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(requests);
    }
}
