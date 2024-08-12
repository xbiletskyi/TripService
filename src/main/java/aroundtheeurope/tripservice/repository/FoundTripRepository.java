package aroundtheeurope.tripservice.repository;

import aroundtheeurope.tripservice.model.dto.FoundTripPreview;
import aroundtheeurope.tripservice.model.entity.FoundTripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface FoundTripRepository extends JpaRepository<FoundTripEntity, UUID> {
    @Query("SELECT new aroundtheeurope.tripservice.model.dto.FoundTripPreview(f.totalPrice, f.totalFlights, f.uniqueCities, f.uniqueCountries, f.departureAt, f.arrivalAt) " +
            "FROM FoundTripEntity f WHERE f.userId = :userId")
    List<FoundTripPreview> findTripPreviewByUserId(UUID userId);

    List<FoundTripEntity> findTripsByRequestId(UUID userId);

    List<FoundTripEntity> findTripsByUserId(UUID userId);
}
