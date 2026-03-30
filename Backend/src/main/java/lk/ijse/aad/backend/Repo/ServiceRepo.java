package lk.ijse.aad.backend.Repo;

import lk.ijse.aad.backend.Entity.Availability;
import lk.ijse.aad.backend.Entity.Category;
import lk.ijse.aad.backend.Entity.Services;
import lk.ijse.aad.backend.Entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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


    @Query("SELECT s FROM Services s LEFT JOIN s.ratings r " +
            "GROUP BY s " +
            "ORDER BY COALESCE(AVG(r.rating), 0) DESC")
    List<Services> findTopRatedServices(Pageable pageable);

    List<Services> findAllByAvailabilityAndCategoryAndProvider_City(Availability availability, Category category, String providerCity);

}
