package lk.ijse.aad.backend.Repo;

import lk.ijse.aad.backend.Entity.Booking;
import lk.ijse.aad.backend.Entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {
    int countByStatus(Status status);
}
