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
    int countByStatusAndService_Provider_Id(Status status, int providerId);
    int countByService_Provider_Id(int providerId);

    @Query("SELECT b FROM Booking b WHERE b.status IN :statuses AND b.user.id = :userId")
    List<Booking> findByStatusesAndUserId(@Param("statuses") List<Status> statuses, @Param("userId") int userId);



}