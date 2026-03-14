package lk.ijse.aad.backend.Repo;

import lk.ijse.aad.backend.Entity.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepo extends JpaRepository<Services, Integer> {
}
