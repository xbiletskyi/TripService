package aroundtheeurope.tripservice.Services.TripSearchService;

import aroundtheeurope.tripservice.model.dto.DepartureInfo;
import aroundtheeurope.tripservice.model.dto.TripRequest;

import java.util.List;

public interface TripFindingStrategy {
    void findTrips(
            TripRequest tripRequest,
            List<List<DepartureInfo>> paths
    );
}
