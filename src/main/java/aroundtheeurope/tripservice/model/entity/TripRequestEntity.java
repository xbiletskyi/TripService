package aroundtheeurope.tripservice.model.entity;

import aroundtheeurope.tripservice.model.dto.TripRequest;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "trip_requests")
public class TripRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "origin")
    private String origin;

    @Column(name = "destination")
    private String destination;

    @Column(name = "departure_at")
    private LocalDateTime departureAt;

    @Column(name = "return_before")
    private LocalDateTime returnBefore;

    @Column(name = "budget")
    private double budget;

    @Column(name = "max_stay")
    private int maxStay;

    @Column(name = "min_stay")
    private int minStay;

    @Column(name = "schengen_only")
    private boolean schengenOnly;

    @Column(name = "time_limit_seconds")
    private int timeLimitSeconds;

    @Column(name = "excluded_airports")
    private String excludedAirports;

    public TripRequestEntity() {}

    public TripRequestEntity(TripRequest tripRequest) {
        this.userId = tripRequest.getUserId();
        this.origin = tripRequest.getOrigin();
        this.destination = tripRequest.getDestination();
        this.departureAt = tripRequest.getDepartureAt();
        this.returnBefore = tripRequest.getReturnBefore();
        this.budget = tripRequest.getBudget();
        this.maxStay = tripRequest.getMaxStay();
        this.minStay = tripRequest.getMinStay();
        this.schengenOnly = tripRequest.isSchengenOnly();
        this.timeLimitSeconds = tripRequest.getTimeLimitSeconds();
        this.excludedAirports = String.join(",", tripRequest.getExcludedAirports());
    }


    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getExcludedAirports() {
        return excludedAirports;
    }

    public void setExcludedAirports(String excludedAirports) {
        this.excludedAirports = excludedAirports;
    }
}