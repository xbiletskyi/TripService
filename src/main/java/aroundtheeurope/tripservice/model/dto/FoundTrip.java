package aroundtheeurope.tripservice.model.dto;

import aroundtheeurope.tripservice.model.entity.FlightInfoEntity;
import aroundtheeurope.tripservice.model.entity.FoundTripEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FoundTrip {

    private double totalPrice;
    private int totalFlights;
    private int uniqueCities;
    private int uniqueCountries;
    private LocalDateTime departureAt;
    private LocalDateTime arrivalAt;
    private List<DepartureInfo> tripSchedule;

    public FoundTrip() {}

    public FoundTrip(
            double totalPrice,
            int totalFlights,
            int uniqueCities,
            int uniqueCountries,
            LocalDateTime departureAt,
            LocalDateTime arrivalAt,
            List<DepartureInfo> tripSchedule
    ) {
        this.totalPrice = totalPrice;
        this.totalFlights = totalFlights;
        this.uniqueCities = uniqueCities;
        this.uniqueCountries = uniqueCountries;
        this.departureAt = departureAt;
        this.arrivalAt = arrivalAt;
        this.tripSchedule = tripSchedule;
    }

    public static FoundTrip constructFromEntity(FoundTripEntity foundTripEntity) {

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
                tripSchedule
        );
    }
    // Getters and Setters

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

    public List<DepartureInfo> getTripSchedule() {
        return tripSchedule;
    }

    public void setTripSchedule(List<DepartureInfo> tripSchedule) {
        this.tripSchedule = tripSchedule;
    }
}
