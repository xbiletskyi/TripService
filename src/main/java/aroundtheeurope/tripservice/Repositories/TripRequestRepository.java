package aroundtheeurope.tripservice.Repositories;

import aroundtheeurope.tripservice.model.entity.TripRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TripRequestRepository extends JpaRepository<TripRequestEntity, UUID> {
}
