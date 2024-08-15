package aroundtheeurope.tripservice.repository;

import aroundtheeurope.tripservice.model.dto.FoundTripPreview;
import aroundtheeurope.tripservice.model.entity.FoundTripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for performing CRUD operations on FoundTripEntity.
 * Includes custom queries for retrieving trip previews and trips by user or request ID.
 */
public interface FoundTripRepository extends JpaRepository<FoundTripEntity, UUID> {

    /**
     * Retrieves a list of FoundTripPreview objects for a specific user.
     *
     * @param userId the unique identifier of the user
     * @return a list of FoundTripPreview objects
     */
    @Query("SELECT new aroundtheeurope.tripservice.model.dto.FoundTripPreview(f.totalPrice, f.totalFlights, f.uniqueCities, f.uniqueCountries, f.departureAt, f.arrivalAt, f.requestId) " +
            "FROM FoundTripEntity f WHERE f.userId = :userId")
    List<FoundTripPreview> findTripPreviewByUserId(UUID userId);

    /**
     * Retrieves a list of FoundTripEntity objects for a specific trip request.
     *
     * @param requestId the unique identifier of the trip request
     * @return a list of FoundTripEntity objects
     */
    @Query("SELECT f FROM FoundTripEntity f " +
            "JOIN f.tripSchedule ts " +
            "JOIN ts.foundTrips ft " +
            "WHERE f.requestId = :requestId")
    List<FoundTripEntity> findTripsByRequestId(@Param("requestId") UUID requestId);

    /**
     * Retrieves a list of FoundTripEntity objects for a specific user.
     *
     * @param userId the unique identifier of the user
     * @return a list of FoundTripEntity objects
     */
    @Query("SELECT f FROM FoundTripEntity f " +
            "JOIN f.tripSchedule ts " +
            "JOIN ts.foundTrips ft " +
            "WHERE f.userId = :userId")
    List<FoundTripEntity> findTripsById(@Param("userId") UUID userId);
}
