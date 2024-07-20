package aroundtheeurope.tripservice.Controllers;

import aroundtheeurope.tripservice.Models.FoundTrip;
import aroundtheeurope.tripservice.Services.TripService.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller class to handle requests for finding routes.
 */
@RestController
public class TripsController {
    @Autowired
    private final TripService tripService;

    /**
     * Constructor for FindRouteController.
     *
     * @param tripService the service used to find routes
     */
    @Autowired
    public TripsController(TripService tripService) {
        this.tripService = tripService;
    }

    /**
     * Endpoint to find routes based on the given parameters.
     *
     * @param origin the IATA code of the departure airport
     * @param destination the IATA code of the destination airport
     * @param departureAt the departure date
     * @param budget the maximum budget for the trip
     * @param maxStay the maximum days between two flights
     * @param schengenOnly if true, only includes flights within the Schengen Area
     * @param timeLimit the time limit in seconds for finding routes
     * @return a ResponseEntity containing the list of found trips
     */
    @GetMapping("/v1/trips")
    public ResponseEntity<List<FoundTrip>> findRoute(
             @RequestParam("origin") String origin,
             @RequestParam(value = "destination", required = false) String destination,
             @RequestParam("departureAt") String departureAt,
             @RequestParam(value = "returnBefore", defaultValue = "3000-01-01") String returnBefore,
             @RequestParam("budget") double budget,
             @RequestParam(value = "maxStay", defaultValue = "1") int maxStay,
             @RequestParam(value = "minStay", defaultValue = "1") int minStay,
             @RequestParam(value = "schengenOnly", defaultValue = "false") boolean schengenOnly,
             @RequestParam(value = "timeLimitSeconds", defaultValue = "10") int timeLimit
            ) {
        if (destination == null) {
            destination = origin;
        }
        try {
            // Validate the parameters
            if (
                    origin == null || origin.isEmpty() ||
                    destination == null || destination.isEmpty() ||
                    departureAt == null || departureAt.isEmpty()
            ) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // Find routes using the route finder service
            List<FoundTrip> routes = tripService.findTrip(
                    origin,
                    destination,
                    LocalDate.parse(departureAt).atStartOfDay(),
                    LocalDate.parse(returnBefore).atStartOfDay(),
                    maxStay,
                    minStay,
                    budget,
                    schengenOnly,
                    timeLimit
            );

            // Return no content if no routes are found
            if (routes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }

            // Return the found routes
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}