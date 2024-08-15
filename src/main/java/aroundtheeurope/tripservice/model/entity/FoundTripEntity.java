package aroundtheeurope.tripservice.model.entity;

import aroundtheeurope.tripservice.model.dto.DepartureInfo;
import jakarta.persistence.*;

import java.time.LocalDateTime;
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
    private LocalDateTime departureAt;

    @Column(name = "arrival_at")
    private LocalDateTime arrivalAt;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "request_id")
    private UUID requestId;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "found_trip_schedule",
            joinColumns = @JoinColumn(name = "found_trip_id"),
            inverseJoinColumns = @JoinColumn(name = "departure_info_id")
    )
    private List<FlightInfoEntity> tripSchedule;

    /**
     * Default constructor for FoundTripEntity.
     * Initializes an empty FoundTripEntity object.
     */
    public FoundTripEntity() {
    }

    /**
     * Parameterized constructor for FoundTripEntity.
     *
     * @param totalPrice      the total price of the trip
     * @param totalFlights    the total number of flights in the trip
     * @param uniqueCities    the number of unique cities visited during the trip
     * @param uniqueCountries the number of unique countries visited during the trip
     * @param departureAt     the departure time of the first flight
     * @param arrivalAt       the arrival time of the last flight
     * @param userId          the ID of the user who made the trip request
     * @param requestId       the ID of the trip request
     * @param tripSchedule    the schedule of the trip, represented as a list of FlightInfoEntity objects
     */
    public FoundTripEntity(
            double totalPrice,
            int totalFlights,
            int uniqueCities,
            int uniqueCountries,
            LocalDateTime departureAt,
            LocalDateTime arrivalAt,
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

    /**
     * Creates a FoundTripEntity from a list of DepartureInfo objects.
     * This method calculates the total price, number of unique cities/countries,
     * and maps the trip schedule to FlightInfoEntity objects.
     *
     * @param path      the list of DepartureInfo objects representing the trip path
     * @param userId    the ID of the user who made the trip request
     * @param requestId the ID of the trip request
     * @return a FoundTripEntity object populated with calculated and mapped data
     */
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
                path.getFirst().getDepartureAt(),
                path.getLast().getDepartureAt(),
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
