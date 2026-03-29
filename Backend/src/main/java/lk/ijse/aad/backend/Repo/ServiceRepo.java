package lk.ijse.aad.backend.Repo;

import lk.ijse.aad.backend.Entity.Availability;
import lk.ijse.aad.backend.Entity.Services;
import lk.ijse.aad.backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepo extends JpaRepository<Services, Integer> {
    long countByProvider(User provider);

    List<Services> findAllByProvider_Id(int providerId);

    List<Services> findByTitle(String title);

    List<Services> findByDescription(String description);

    List<Services> findByTitleAndDescription(String title, String description);

    List<Services> findAllByTitleAndDescription(String title, String description);

    List<Services> findByProvider_CityAndAvailability(String providerCity, Availability availability);

}
