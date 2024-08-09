package aroundtheeurope.tripservice.Services.TripSearchService.DFS;


import aroundtheeurope.tripservice.model.AlgorithmNode;
import aroundtheeurope.tripservice.Services.TripSearchService.TripFindingStrategy;
import aroundtheeurope.tripservice.model.dto.DepartureInfo;
import aroundtheeurope.tripservice.Services.DepartureService.DepartureService;
import aroundtheeurope.tripservice.model.dto.TripRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DFS implements TripFindingStrategy {

    DepartureService departureService;

    @Autowired
    public DFS(DepartureService departureService) {
        this.departureService = departureService;
    }

    public void findTrips(
            TripRequest tripRequest,
            List<List<DepartureInfo>> paths
    ) {
        // Stack to manage the nodes to visit
        Stack<AlgorithmNode> stack = new Stack<>();
        // Push the initial node onto the stack
        stack.push(new AlgorithmNode(
                tripRequest.getOrigin(),
                tripRequest.getDepartureAt(),
                0, new ArrayList<>()
        ));

        // Continue searching while there are nodes to visit and the thread is not interrupted
        while (!stack.isEmpty() && !Thread.currentThread().isInterrupted()) {
            AlgorithmNode currentNode = stack.pop();

            // List to store all departures within the given date range
            int dayRange = tripRequest.getMaxStay() - tripRequest.getMinStay() + 1;
            LocalDateTime currentDate = currentNode.getDate().plusDays(tripRequest.getMinStay());
            List<DepartureInfo> allDepartures = departureService.retrieveDepartures(
                    currentNode.getAirport(),
                    currentDate.toLocalDate(),
                    dayRange,
                    tripRequest.isSchengenOnly()
            );
            // Sort departures by price in descending order
            allDepartures.sort(Comparator.comparing(DepartureInfo::getPrice).reversed());

            for (DepartureInfo departureInfo : allDepartures) {
                double newPrice = departureInfo.getPrice() + currentNode.getPrice();
                // Check if the new price is within the budget
                LocalDateTime departureDate = departureInfo.getDepartureAt();
                if (
                        newPrice < tripRequest.getBudget()
                        && departureDate.isBefore(tripRequest.getReturnBefore())
                        && tripRequest.getExcludedAirports().stream().noneMatch(
                                airport -> airport.equals(departureInfo.getDestinationAirportCode())
                        )
                ) {
                    // Create a new trip path and add the current departure to it
                    List<DepartureInfo> newTrip = new ArrayList<>(currentNode.getTrip());
                    newTrip.add(departureInfo);

                    // Check if the destination is reached and the path has more than two steps
                    if (departureInfo.getDestinationAirportCode().equals(tripRequest.getDestination())
                            && currentNode.getTrip().size() > 2) {
                        paths.add(newTrip);
                    }
                    // Push the next node onto the stack
                    stack.push(new AlgorithmNode(
                            departureInfo.getDestinationAirportCode(),
                            departureInfo.getDepartureAt(),
                            newPrice,
                            newTrip
                    ));
                }
            }
        }
    }

}
