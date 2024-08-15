package aroundtheeurope.tripservice.Services.DepartureService;

import aroundtheeurope.tripservice.model.dto.DepartureInfo;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for retrieving departure information from a flight data service.
 * Implementations of this interface are responsible for fetching departure data
 * from the FlightService by provided parameters
 */
public interface DepartureService {

    /**
     * Retrieves departure information from the TakeFlights microservice for a specified airport, date, and date range.
     *
     * @param airportCode the IATA code of the airport
     * @param date the starting date of the departure search
     * @param daysRange the number of days to search departures from the start date
     * @param schengenOnly if true, only includes flights within the Schengen Area
     * @return a list of DepartureInfo objects containing departure details
     */
    List<DepartureInfo> retrieveDepartures(
            String airportCode,
            LocalDate date,
            int daysRange,
            boolean schengenOnly
    );
}
