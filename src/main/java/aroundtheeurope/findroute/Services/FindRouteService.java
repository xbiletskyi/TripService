package aroundtheeurope.findroute.Services;

import aroundtheeurope.findroute.Models.DepartureInfo;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.*;

@Service
public class FindRouteService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${TakeFlights.url}")
    String takeFlightsUrl;

    @Autowired
    public FindRouteService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String findRoute(String source, String destination, String departureAt, int dayRange, int maxPrice, int timeLimit){
        List<List<DepartureInfo>> paths = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit( () -> {
            for (int depth = 1; ; depth++){
                IDFS(source, destination, departureAt, dayRange, maxPrice, depth, paths);
            }
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
        return paths.toString();
    }

    private void IDFS (String source, String destination, String departureAt, int dayRange, double maxPrice,
                       int depthLimit, List<List<DepartureInfo>> paths){
        Stack<Node> stack = new Stack<>();
        stack.push(new Node(source, departureAt, 0, new ArrayList<>(), depthLimit));

        while(!stack.isEmpty() && !Thread.currentThread().isInterrupted()){
            Node currentNode = stack.pop();
            if (currentNode.depth == 0) {
                continue;
            }

            List<DepartureInfo> allDepartures = new ArrayList<>();
            LocalDate initialDate = LocalDate.parse(currentNode.date);
            for (int i = 0; i < dayRange; i++) {
                LocalDate dateToCheck = initialDate.plusDays(i);
                List<DepartureInfo> dailyDepartures = getDepartures(currentNode.airport, dateToCheck.toString());
                if (dailyDepartures != null){
                    allDepartures.addAll(dailyDepartures);
                }
            }
            allDepartures.sort(Comparator.comparing(DepartureInfo::getPrice));

            for (DepartureInfo departureInfo : allDepartures) {
                double newPrice = departureInfo.getPrice() + currentNode.price;
                if (newPrice < maxPrice) {
                    List<DepartureInfo> newTrip = new ArrayList<>(currentNode.trip);
                    newTrip.add(departureInfo);

                    if (departureInfo.getDestinationAirportCode().equals(destination)) {
                        paths.add(newTrip);
                    }
                    else {
                        stack.push(new Node(departureInfo.getDestinationAirportCode(), departureInfo.getDepartureAt(),
                                newPrice, newTrip, currentNode.depth - 1));
                    }
                }
            }
        }
    }

//    private void explorationStep(List<DepartureInfo> currentTrip, String currentAirport, String currentDate,
//                                 double currentPrice, double maxPrice, int dayRange, String destinationAirport){
//        List<DepartureInfo> allDepartures = new ArrayList<>();
//        LocalDate initialDate = LocalDate.parse(currentDate);
//        for (int i = 0; i < dayRange; i++) {
//            LocalDate dateToCheck = initialDate.plusDays(i);
//            List<DepartureInfo> dailyDepartures = getDepartures(currentAirport, dateToCheck.toString());
//            if (dailyDepartures != null) {
//                allDepartures.addAll(dailyDepartures);
//            }
//        }
//        allDepartures.sort(Comparator.comparingDouble(DepartureInfo::getPrice));
//
//        for (DepartureInfo departureInfo : allDepartures) {
//            if (departureInfo.getPrice() + currentPrice < maxPrice){
//                if (departureInfo.getDestinationAirportCode().equals(destinationAirport)){
//
//                }
//            }
//        }
//    }

    private List<DepartureInfo> getDepartures(String airportCode, String date){
        String url = takeFlightsUrl + "origin=" + airportCode + "&departure_at=" + date;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
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
        int depth;

        Node(String airport, String date, double price, List<DepartureInfo> trip, int depth) {
            this.airport = airport;
            this.date = date;
            this.price = price;
            this.trip = trip;
            this.depth = depth;
        }
    }
}
