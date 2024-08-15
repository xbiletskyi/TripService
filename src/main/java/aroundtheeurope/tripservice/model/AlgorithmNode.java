package aroundtheeurope.tripservice.model;

import aroundtheeurope.tripservice.model.dto.DepartureInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The AlgorithmNode class represents a node used in the search algorithms for finding trips.
 * It contains information about the current airport, date, price, the trip path taken so far,
 * unique cities visited, and the heuristic value used to prioritize nodes during the search.
 */
public class AlgorithmNode {
    private String airport;
    private LocalDateTime date;
    private double price;
    private List<DepartureInfo> trip;
    private Set<String> uniqueCities;
    private double heuristic;

    /**
     * Constructor to initialize an AlgorithmNode with all properties to be used for Heuristic algorithm.
     *
     * @param airport       the current airport code
     * @param date          the date and time at this node
     * @param price         the cumulative price of the trip so far
     * @param trip          the list of DepartureInfo objects representing the trip path
     * @param heuristic     the heuristic value used to prioritize this node in the search
     * @param uniqueCities  the set of unique cities visited so far
     */
    public AlgorithmNode(
            String airport,
            LocalDateTime date,
            double price,
            List<DepartureInfo> trip,
            double heuristic,
            Set<String> uniqueCities) {
        this.airport = airport;
        this.date = date;
        this.price = price;
        this.trip = trip;
        this.heuristic = heuristic;
        this.uniqueCities = uniqueCities;
    }

    /**
     * Constructor to initialize an AlgorithmNode without uniqueCities and heuristic.
     * This is used for DFS algorithm without heuristic consideration
     *
     * @param airport the current airport code
     * @param date    the date and time at this node
     * @param price   the cumulative price of the trip so far
     * @param trip    the list of DepartureInfo objects representing the trip path
     */
    public AlgorithmNode(
            String airport,
            LocalDateTime date,
            double price,
            List<DepartureInfo> trip) {
        this.airport = airport;
        this.date = date;
        this.price = price;
        this.trip = trip;
    }

    /**
     * Factory method to create an AlgorithmNode from string inputs and a list of trip details.
     * It handles the parsing of the date string and initializes the unique cities set.
     *
     * @param airport   the current airport code
     * @param date      the date and time as a string
     * @param price     the cumulative price of the trip so far
     * @param trip      the list of DepartureInfo objects representing the trip path
     * @param heuristic the heuristic value used to prioritize this node in the search
     * @return a new AlgorithmNode object
     */
    public static AlgorithmNode createNode(
            String airport,
            String date,
            double price,
            List<DepartureInfo> trip,
            double heuristic) {
        Set<String> uniqueCities = new HashSet<>();
        for (DepartureInfo departureInfo : trip) {
            uniqueCities.add(departureInfo.getDestinationAirportCode());
        }
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(date);
        } catch (DateTimeParseException e) {
            LocalDate localDate = LocalDate.parse(date);
            localDateTime = localDate.atStartOfDay();
        }

        return new AlgorithmNode(airport, localDateTime, price, trip, heuristic, uniqueCities);
    }

    // Getters and setters

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<DepartureInfo> getTrip() {
        return trip;
    }

    public void setTrip(List<DepartureInfo> trip) {
        this.trip = trip;
    }

    public Set<String> getUniqueCities() {
        return uniqueCities;
    }

    public void setUniqueCities(Set<String> uniqueCities) {
        this.uniqueCities = uniqueCities;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }
}