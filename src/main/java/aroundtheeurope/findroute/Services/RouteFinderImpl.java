package aroundtheeurope.findroute.Services;

import aroundtheeurope.findroute.Models.DepartureInfo;
import aroundtheeurope.findroute.Models.FoundTrip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.*;

@Service
public class RouteFinderImpl implements RouteFinder{
    private final DepartureService departureService;

    @Autowired
    public RouteFinderImpl(DepartureService departureService) {
        this.departureService = departureService;
    }

    @Override
    public List<FoundTrip> findRoute(String origin,
                                     String destination,
                                     String departureAt,
                                     int maxStay,
                                     double budget,
                                     int timeLimit,
                                     boolean schengenOnly){

        List<List<DepartureInfo>> paths = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit( () -> {
            DFS(origin, destination, departureAt, maxStay, budget, paths, schengenOnly);
        });

        try{
            future.get(timeLimit, TimeUnit.SECONDS);
        }
        catch (TimeoutException e) {
            future.cancel(true);
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        finally{
            executor.shutdown();
        }

        List<FoundTrip> foundTrips = new ArrayList<>();
        for (List<DepartureInfo> path : paths) {
            FoundTrip foundTrip = FoundTrip.createFoundTrip(path);
            foundTrips.add(foundTrip);
        }
        foundTrips.sort(
                Comparator.comparingInt(FoundTrip::getUniqueCities).reversed()
                        .thenComparingDouble(FoundTrip::getTotalPrice)
        );
        return foundTrips;
    }

    private void DFS(String source, String destination, String departureAt, int dayRange, double maxPrice,
                     List<List<DepartureInfo>> paths, boolean schengenOnly){
        Stack<Node> stack = new Stack<>();
        Set<String> visited = new HashSet<>();
        stack.push(new Node(source, departureAt, 0, new ArrayList<>()));

        while(!stack.isEmpty() && !Thread.currentThread().isInterrupted()){
            Node currentNode = stack.pop();
            String currentNodeKey = currentNode.airport + ":" + currentNode.date;

            if (visited.contains(currentNodeKey)) {
                continue;
            }
            visited.add(currentNodeKey);

            List<DepartureInfo> allDepartures = new ArrayList<>();
            LocalDate initialDate = LocalDate.parse(currentNode.date, DateTimeFormatter.ISO_LOCAL_DATE);
            initialDate = initialDate.plusDays(1);
            for (int i = 0; i < dayRange; i++) {
                LocalDate dateToCheck = initialDate.plusDays(i);
                List<DepartureInfo> dailyDepartures = departureService.getDepartures(currentNode.airport, dateToCheck.toString(), schengenOnly);
                if (dailyDepartures != null){
                    allDepartures.addAll(dailyDepartures);
                }
            }
            allDepartures.sort(Comparator.comparing(DepartureInfo::getPrice).reversed());


            for (DepartureInfo departureInfo : allDepartures) {
                double newPrice = departureInfo.getPrice() + currentNode.price;
                if (newPrice < maxPrice) {
                    List<DepartureInfo> newTrip = new ArrayList<>(currentNode.trip);
                    newTrip.add(departureInfo);

                    if (departureInfo.getDestinationAirportCode().equals(destination) && currentNode.trip.size() > 2) {
                        paths.add(newTrip);
                    }
                    stack.push(new Node(departureInfo.getDestinationAirportCode(), departureInfo.getDepartureAt(),
                            newPrice, newTrip));
                }
            }
        }
    }

    private static class Node {
        String airport;
        String date;
        double price;
        List<DepartureInfo> trip;

        Node(String airport, String date, double price, List<DepartureInfo> trip) {
            this.airport = airport;
            this.date = formatDate(date);
            this.price = price;
            this.trip = trip;
        }

        private String formatDate(String dateTime) {
            try{
                LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
                LocalDate localDate = localDateTime.toLocalDate();
                return localDate.toString();
            } catch (DateTimeParseException e){
                LocalDate localDate = LocalDate.parse(dateTime);
                return localDate.toString();
            }
        }
    }
}
