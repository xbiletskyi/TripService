package aroundtheeurope.tripservice.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a request for trip search.
 * This class is used to encapsulate the parameters required for searching trips,
 * such as user ID, origin, budget, departure date and various constraints like stay duration and Schengen-only travel.
 */
public class TripRequest {

    private UUID userId;
    private String origin;
    private String destination;
    private LocalDateTime departureAt;
    private LocalDateTime returnBefore = LocalDateTime.parse("3000-01-01T00:00:00");
    private double budget;
    private int maxStay = 1;
    private int minStay = 1;
    private boolean schengenOnly = false;
    private int timeLimitSeconds = 100;
    private List<String> excludedAirports = new ArrayList<>();

    /**
     * Default constructor for TripRequest.
     * Initializes an empty TripRequest object.
     */
    public TripRequest() {}

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
    public TripRequest(UUID userId, String origin, String destination, LocalDateTime departureAt, LocalDateTime returnBefore, double budget, int maxStay, int minStay, boolean schengenOnly, int timeLimitSeconds, List<String> excludedAirports) {
        this.userId = userId;
        this.origin = origin;
        this.destination = destination;
        this.departureAt = departureAt;
        this.returnBefore = returnBefore;
        this.budget = budget;
        this.maxStay = maxStay;
        this.minStay = minStay;
        this.schengenOnly = schengenOnly;
        this.timeLimitSeconds = timeLimitSeconds;
        this.excludedAirports = excludedAirports;
    }

    // Getters and setters

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureAt() {
        return departureAt;
    }

    public void setDepartureAt(LocalDateTime departureAt) {
        this.departureAt = departureAt;
    }

    public LocalDateTime getReturnBefore() {
        return returnBefore;
    }

    public void setReturnBefore(LocalDateTime returnBefore) {
        this.returnBefore = returnBefore;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public int getMaxStay() {
        return maxStay;
    }

    public void setMaxStay(int maxStay) {
        this.maxStay = maxStay;
    }

    public int getMinStay() {
        return minStay;
    }

    public void setMinStay(int minStay) {
        this.minStay = minStay;
    }

    public boolean isSchengenOnly() {
        return schengenOnly;
    }

    public void setSchengenOnly(boolean schengenOnly) {
        this.schengenOnly = schengenOnly;
    }

    public int getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public void setTimeLimitSeconds(int timeLimitSeconds) {
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public List<String> getExcludedAirports() {
        return excludedAirports;
    }

    public void setExcludedAirports(List<String> excludedAirports) {
        this.excludedAirports = excludedAirports;
    }
}
