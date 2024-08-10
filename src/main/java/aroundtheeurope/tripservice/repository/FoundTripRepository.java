package aroundtheeurope.tripservice.repository;

import aroundtheeurope.tripservice.model.dto.FoundTripPreview;
import aroundtheeurope.tripservice.model.entity.FoundTripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FoundTripRepository extends JpaRepository<FoundTripEntity, UUID> {
    @Query(value = """
    SELECT  f.total_price AS totalPrice,
        f.total_flight AS totalFlights,
        f.unique_cities AS uniqueCities,
        f.unique_countries AS uniqueCountries,
        f.departure_at AS departureAt,
        f.arrival_at AS arrivalAt
    FROM found_trips f
    WHERE f.user_id = :userId
    """,
    nativeQuery = true)
    List<FoundTripPreview> findTripPreviewByUserId(UUID userId);

    List<FoundTripEntity> findTripsByRequestId(UUID userId);

    List<FoundTripEntity> findTripsByUserId(UUID userId);
}
