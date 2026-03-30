package lk.ijse.aad.backend.Repo;

import lk.ijse.aad.backend.Dto.RatingDto;
import lk.ijse.aad.backend.Entity.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepo extends JpaRepository<Ratings, Integer> {
    List<Ratings> findByService_Id(int serviceId);
}
