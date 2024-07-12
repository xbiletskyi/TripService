package aroundtheeurope.findroute.Services;

import aroundtheeurope.findroute.Models.DepartureInfo;
import aroundtheeurope.findroute.Models.FoundTrip;

import java.util.List;

public interface RouteFinder {
    List<FoundTrip> findRoute(String origin,
                              String destination,
                              String departureAt,
                              int maxStay,
                              double budget,
                              int timeLimit,
                              boolean schengenOnly);
}
