package aroundtheeurope.tripservice.repository;

import aroundtheeurope.tripservice.model.entity.TripRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for performing CRUD operations on TripRequestEntity.
 * Extends JpaRepository to provide standard data access methods.
 */
public interface TripRequestRepository extends JpaRepository<TripRequestEntity, UUID> {

    List<TripRequestEntity> findByUserId(UUID userId);

}
