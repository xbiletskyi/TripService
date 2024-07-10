package aroundtheeurope.findroute.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FoundTrip {
    @JsonProperty("totalPrice")
    double totalPrice;
    @JsonProperty("uniqueCities")
    int uniqueCities;
    @JsonProperty("departureAt")
    String departureAt;
    @JsonProperty("arrivalAt")
    String arrivalAt;
    @JsonProperty("tripSchedule")
    List<DepartureInfo> tripSchedule;

    public FoundTrip(double totalPrice,
                     int uniqueCities,
                     String departureAt,
                     String arrivalAt,
                     List<DepartureInfo> tripSchedule) {
        this.totalPrice = totalPrice;
        this.uniqueCities = uniqueCities;
        this.departureAt = departureAt;
        this.arrivalAt = arrivalAt;
        this.tripSchedule = tripSchedule;
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
