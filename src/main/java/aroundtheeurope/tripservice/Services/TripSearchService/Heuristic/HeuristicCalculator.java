package aroundtheeurope.tripservice.Services.TripSearchService.Heuristic;

import aroundtheeurope.tripservice.model.dto.DepartureInfo;
import aroundtheeurope.tripservice.model.AlgorithmNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * HeuristicCalculator is a service that calculates the heuristic value for an AlgorithmNode.
 * The heuristic value is used to prioritize nodes during the trip-finding process, based on factors like price and unique cities.
 */
@Service
public class HeuristicCalculator {

    @Value("${RouteFinder.heuristic.priceFactorWeight}")
    double priceFactorWeight;

    @Value("${RouteFinder.heuristic.uniqueCitiesFactorWeight}")
    double uniqueCitiesFactorWeight;

    @Value("${RouteFinder.heuristic.lengthFactorWeight}")
    double lengthFactorWeight;

    /**
     * Calculates the heuristic value for a given node in the algorithm.
     * The heuristic combines factors such as price, the number of unique cities visited, and the length of the trip.
     *
     * @param departureInfo the departure information of the current leg of the trip
     * @param newNode       the node representing the current state of the trip in the algorithm
     * @return the calculated heuristic value for the node
     */
    public double calculateHeuristic(
            DepartureInfo departureInfo,
            AlgorithmNode newNode
    ) {
        double priceFactor = newNode.getPrice() + departureInfo.getPrice();
        double uniqueCitiesFactor = newNode.getUniqueCities().contains(departureInfo.getDestinationAirportCode())
                ? 0 : uniqueCitiesFactorWeight;
        double lengthFactor = newNode.getUniqueCities().size();
        return (priceFactorWeight / priceFactor)
                + uniqueCitiesFactor
                + (lengthFactor * lengthFactorWeight);
    }
}
