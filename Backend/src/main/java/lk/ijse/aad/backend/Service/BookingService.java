package lk.ijse.aad.backend.Service;

import lk.ijse.aad.backend.Dto.BookingResponseDto;
import lk.ijse.aad.backend.Entity.Booking;
import lk.ijse.aad.backend.Entity.Status;

import java.util.List;

public interface BookingService {
    int conuntAllServices();
    List<BookingResponseDto> getAllBookings();
    BookingResponseDto getBookingById(int bookingId);
    int countByStatus(Status status);
}
