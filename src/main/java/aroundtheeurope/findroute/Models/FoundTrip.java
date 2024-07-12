package aroundtheeurope.findroute.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public FoundTrip(double totalPrice,
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
                path.getFirst().getDepartureAt(),
                path.getLast().getDepartureAt(),
                path
        );
    }

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
