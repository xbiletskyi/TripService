package aroundtheeurope.tripservice.Services.TripRetrieveService;

import aroundtheeurope.tripservice.model.dto.FoundTrip;
import aroundtheeurope.tripservice.model.dto.FoundTripPreview;

import java.util.List;
import java.util.UUID;

/**
 * Interface for defining the contract for trip retrieval services.
 * Implementations of this interface are responsible for retrieving found trips
 * and trip previews based on user IDs or request IDs.
 */
public interface ITripRetrieveService {

    /**
     * Retrieves a list of trip previews for a specific user.
     *
     * @param userId the unique identifier of the user
     * @return a list of FoundTripPreview objects for the specified user
     */
    public List<FoundTripPreview> retrievePreview(UUID userId);

    /**
     * Retrieves a list of found trips based on a specific trip request.
     *
     * @param requestId the unique identifier of the trip request
     * @return a list of FoundTrip objects that match the specified request ID
     */
    public List<FoundTrip> retrieveByRequest(UUID requestId);

    /**
     * Retrieves a list of found trips for a specific user.
     *
     * @param userid the unique identifier of the user
     * @return a list of FoundTrip objects that belong to the specified user
     */
    public List<FoundTrip> retrieveByUser(UUID userid);

}

