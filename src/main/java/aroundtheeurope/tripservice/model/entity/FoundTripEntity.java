package aroundtheeurope.tripservice.model.entity;

import aroundtheeurope.tripservice.model.dto.DepartureInfo;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "found_trips")
public class FoundTripEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "total_flights")
    private int totalFlights;

    @Column(name = "unique_cities")
    private int uniqueCities;

    @Column(name = "unique_countries")
    private int uniqueCountries;

    @Column(name = "departure_at")
    private String departureAt;

    @Column(name = "arrival_at")
    private String arrivalAt;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "request_id")
    private UUID requestId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "found_trip_id")
    private List<FlightInfoEntity> tripSchedule;

    public FoundTripEntity() {
    }

    public FoundTripEntity(
            double totalPrice,
            int totalFlights,
            int uniqueCities,
            int uniqueCountries,
            String departureAt,
            String arrivalAt,
            UUID userId,
            UUID requestId,
            List<FlightInfoEntity> tripSchedule) {
        this.totalPrice = totalPrice;
        this.totalFlights = totalFlights;
        this.uniqueCities = uniqueCities;
        this.uniqueCountries = uniqueCountries;
        this.departureAt = departureAt;
        this.arrivalAt = arrivalAt;
        this.userId = userId;
        this.requestId = requestId;
        this.tripSchedule = tripSchedule;
    }

    public static FoundTripEntity createFoundTripEntity(
            List<DepartureInfo> path,
            UUID userId,
            UUID requestId
    ) {
        double totalPrice = 0;
        Set<String> uniqueCities = new HashSet<>();
        Set<String> uniqueCountries = new HashSet<>();
        List<FlightInfoEntity> tripSchedule = new ArrayList<>();
        for (DepartureInfo departureInfo : path) {
            totalPrice += departureInfo.getPrice();
            uniqueCities.add(departureInfo.getDestinationAirportCode());
            uniqueCities.add(departureInfo.getOriginAirportCode());
            uniqueCountries.add(departureInfo.getDestinationCountryCode());
            uniqueCountries.add(departureInfo.getOriginCountryCode());
            tripSchedule.add(FlightInfoEntity.mapToEntity(departureInfo));
        }
        return new FoundTripEntity(
                totalPrice,
                path.size(),
                uniqueCities.size(),
                uniqueCountries.size(),
                path.getFirst().getDepartureAt().toString(),
                path.getLast().getDepartureAt().toString(),
                userId,
                requestId,
                tripSchedule
        );
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public List<FlightInfoEntity> getTripSchedule() {
        return tripSchedule;
    }

    public void setTripSchedule(List<FlightInfoEntity> tripSchedule) {
        this.tripSchedule = tripSchedule;
    }
}