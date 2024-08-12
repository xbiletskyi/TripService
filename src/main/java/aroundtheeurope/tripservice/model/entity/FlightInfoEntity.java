package aroundtheeurope.tripservice.model.entity;

import aroundtheeurope.tripservice.model.dto.DepartureInfo;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "departure_info")
public class FlightInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "flight_number")
    private String flightNumber;

    @Column(name = "departure_at")
    private LocalDateTime departureAt;

    @Column(name = "origin_airport_name")
    private String originAirportName;

    @Column(name = "origin_airport_code")
    private String originAirportCode;

    @Column(name = "origin_country_code")
    private String originCountryCode;

    @Column(name = "destination_airport_name")
    private String destinationAirportName;

    @Column(name = "destination_airport_code")
    private String destinationAirportCode;

    @Column(name = "destination_country_code")
    private String destinationCountryCode;

    @Column(name = "price")
    private double price;

    @Column(name = "currency_code")
    private String currencyCode;

    @ManyToMany(mappedBy = "tripSchedule")
    private Set<FoundTripEntity> foundTrips;

    public FlightInfoEntity() {
    }

    public FlightInfoEntity(
            String flightNumber,
            LocalDateTime departureAt,
            String originAirportName,
            String originAirportCode,
            String originCountryCode,
            String destinationAirportName,
            String destinationAirportCode,
            String destinationCountryCode,
            double price,
            String currencyCode
    ) {
        this.flightNumber = flightNumber;
        this.departureAt = departureAt;
        this.originAirportName = originAirportName;
        this.originAirportCode = originAirportCode;
        this.originCountryCode = originCountryCode;
        this.destinationAirportName = destinationAirportName;
        this.destinationAirportCode = destinationAirportCode;
        this.destinationCountryCode = destinationCountryCode;
        this.price = price;
        this.currencyCode = currencyCode;
    }

    public static FlightInfoEntity mapToEntity(DepartureInfo info) {
        FlightInfoEntity entity = new FlightInfoEntity();
        entity.setFlightNumber(info.getFlightNumber());
        entity.setDepartureAt(info.getDepartureAt());
        entity.setOriginAirportName(info.getOriginAirportName());
        entity.setOriginAirportCode(info.getOriginAirportCode());
        entity.setOriginCountryCode(info.getOriginCountryCode());
        entity.setDestinationAirportName(info.getDestinationAirportName());
        entity.setDestinationAirportCode(info.getDestinationAirportCode());
        entity.setDestinationCountryCode(info.getDestinationCountryCode());
        entity.setPrice(info.getPrice());
        entity.setCurrencyCode(info.getCurrencyCode());
        return entity;
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public LocalDateTime getDepartureAt() {
        return departureAt;
    }

    public void setDepartureAt(LocalDateTime departureAt) {
        this.departureAt = departureAt;
    }

    public String getOriginAirportName() {
        return originAirportName;
    }

    public void setOriginAirportName(String originAirportName) {
        this.originAirportName = originAirportName;
    }

    public String getOriginAirportCode() {
        return originAirportCode;
    }

    public void setOriginAirportCode(String originAirportCode) {
        this.originAirportCode = originAirportCode;
    }

    public String getOriginCountryCode() {
        return originCountryCode;
    }

    public void setOriginCountryCode(String originCountryCode) {
        this.originCountryCode = originCountryCode;
    }

    public String getDestinationAirportName() {
        return destinationAirportName;
    }

    public void setDestinationAirportName(String destinationAirportName) {
        this.destinationAirportName = destinationAirportName;
    }

    public String getDestinationAirportCode() {
        return destinationAirportCode;
    }

    public void setDestinationAirportCode(String destinationAirportCode) {
        this.destinationAirportCode = destinationAirportCode;
    }

    public String getDestinationCountryCode() {
        return destinationCountryCode;
    }

    public void setDestinationCountryCode(String destinationCountryCode) {
        this.destinationCountryCode = destinationCountryCode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}

