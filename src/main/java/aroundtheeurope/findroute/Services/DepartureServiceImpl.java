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
import java.util.*;

/**
 * Service implementation for retrieving departure information.
 */
@Service
public class DepartureServiceImpl implements DepartureService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${TakeFlights.url}")
    String takeFlightsUrl;

    @Value("${spring.security.user.name}")
    String authUsername;

    @Value("${spring.security.user.password}")
    String authPassword;

    /**
     * Constructor for DepartureServiceImpl.
     *
     * @param restTemplate the RestTemplate to make HTTP requests
     * @param objectMapper the ObjectMapper to serialize and deserialize objects
     */
    @Autowired
    public DepartureServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
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
    public List<DepartureInfo> getDepartures(String airportCode, String date, boolean schengenOnly){
        String url = takeFlightsUrl + "origin=" + airportCode + "&departure_at=" + date + "&schengenOnly=" + schengenOnly;
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
}
