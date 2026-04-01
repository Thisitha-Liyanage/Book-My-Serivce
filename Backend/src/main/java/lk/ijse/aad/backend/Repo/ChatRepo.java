package lk.ijse.aad.backend.Repo;

import lk.ijse.aad.backend.Entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Integer> {
    List<Chat> findAllByBooking_IdAndRead(int bookingId, boolean read);
    List<Chat> findAllByBooking_Id(int bookingId);

    List<Chat> findAllByReceiver_IdOrSender_Id(int receiverId, int senderId);
}
