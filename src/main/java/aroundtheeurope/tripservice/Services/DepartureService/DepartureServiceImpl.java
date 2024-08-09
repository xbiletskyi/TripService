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
 */
@Service
public class DepartureServiceImpl implements DepartureService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${FlightService.url}")
    String takeFlightsUrl;
    /**
     * Constructor for DepartureServiceImpl.
     *
     * @param restTemplate the RestTemplate to make HTTP requests
     * @param objectMapper the ObjectMapper to serialize and deserialize objects
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
     *
     * @param airportCode the IATA code of the airport
     * @param date the date of departure
     * @param schengenOnly if true, only includes flights within the Schengen Area
     * @return the list of DepartureInfo
     */
    @Override
    public List<DepartureInfo> retrieveDepartures(
            String airportCode,
            LocalDate date,
            int dayRange,
            boolean schengenOnly
    ){
        String url = takeFlightsUrl + "origin=" + airportCode
                + "&departureAt=" + date.toString()
                + "&schengenOnly=" + schengenOnly
                + "&dayRange=" + dayRange;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        List<DepartureInfo> departureInfos = Collections.emptyList(); // Default to an empty list

        if (response.getBody() != null) {
            try {
                departureInfos = objectMapper.readValue(response.getBody(), new TypeReference<List<DepartureInfo>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Response body is null for request to " + url);
        }
        return departureInfos;
    }
}
