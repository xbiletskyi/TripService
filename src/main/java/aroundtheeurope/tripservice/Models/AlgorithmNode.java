package aroundtheeurope.tripservice.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlgorithmNode {
    private String airport;
    private String date;
    private double price;
    private List<DepartureInfo> trip;
    private Set<String> uniqueCities;
    private double heuristic;

    public AlgorithmNode(String airport, String date, double price, List<DepartureInfo> trip, double heuristic, Set<String> uniqueCities) {
        this.airport = airport;
        this.date = date;
        this.price = price;
        this.trip = trip;
        this.heuristic = heuristic;
        this.uniqueCities = uniqueCities;
    }

    public AlgorithmNode(String airport, String date, double price, List<DepartureInfo> trip) {
        this.airport = airport;
        this.date = date;
        this.price = price;
        this.trip = trip;
    }

    public static AlgorithmNode createNode(String airport, String date, double price, List<DepartureInfo> trip, double heuristic) {
        // Unique cities HashSet creation
        Set<String> uniqueCities = new HashSet<>();
        for (DepartureInfo departureInfo : trip) {
            uniqueCities.add(departureInfo.getDestinationAirportCode());
        }

        // Date formatting
        String dateFormatted = "";
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(date);
            LocalDate localDate = localDateTime.toLocalDate();
            dateFormatted = localDate.toString();
        } catch (DateTimeParseException e) {
            LocalDate localDate = LocalDate.parse(date);
            dateFormatted = localDate.toString();
        }

        return new AlgorithmNode(airport, dateFormatted, price, trip, heuristic, uniqueCities);
    }


    // Getters and setters
    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
