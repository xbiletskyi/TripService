package aroundtheeurope.tripservice.Services.DepartureService;

import aroundtheeurope.tripservice.model.dto.DepartureInfo;
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
import java.util.*;

/**
 * Service implementation for retrieving departure information.
 * This service interacts with an external microservice (FlightService) to fetch
 * departure data based on specified criteria such as airport code, date range, and Schengen area.
 */
@Service
public class DepartureServiceImpl implements DepartureService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${FlightService.url}")
    String takeFlightsUrl;
    /**
     * Constructor for DepartureServiceImpl.
     * Initializes the service with the necessary dependencies to make HTTP requests and process JSON data.
     *
     * @param restTemplate the RestTemplate used to make HTTP requests
     * @param objectMapper the ObjectMapper used to serialize and deserialize JSON objects
     */
    @Autowired
    public DepartureServiceImpl(
            RestTemplate restTemplate,
            ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Retrieves departure information from the TakeFlights microservice.
     * Constructs a URL based on the given parameters, makes an HTTP GET request to the microservice,
     * and processes the JSON response into a list of DepartureInfo objects.
     *
     * @param airportCode the IATA code of the airport
     * @param date the starting date of the departure search
     * @param dayRange the number of days to search departures from the start date
     * @param schengenOnly if true, only includes flights within the Schengen Area
     * @return a list of DepartureInfo objects containing departure details
     */
    @Override
    public List<DepartureInfo> retrieveDepartures(
            String airportCode,
            LocalDate date,
            int dayRange,
            boolean schengenOnly
    ){
        // Construct the URL for the FlightService microservice request
        String url = takeFlightsUrl + "origin=" + airportCode
                + "&departureAt=" + date.toString()
                + "&schengenOnly=" + schengenOnly
                + "&dayRange=" + dayRange;

        // Set HTTP headers and create an HttpEntity for the request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Make the HTTP GET request and retrieve the response as a string
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        List<DepartureInfo> departureInfos = Collections.emptyList();

        if (response.getBody() != null) {
            try {
                // Deserialize the JSON response into a list of DepartureInfo objects
                departureInfos = objectMapper.readValue(response.getBody(), new TypeReference<>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Response body is null for request to " + url);
        }
        return departureInfos;
    }
}
