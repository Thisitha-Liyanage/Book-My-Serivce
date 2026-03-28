package lk.ijse.aad.backend.Service.Impl;

import jakarta.transaction.Transactional;
import lk.ijse.aad.backend.Dto.BookingResponseDto;
import lk.ijse.aad.backend.Entity.Booking;
import lk.ijse.aad.backend.Entity.Status;
import lk.ijse.aad.backend.Exception.Custom.BookingNotFoundException;
import lk.ijse.aad.backend.Repo.BookingRepo;
import lk.ijse.aad.backend.Service.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    BookingRepo bookingRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public int countAllServices() {
        return (int) bookingRepo.count();
    }

    @Override
    public List<BookingResponseDto> getAllBookings() {
        List<Booking> bookings = bookingRepo.findAll();
        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingResponseDto.class))
                .toList();
    }

    @Override
    public BookingResponseDto getBookingById(int bookingId) {
        Optional<Booking> booking = bookingRepo.findById(bookingId);
        if(booking.isEmpty()){
            throw new BookingNotFoundException("Booking not found");
        }
        return modelMapper.map(booking.get(), BookingResponseDto.class);
    }

    @Override
    public int countByStatus(Status status) {
        return bookingRepo.countByStatus(status);
    }

    @Override
    public List<BookingResponseDto> getAllBookingsByProvider(int providerId) {

        List<Booking> bookings = bookingRepo.findByService_Provider_Id(providerId);

        return bookings.stream()
                .map(booking -> modelMapper.map(booking, BookingResponseDto.class))
                .toList();
    }

    @Override
    @Transactional
    public void updateStatus(int bookingId , Status status) {
        int updatedRaws = bookingRepo.updateBookingStatus(bookingId , status);
        if (updatedRaws <= 0) {
            throw new BookingNotFoundException("Booking not found");
        }
    }

    @Override
    public int countByStatusAndServiceProviderId(Status status, int providerId) {
        return bookingRepo.countByStatusAndService_Provider_Id(status, providerId);
    }

    @Override
    public int countByProviderID(int providerId) {
        return bookingRepo.countByService_Provider_Id(providerId);
    }
}
