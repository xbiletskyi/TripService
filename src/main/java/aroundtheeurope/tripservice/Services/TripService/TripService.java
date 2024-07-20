package aroundtheeurope.tripservice.Services.TripService;

import aroundtheeurope.tripservice.Models.FoundTrip;

import java.util.List;

/**
 * Interface for finding routes between airports.
 */
public interface TripService {

    /**
     * Finds routes between airports within the given criteria.
     *
     * @param origin the IATA code of the departure airport
     * @param destination the IATA code of the destination airport
     * @param departureAt the departure date
     * @param maxStay the maximum days between two flights
     * @param budget the maximum amount of money spent on trips
     * @param timeLimit the time limit in seconds for finding routes
     * @param schengenOnly if true, only includes flights within the Schengen Area
     * @return the list of found trips
     */
    List<FoundTrip> findRoute(String origin,
                              String destination,
                              String departureAt,
                              int maxStay,
                              double budget,
                              int timeLimit,
                              boolean schengenOnly);
}
