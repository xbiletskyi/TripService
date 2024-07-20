package aroundtheeurope.tripservice.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Model class representing a found trip.
 */
public class FoundTrip {
    @JsonProperty("totalPrice")
    double totalPrice;
    @JsonProperty("totalFlights")
    int totalFlights;
    @JsonProperty("uniqueCities")
    int uniqueCities;
    @JsonProperty("uniqueCountries")
    int uniqueCountries;
    @JsonProperty("departureAt")
    String departureAt;
    @JsonProperty("arrivalAt")
    String arrivalAt;
    @JsonProperty("tripSchedule")
    List<DepartureInfo> tripSchedule;

    /**
     * Constructor for FoundTrip.
     *
     * @param totalPrice      the total price of the trip
     * @param totalFlights    the total number of flights in the trip
     * @param uniqueCities    the number of unique cities visited
     * @param uniqueCountries the number of unique countries visited
     * @param departureAt     the departure date and time
     * @param arrivalAt       the arrival date and time
     * @param tripSchedule    the schedule of the trip
     */
    public FoundTrip(
            double totalPrice,
            int totalFlights,
            int uniqueCities,
            int uniqueCountries,
            String departureAt,
            String arrivalAt,
            List<DepartureInfo> tripSchedule) {
        this.totalPrice = totalPrice;
        this.totalFlights = totalFlights;
        this.uniqueCities = uniqueCities;
        this.uniqueCountries = uniqueCountries;
        this.departureAt = departureAt;
        this.arrivalAt = arrivalAt;
        this.tripSchedule = tripSchedule;
    }

    /**
     * Creates a FoundTrip object from a trip schedule (list of DepartureInfo objects).
     *
     * @param path the list of DepartureInfo representing the trip
     * @return a FoundTrip object
     */
    public static FoundTrip createFoundTrip(List<DepartureInfo> path){
        double totalPrice = 0;
        Set<String> uniqueCities = new HashSet<>();
        Set<String> uniqueCountries = new HashSet<>();
        for (DepartureInfo departureInfo : path) {
            totalPrice += departureInfo.getPrice();
            uniqueCities.add(departureInfo.getDestinationAirportCode());
            uniqueCities.add(departureInfo.getOriginAirportCode());
            uniqueCountries.add(departureInfo.getDestinationCountryCode());
            uniqueCountries.add(departureInfo.getOriginCountryCode());
        }
        return new FoundTrip(
                totalPrice,
                path.size(),
                uniqueCities.size(),
                uniqueCountries.size(),
                path.getFirst().getDepartureAt().toString(),
                path.getLast().getDepartureAt().toString(),
                path
        );
    }

    // Getters and setters
    public int getTotalFlights() {
        return totalFlights;
    }

    public void setTotalFlights(int totalFlights) {
        this.totalFlights = totalFlights;
    }

    public int getUniqueCountries() {
        return uniqueCountries;
    }

    public void setUniqueCountries(int uniqueCountries) {
        this.uniqueCountries = uniqueCountries;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getUniqueCities() {
        return uniqueCities;
    }

    public void setUniqueCities(int uniqueCities) {
        this.uniqueCities = uniqueCities;
    }

    public String getDepartureAt() {
        return departureAt;
    }

    public void setDepartureAt(String departureAt) {
        this.departureAt = departureAt;
    }

    public String getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(String arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public List<DepartureInfo> getTripSchedule() {
        return tripSchedule;
    }

    public void setTripSchedule(List<DepartureInfo> tripSchedule) {
        this.tripSchedule = tripSchedule;
    }
}