package aroundtheeurope.tripservice.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TripRequest {
    private UUID userId;
    private String origin;
    private String destination;
    private LocalDateTime departureAt;
    private LocalDateTime returnBefore;
    private double budget;
    private int maxStay;
    private int minStay;
    private boolean schengenOnly;
    private int timeLimitSeconds;
    private List<String> excludedAirports;


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
