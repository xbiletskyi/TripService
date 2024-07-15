package aroundtheeurope.findroute.Services;

import aroundtheeurope.findroute.Models.DepartureInfo;

import java.util.List;

/**
 * Interface for retrieving departure information.
 */
public interface DepartureService {

    /**
     * Retrieves departure information from the TakeFlights microservice.
     *
     * @param airportCode the IATA code of the airport
     * @param date the date of departure
     * @param schengenOnly if true, only includes flights within the Schengen Area
     * @return the list of DepartureInfo
     */
    List<DepartureInfo> getDepartures(String airportCode,
                                      String date,
                                      boolean schengenOnly);
}
