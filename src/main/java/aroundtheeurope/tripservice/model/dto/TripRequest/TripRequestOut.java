package aroundtheeurope.tripservice.model.dto.TripRequest;

import aroundtheeurope.tripservice.model.entity.TripRequestEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing the response for previous requests retrieval from the database
 */
public class TripRequestOut extends TripRequest {

    private UUID requestId;

    /**
     * Private constructor for TripRequestOut.
     * Used internally to create an instance from a TripRequestEntity.
     *
     * @param requestId the unique identifier of the trip request
     * @param origin the origin airport code
     * @param destination the destination airport code
     * @param departureAt the departure date and time for the trip
     * @param returnBefore the latest possible return date and time for the trip
     * @param budget the budget for the trip
     * @param maxStay the maximum stay duration in days
     * @param minStay the minimum stay duration in days
     * @param schengenOnly flag to indicate if the trip should be limited to the Schengen Area only
     * @param timeLimitSeconds the time limit in seconds for the search operation
     * @param excludedAirports a list of airport codes to exclude from the trip
     */
    private TripRequestOut(
            UUID requestId,
            String origin,
            String destination,
            LocalDateTime departureAt,
            LocalDateTime returnBefore,
            double budget,
            int maxStay,
            int minStay,
            boolean schengenOnly,
            int timeLimitSeconds,
            List<String> excludedAirports
    ){
        super(
                origin,
                destination,
                departureAt,
                returnBefore,
                budget,
                maxStay,
                minStay,
                schengenOnly,
                timeLimitSeconds,
                excludedAirports
        );
        this.requestId = requestId;
    }

    /**
     * Factory method to construct a TripRequestOut instance from a TripRequestEntity.
     *
     * @param requestEntity the entity containing the trip request data
     * @return a new instance of TripRequestOut based on the entity data
     */
    public static TripRequestOut constructFromEntity(TripRequestEntity requestEntity) {
        return new TripRequestOut(
                requestEntity.getId(),
                requestEntity.getOrigin(),
                requestEntity.getDestination(),
                requestEntity.getDepartureAt(),
                requestEntity.getReturnBefore(),
                requestEntity.getBudget(),
                requestEntity.getMaxStay(),
                requestEntity.getMinStay(),
                requestEntity.isSchengenOnly(),
                requestEntity.getTimeLimitSeconds(),
                Arrays.asList(requestEntity.getExcludedAirports().split(","))
        );
    }

    // Getters and setters

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }
}
