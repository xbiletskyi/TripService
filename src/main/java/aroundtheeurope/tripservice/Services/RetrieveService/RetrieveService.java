package aroundtheeurope.tripservice.Services.RetrieveService;

import aroundtheeurope.tripservice.model.dto.TripRequest.TripRequestIn;
import aroundtheeurope.tripservice.model.dto.TripRequest.TripRequestOut;
import aroundtheeurope.tripservice.model.entity.TripRequestEntity;
import aroundtheeurope.tripservice.repository.FoundTripRepository;
import aroundtheeurope.tripservice.model.dto.FoundTrip;
import aroundtheeurope.tripservice.model.dto.FoundTripPreview;
import aroundtheeurope.tripservice.model.entity.FoundTripEntity;
import aroundtheeurope.tripservice.repository.TripRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service implementation for retrieving trips and trip previews.
 * This class provides methods to retrieve trip data from the database based on user IDs or request IDs.
 */
@Service
public class RetrieveService implements IRetrieveService {

    private final FoundTripRepository foundTripRepository;
    private final TripRequestRepository tripRequestRepository;

    /**
     * Constructor for TripRetrieveService.
     * Initializes the service with the necessary repository for retrieving trip data.
     *
     * @param foundTripRepository the repository used to access found trips in the database
     * @param tripRequestRepository the repository used to access requests in the database
     */
    @Autowired
    public RetrieveService(
            FoundTripRepository foundTripRepository,
            TripRequestRepository tripRequestRepository
    ) {
        this.foundTripRepository = foundTripRepository;
        this.tripRequestRepository = tripRequestRepository;
    }

    /**
     * Retrieves a list of trip previews for a specific user.
     *
     * @param userId the unique identifier of the user
     * @return a list of FoundTripPreview objects for the specified user
     */
    @Override
    public List<FoundTripPreview> retrievePreview(UUID userId){
        return foundTripRepository.findTripPreviewByUserId(userId);
    }

    /**
     * Retrieves a list of found trips based on a specific trip request.
     *
     * @param requestId the unique identifier of the trip request
     * @return a list of FoundTrip objects that match the specified request ID
     */
    @Override
    public List<FoundTrip> retrieveByRequest(UUID requestId){
        List<FoundTripEntity> trips = foundTripRepository.findTripsByRequestId(requestId);
        return trips.stream().map(FoundTrip::constructFromEntity).toList();
    }

    /**
     * Retrieves a list of found trips for a specific user.
     *
     * @param userid the unique identifier of the user
     * @return a list of FoundTrip objects that belong to the specified user
     */
    @Override
    public List<FoundTrip> retrieveByUser(UUID userid){
        List<FoundTripEntity> trips = foundTripRepository.findTripsById(userid);
        return trips.stream().map(FoundTrip::constructFromEntity).toList();
    }

    /**
     * Retrieves all requests of the given user
     *
     * @param userId the unique identifier of the user
     * @return a list of requests' details issued by the user
     */
    @Override
    public List<TripRequestOut> retrieveRequestByUser(UUID userId){
        List<TripRequestEntity> requests = tripRequestRepository.findByUserId(userId);
        return requests.stream().map(TripRequestOut::constructFromEntity).toList();
    }
}
