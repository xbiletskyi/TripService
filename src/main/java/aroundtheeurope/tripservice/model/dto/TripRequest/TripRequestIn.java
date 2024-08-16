package aroundtheeurope.tripservice.model.dto.TripRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing an incoming request for trip search.
 * This class is used to encapsulate the parameters required for searching trips,
 * such as user ID, origin, budget, departure date and various constraints like stay duration and Schengen-only travel.
 */
public class TripRequestIn extends TripRequest {

    private UUID userId;

    /**
     * Default constructor for TripRequest.
     * Initializes an empty TripRequest object.
     */
    public TripRequestIn() {}

    /**
     * Parameterized constructor for TripRequest.
     *
     * @param userId the unique identifier of the user making the trip request
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
    public TripRequestIn(
            UUID userId,
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
        this.userId = userId;
    }


    // Getters and setters

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
