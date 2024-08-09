package aroundtheeurope.tripservice.Services.TripRetrieveService;

import aroundtheeurope.tripservice.model.dto.FoundTrip;
import aroundtheeurope.tripservice.model.dto.FoundTripPreview;

import java.util.List;
import java.util.UUID;

public interface ITripRetrieveService {

    public List<FoundTripPreview> retrievePreview(UUID userId);

    public List<FoundTrip> retrieveByRequest(UUID requestId);

    public List<FoundTrip> retrieveByUser(UUID userid);

}

