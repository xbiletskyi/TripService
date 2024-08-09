package aroundtheeurope.tripservice.Services.TripSearchService.Heuristic;

import aroundtheeurope.tripservice.model.dto.DepartureInfo;
import aroundtheeurope.tripservice.model.AlgorithmNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class HeuristicCalculator {

    @Value("${RouteFinder.heuristic.priceFactorWeight}")
    double priceFactorWeight;

    @Value("${RouteFinder.heuristic.uniqueCitiesFactorWeight}")
    double uniqueCitiesFactorWeight;

    @Value("${RouteFinder.heuristic.lengthFactorWeight}")
    double lengthFactorWeight;

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
