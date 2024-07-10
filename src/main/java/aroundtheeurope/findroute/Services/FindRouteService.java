package aroundtheeurope.findroute.Services;

import aroundtheeurope.findroute.Models.DepartureInfo;
import aroundtheeurope.findroute.Models.FoundTrip;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.*;

@Service
public class FindRouteService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${TakeFlights.url}")
    String takeFlightsUrl;

    @Value("${spring.security.user.name}")
    String authUsername;

    @Value("${spring.security.user.password}")
    String authPassword;

    @Autowired
    public FindRouteService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<FoundTrip> findRoute(String origin, String destination, String departureAt, int maxStay, double budget, int timeLimit){
        List<List<DepartureInfo>> paths = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //#############################
        System.out.println("Started search from " + origin + " to " + destination + "at " + departureAt);

        Future<?> future = executor.submit( () -> {
            DFS(origin, destination, departureAt, maxStay, budget, paths);
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
            double totalPrice = 0;
            Set<String> uniqueCities = new HashSet<>();
            for (DepartureInfo departureInfo : path) {
                totalPrice += departureInfo.getPrice();
                uniqueCities.add(departureInfo.getDestinationAirportCode());
                uniqueCities.add(departureInfo.getOriginAirportCode());
            }
            FoundTrip foundTrip = new FoundTrip(totalPrice,
                    uniqueCities.size(),
                    path.getFirst().getDepartureAt(),
                    path.getLast().getDepartureAt(),
                    path
            );
            foundTrips.add(foundTrip);
        }
        foundTrips.sort(Comparator.comparingDouble(FoundTrip::getTotalPrice).reversed().thenComparingInt(FoundTrip::getUniqueCities));
        return foundTrips;
    }

    private void DFS(String source, String destination, String departureAt, int dayRange, double maxPrice,
                     List<List<DepartureInfo>> paths){
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
            for (int i = 0; i < dayRange; i++) {
                LocalDate dateToCheck = initialDate.plusDays(i);
                List<DepartureInfo> dailyDepartures = getDepartures(currentNode.airport, dateToCheck.toString());
                //#####################
                System.out.println("Received departures from " + currentNode.airport + " at " + dateToCheck);

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

//                    boolean cityVisited = false;
//                    if (currentNode.trip.size() > 4) {
//                        cityVisited = currentNode.trip.subList(currentNode.trip.size() - 4, currentNode.trip.size()).stream()
//                                .anyMatch(info -> info.getDestinationAirportCode().equals(departureInfo.getDestinationAirportCode()));
//                    } else {
//                        cityVisited = currentNode.trip.stream()
//                                .anyMatch(info -> info.getDestinationAirportCode().equals(departureInfo.getDestinationAirportCode()));
//                    }

                    //###########
                    System.out.println("Current trip is: ");
                    for (DepartureInfo newTripInfo : newTrip) {
                        System.out.println("    " + newTripInfo);
                    }

                    if (departureInfo.getDestinationAirportCode().equals(destination) && currentNode.trip.size() > 2) {
                        paths.add(newTrip);
                    }
//                    else if (!cityVisited){
                        stack.push(new Node(departureInfo.getDestinationAirportCode(), departureInfo.getDepartureAt(),
                                newPrice, newTrip));
//                    }
                }
            }
        }
    }

    private List<DepartureInfo> getDepartures(String airportCode, String date){
        String url = takeFlightsUrl + "origin=" + airportCode + "&departure_at=" + date;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        String auth = authUsername + ":" + authPassword;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeader = "Basic " + encodedAuth;
        headers.set("Authorization", authHeader);

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        List<DepartureInfo> departureInfos = null;
        try{
            departureInfos = objectMapper.readValue(response.getBody(), new TypeReference<List<DepartureInfo>>(){});
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return departureInfos;
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
