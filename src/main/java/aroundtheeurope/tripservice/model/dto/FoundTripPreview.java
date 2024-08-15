package aroundtheeurope.tripservice.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a preview of a found trip.
 * This class is used to transfer summary data about a trip between different layers of the application,
 * focusing on key information such as total price, number of flights, unique cities/countries, and timing.
 */
public class FoundTripPreview {
    private double totalPrice;
    private int totalFlights;
    private int uniqueCities;
    private int uniqueCountries;
    private LocalDateTime departureAt;
    private LocalDateTime arrivalAt;
    private UUID requestId;

    /**
     * Default constructor for FoundTripPreview.
     * Initializes an empty FoundTripPreview object.
     */
    public FoundTripPreview() {}

    /**
     * Parameterized constructor for FoundTripPreview.
     *
     * @param totalPrice the total price of the trip
     * @param totalFlights the total number of flights in the trip
     * @param uniqueCities the number of unique cities visited
     * @param uniqueCountries the number of unique countries visited
     * @param departureAt the departure time of the first flight
     * @param arrivalAt the arrival time of the last flight
     */
    public FoundTripPreview(
            double totalPrice,
            int totalFlights,
            int uniqueCities,
            int uniqueCountries,
            LocalDateTime departureAt,
            LocalDateTime arrivalAt,
            UUID requestId
    ) {
        this.totalPrice = totalPrice;
        this.totalFlights = totalFlights;
        this.uniqueCities = uniqueCities;
        this.uniqueCountries = uniqueCountries;
        this.departureAt = departureAt;
        this.arrivalAt = arrivalAt;
        this.requestId = requestId;
    }

    // Getters and setters


    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalFlights() {
        return totalFlights;
    }

    public void setTotalFlights(int totalFlights) {
        this.totalFlights = totalFlights;
    }

    public int getUniqueCities() {
        return uniqueCities;
    }

    public void setUniqueCities(int uniqueCities) {
        this.uniqueCities = uniqueCities;
    }

    public int getUniqueCountries() {
        return uniqueCountries;
    }

    public void setUniqueCountries(int uniqueCountries) {
        this.uniqueCountries = uniqueCountries;
    }

    public LocalDateTime getDepartureAt() {
        return departureAt;
    }

    public void setDepartureAt(LocalDateTime departureAt) {
        this.departureAt = departureAt;
    }

    public LocalDateTime getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(LocalDateTime arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }
}
