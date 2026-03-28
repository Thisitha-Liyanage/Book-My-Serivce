package lk.ijse.aad.backend.Repo;

import lk.ijse.aad.backend.Entity.Availability;
import lk.ijse.aad.backend.Entity.ProviderAvailability;
import lk.ijse.aad.backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvailabilityRepo extends JpaRepository<ProviderAvailability, Integer> {
    Optional<ProviderAvailability> findByProvider(User provider);
}
