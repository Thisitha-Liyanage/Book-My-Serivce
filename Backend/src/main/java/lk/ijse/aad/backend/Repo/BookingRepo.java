package lk.ijse.aad.backend.Repo;

import lk.ijse.aad.backend.Entity.Booking;
import lk.ijse.aad.backend.Entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking, Integer> {

    int countByStatus(Status status);

    List<Booking> findByService_Provider_Id(int providerId);

    @Transactional
    @Modifying
    @Query("UPDATE Booking b SET b.status = :status WHERE b.id = :id")
    int updateBookingStatus(@Param("id") int id, @Param("status") Status status);

    // ✅ FIXED
    int countByStatusAndService_Provider_Id(Status status, int providerId);

    // ✅ THIS IS CORRECT
    int countByService_Provider_Id(int providerId);
}