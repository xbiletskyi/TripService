package aroundtheeurope.tripservice.Services.DepartureService;

import aroundtheeurope.tripservice.Models.DepartureInfo;

import java.util.List;

/**
 * Interface for retrieving departure information.
 */
public interface DepartureService {
    /**
     * Retrieves departure information from the TakeFlights microservice on the given period of days.
     *
     * @param airportCode the IATA code of the airport
     * @param date the date of departure
     * @param daysRange number of days in date range
     * @param schengenOnly if true, only includes flights within the Schengen Area
     * @return the list of DepartureInfo
     */
    List<DepartureInfo> retrieveDepartures(String airportCode,
                                           String date,
                                           int daysRange,
                                           boolean schengenOnly);
}
