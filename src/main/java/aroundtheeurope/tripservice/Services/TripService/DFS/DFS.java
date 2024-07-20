package aroundtheeurope.tripservice.Services.TripService.DFS;


import aroundtheeurope.tripservice.Models.AlgorithmNode;
import aroundtheeurope.tripservice.Models.DepartureInfo;
import aroundtheeurope.tripservice.Services.DepartureService.DepartureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DFS {

    DepartureService departureService;

    @Autowired
    public DFS(DepartureService departureService) {
        this.departureService = departureService;
    }

    public void findTrips(
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
        // Stack to manage the nodes to visit
        Stack<AlgorithmNode> stack = new Stack<>();
        // Push the initial node onto the stack
        stack.push(new AlgorithmNode(origin, departureAt, 0, new ArrayList<>()));

        // Continue searching while there are nodes to visit and the thread is not interrupted
        while (!stack.isEmpty() && !Thread.currentThread().isInterrupted()) {
            AlgorithmNode currentNode = stack.pop();

            // List to store all departures within the given date range
            int dayRange = maxStay - minStay + 1;
            LocalDateTime currentDate = currentNode.getDate().plusDays(minStay);
            List<DepartureInfo> allDepartures = departureService.retrieveDepartures(
                    currentNode.getAirport(),
                    currentDate.toLocalDate(),
                    dayRange,
                    schengenOnly
            );
            // Sort departures by price in descending order
            allDepartures.sort(Comparator.comparing(DepartureInfo::getPrice).reversed());

            for (DepartureInfo departureInfo : allDepartures) {
                double newPrice = departureInfo.getPrice() + currentNode.getPrice();
                // Check if the new price is within the budget
                LocalDateTime departureDate = departureInfo.getDepartureAt();
                if (newPrice < maxPrice && departureDate.isBefore(returnBefore)) {
                    System.out.println("Visited node: " + departureInfo);
                    // Create a new trip path and add the current departure to it
                    List<DepartureInfo> newTrip = new ArrayList<>(currentNode.getTrip());
                    newTrip.add(departureInfo);

                    // Check if the destination is reached and the path has more than two steps
                    if (departureInfo.getDestinationAirportCode().equals(destination)
                            && currentNode.getTrip().size() > 2) {
                        paths.add(newTrip);
                        System.out.println("\n\n\n\nFOUND TRIP\n\n\n");
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
