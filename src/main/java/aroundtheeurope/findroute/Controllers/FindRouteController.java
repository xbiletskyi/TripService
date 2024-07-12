package aroundtheeurope.findroute.Controllers;


import aroundtheeurope.findroute.Models.FoundTrip;
import aroundtheeurope.findroute.Services.RouteFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
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
        List<FoundTrip> routes = routeFinder.findRoute(origin, destination, departureAt, maxStay,
                budget, timeLimit, schengenOnly);
        return ResponseEntity.ok(routes);
    }
}
