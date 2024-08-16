package aroundtheeurope.tripservice.model.dto.TripRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a basic request for a trip search, containing the core parameters
 * like origin, destination, departure time, and other optional constraints.
 */
public class TripRequest {

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

    // Default constructor
    public TripRequest() {}

    // Parameterized constructor to initialize all fields.
    public TripRequest(
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
    ) {
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
