package aroundtheeurope.tripservice.model.dto;

import aroundtheeurope.tripservice.model.entity.FlightInfoEntity;
import aroundtheeurope.tripservice.model.entity.FoundTripEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Data Transfer Object (DTO) representing a found trip.
 * This class is used to transfer data via http endpoint,
 * representing a trip that has been found based on provided criteria.
 */
public class FoundTrip extends FoundTripPreview{

    // The schedule of the trip, consisting of a list of DepartureInfo objects
    private List<DepartureInfo> tripSchedule;

    /**
     * Default constructor for FoundTrip.
     * Initializes an empty FoundTrip object.
     */
    public FoundTrip() {}

    /**
     * Parameterized constructor for FoundTrip.
     *
     * @param totalPrice the total price of the trip
     * @param totalFlights the total number of flights in the trip
     * @param uniqueCities the number of unique cities visited
     * @param uniqueCountries the number of unique countries visited
     * @param departureAt the departure time of the first flight
     * @param arrivalAt the arrival time of the last flight
     * @param tripSchedule the schedule of the trip, represented as a list of DepartureInfo objects
     */
    public FoundTrip(
            double totalPrice,
            int totalFlights,
            int uniqueCities,
            int uniqueCountries,
            LocalDateTime departureAt,
            LocalDateTime arrivalAt,
            UUID requestId,
            List<DepartureInfo> tripSchedule
    ) {
        super(
                totalPrice,
                totalFlights,
                uniqueCities,
                uniqueCountries,
                departureAt,
                arrivalAt,
                requestId
        );
        this.tripSchedule = tripSchedule;
    }

    /**
     * Constructs a FoundTrip DTO from a FoundTripEntity.
     * This method is used to convert a persistent entity into a DTO for data transfer.
     *
     * @param foundTripEntity the FoundTripEntity object to convert
     * @return a new FoundTrip DTO object populated with data from the entity
     */
    public static FoundTrip constructFromEntity(FoundTripEntity foundTripEntity) {

        // Convert the list of FlightInfoEntity objects to a list of DepartureInfo DTOs
        List<DepartureInfo> tripSchedule = foundTripEntity.getTripSchedule().stream()
                .map(DepartureInfo::constructFromEntity)
                .toList();

        return new FoundTrip(
                foundTripEntity.getTotalPrice(),
                foundTripEntity.getTotalFlights(),
                foundTripEntity.getUniqueCities(),
                foundTripEntity.getUniqueCountries(),
                foundTripEntity.getDepartureAt(),
                foundTripEntity.getArrivalAt(),
                foundTripEntity.getRequestId(),
                tripSchedule
        );
    }

    // Getters and Setters

    public List<DepartureInfo> getTripSchedule() {
        return tripSchedule;
    }

    public void setTripSchedule(List<DepartureInfo> tripSchedule) {
        this.tripSchedule = tripSchedule;
    }
}
