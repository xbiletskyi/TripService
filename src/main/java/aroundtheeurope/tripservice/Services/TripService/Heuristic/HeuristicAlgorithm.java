package aroundtheeurope.tripservice.Services.TripService.Heuristic;

import aroundtheeurope.tripservice.Models.DepartureInfo;
import aroundtheeurope.tripservice.Models.AlgorithmNode;
import aroundtheeurope.tripservice.Services.DepartureService.DepartureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class HeuristicAlgorithm {

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

    public List<List<DepartureInfo>> findTrips(
            String origin,
            String destination,
            LocalDateTime departureAt,
            LocalDateTime returnBefore,
            int maxStay,
            int minStay,
            double maxPrice,
            boolean schengenOnly,
            List<List<DepartureInfo>> paths
    ) {
        PriorityQueue<AlgorithmNode> openSet = new PriorityQueue<>(
                Comparator.comparingDouble(AlgorithmNode::getHeuristic).reversed()
        );
        Set<String> uniqueCities = new HashSet<>();
        openSet.add(AlgorithmNode.createNode(
                origin,
                departureAt.toString(),
                0,
                new ArrayList<>(),
                0
        ));

        while (!openSet.isEmpty() && !Thread.currentThread().isInterrupted()) {
            AlgorithmNode currentNode = openSet.poll();
            uniqueCities.add(currentNode.getAirport());

            LocalDateTime currentDate = currentNode.getDate().plusDays(minStay);
            int dayRange = maxStay - minStay + 1;
            List<DepartureInfo> allDepartures = departureService.retrieveDepartures(
                    currentNode.getAirport(),
                    currentDate.toLocalDate(),
                    dayRange,
                    schengenOnly
            );

            for (DepartureInfo departureInfo : allDepartures) {
                double newPrice = departureInfo.getPrice() + currentNode.getPrice();
                LocalDateTime departureDate = departureInfo.getDepartureAt();
                if (newPrice < maxPrice && departureDate.isBefore(returnBefore)) {
                    List<DepartureInfo> newTrip = new ArrayList<>(currentNode.getTrip());
                    newTrip.add(departureInfo);

                    if (departureInfo.getDestinationAirportCode().equals(destination) && newTrip.size() > 2) {
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
        return paths;
    }
}
