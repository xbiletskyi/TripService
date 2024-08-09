package aroundtheeurope.tripservice.Services.TripSearchService.Heuristic;

import aroundtheeurope.tripservice.Services.TripSearchService.TripFindingStrategy;
import aroundtheeurope.tripservice.model.dto.DepartureInfo;
import aroundtheeurope.tripservice.model.AlgorithmNode;
import aroundtheeurope.tripservice.Services.DepartureService.DepartureService;
import aroundtheeurope.tripservice.model.dto.TripRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class HeuristicAlgorithm implements TripFindingStrategy {

    private final DepartureService departureService;
    private final HeuristicCalculator heuristicCalculator;

    @Autowired
    public HeuristicAlgorithm(
            DepartureService departureService,
            HeuristicCalculator heuristicCalculator
    ) {
        this.departureService = departureService;
        this.heuristicCalculator = heuristicCalculator;
    }

    public void findTrips(
            TripRequest tripRequest,
            List<List<DepartureInfo>> paths
    ) {
        PriorityQueue<AlgorithmNode> openSet = new PriorityQueue<>(
                Comparator.comparingDouble(AlgorithmNode::getHeuristic).reversed()
        );
        Set<String> uniqueCities = new HashSet<>();
        openSet.add(AlgorithmNode.createNode(
                tripRequest.getOrigin(),
                tripRequest.getDepartureAt().toString(),
                0,
                new ArrayList<>(),
                0
        ));

        while (!openSet.isEmpty() && !Thread.currentThread().isInterrupted()) {
            AlgorithmNode currentNode = openSet.poll();
            uniqueCities.add(currentNode.getAirport());

            LocalDateTime currentDate = currentNode.getDate().plusDays(tripRequest.getMinStay());
            int dayRange = tripRequest.getMaxStay() - tripRequest.getMinStay() + 1;
            List<DepartureInfo> allDepartures = departureService.retrieveDepartures(
                    currentNode.getAirport(),
                    currentDate.toLocalDate(),
                    dayRange,
                    tripRequest.isSchengenOnly()
            );

            for (DepartureInfo departureInfo : allDepartures) {
                double newPrice = departureInfo.getPrice() + currentNode.getPrice();
                LocalDateTime departureDate = departureInfo.getDepartureAt();
                if (
                        newPrice < tripRequest.getBudget()
                        && departureDate.isBefore(tripRequest.getReturnBefore())
                        && tripRequest.getExcludedAirports().stream().noneMatch(
                            airport -> airport.equals(departureInfo.getDestinationAirportCode())
                        )
                ) {
                    List<DepartureInfo> newTrip = new ArrayList<>(currentNode.getTrip());
                    newTrip.add(departureInfo);

                    if (departureInfo.getDestinationAirportCode().equals(tripRequest.getDestination()) && newTrip.size() > 2) {
                        paths.add(newTrip);
                        System.out.println("Path FOUND");
                    }

                    double currentHeuristic = heuristicCalculator.calculateHeuristic(departureInfo, currentNode);
                    AlgorithmNode nextNode = AlgorithmNode.createNode(
                            departureInfo.getDestinationAirportCode(),
                            departureInfo.getDepartureAt().toString(),
                            newPrice,
                            newTrip,
                            currentHeuristic
                    );
                    openSet.add(nextNode);
                }
            }
        }
    }
}
