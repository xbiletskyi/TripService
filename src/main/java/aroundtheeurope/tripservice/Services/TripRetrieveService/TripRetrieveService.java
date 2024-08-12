package aroundtheeurope.tripservice.Services.TripRetrieveService;

import aroundtheeurope.tripservice.repository.FoundTripRepository;
import aroundtheeurope.tripservice.model.dto.FoundTrip;
import aroundtheeurope.tripservice.model.dto.FoundTripPreview;
import aroundtheeurope.tripservice.model.entity.FoundTripEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TripRetrieveService implements ITripRetrieveService {

    private final FoundTripRepository foundTripRepository;

    @Autowired
    public TripRetrieveService(FoundTripRepository foundTripRepository) {
        this.foundTripRepository = foundTripRepository;
    }

    @Override
    public List<FoundTripPreview> retrievePreview(UUID userId){
        List<FoundTripPreview> list = foundTripRepository.findTripPreviewByUserId(userId);
        return list;
    }

    @Override
    public List<FoundTrip> retrieveByRequest(UUID requestId){
        List<FoundTripEntity> trips = foundTripRepository.findTripsByRequestId(requestId);
        return trips.stream().map(FoundTrip::constructFromEntity).toList();
    }

    @Override
    public List<FoundTrip> retrieveByUser(UUID userid){
        List<FoundTripEntity> trips = foundTripRepository.findTripsByUserId(userid);
        return trips.stream().map(FoundTrip::constructFromEntity).toList();
    }
}
