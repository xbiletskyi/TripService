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
 * Controller class to handle requests for finding routes.
 */
@RestController
@RequestMapping("/v1/trips")
public class TripsController {

    private final ITripSearchService tripService;
    private final ITripRetrieveService tripRetrieveService;

    @Autowired
    public TripsController(
            ITripSearchService tripService,
            ITripRetrieveService tripRetrieveService
            ) {
        this.tripService = tripService;
        this.tripRetrieveService = tripRetrieveService;
    }


    @PostMapping
    public ResponseEntity<String> findTrips(@RequestBody TripRequest tripRequest) {
        try {
            return tripService.findTrip(tripRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/preview")
    public ResponseEntity<List<FoundTripPreview>> getPreview(@RequestParam UUID userId) {
        try {
            return ResponseEntity.ok().body(tripRetrieveService.retrievePreview(userId));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<FoundTrip>> getTrips(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID requestId
    ) {
        List<FoundTrip> foundTrips;
        if (userId != null) {
            foundTrips = tripRetrieveService.retrieveByRequest(requestId);
        }
        else if (requestId != null) {
            foundTrips = tripRetrieveService.retrieveByUser(userId);
        }
        else{
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok().body(foundTrips);
    }
}