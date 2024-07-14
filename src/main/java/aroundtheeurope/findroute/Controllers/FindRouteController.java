package aroundtheeurope.findroute.Controllers;

import aroundtheeurope.findroute.Models.FoundTrip;
import aroundtheeurope.findroute.Services.RouteFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FindRouteController {
    @Autowired
    private final RouteFinder routeFinder;

    @Autowired
    public FindRouteController(RouteFinder routeFinder) {
        this.routeFinder = routeFinder;
    }

    @GetMapping("/findroute")
    public ResponseEntity<List<FoundTrip>> findRoute(@RequestParam("origin") String origin,
                                                     @RequestParam("destination") String destination,
                                                     @RequestParam("departureAt") String departureAt,
                                                     @RequestParam("budget") double budget,
                                                     @RequestParam("maxStay") int maxStay,
                                                     @RequestParam(value = "schengenOnly", defaultValue = "false") boolean schengenOnly,
                                                     @RequestParam(value = "timeLimitSeconds", defaultValue = "10") int timeLimit) {
        try {
            if (origin == null || origin.isEmpty() ||
                    destination == null || destination.isEmpty() ||
                    departureAt == null || departureAt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            List<FoundTrip> routes = routeFinder.findRoute(origin, destination, departureAt, maxStay, budget, timeLimit, schengenOnly);

            if (routes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            }

            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}