package lk.ijse.aad.backend.Service.Impl;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lk.ijse.aad.backend.Dto.BookingDto;
import lk.ijse.aad.backend.Dto.BookingResponseDto;
import lk.ijse.aad.backend.Entity.Booking;
import lk.ijse.aad.backend.Entity.Services;
import lk.ijse.aad.backend.Entity.Status;
import lk.ijse.aad.backend.Entity.User;
import lk.ijse.aad.backend.Exception.Custom.BookingNotFoundException;
import lk.ijse.aad.backend.Exception.Custom.ServiceNotFoundEception;
import lk.ijse.aad.backend.Repo.BookingRepo;
import lk.ijse.aad.backend.Repo.*;
import lk.ijse.aad.backend.Service.BookingService;
import lk.ijse.aad.backend.Util.EMailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private EMailService EMailService;

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
        Booking booking = bookingRepo.findById(bookingId).get();
        if(booking == null){
            throw new BookingNotFoundException("Booking not found");
        }

        int updatedRaws = bookingRepo.updateBookingStatus(bookingId , status);
        if (updatedRaws <= 0) {
            throw new BookingNotFoundException("Booking not found");
        }

        Optional<User> user = userRepo.findById(booking.getUser().getId());
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User Not Found");
        }

        try{
            EMailService.sendBookingStatusToCustomer(user.get().getEmail(), booking.getService().
                            getProvider().getName(),
                    String.valueOf(status));
        }catch (MessagingException e){
            throw new MailSendException("Mail Not Sent");
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

    @Override
    public List<BookingResponseDto> getBookingsByStatus(String email , List<Status> statuses) {

        Optional<User> user = userRepo.findByEmail(email);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        List<Booking> bookings = bookingRepo.findByStatusesAndUserId(
                statuses, user.get().getId()
        );

        return bookings.stream()
                .map(booking -> {
                    BookingResponseDto dto = modelMapper.map(booking, BookingResponseDto.class);

                    dto.setServiceId(booking.getService().getId());
                    dto.setUserId(booking.getUser().getId());

                    return dto;
                })
                .toList();
    }

    @Override
    public void AddBooking(String email, BookingDto bookingDto) {
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        Booking booking = modelMapper.map(bookingDto, Booking.class);
        booking.setUser(user.get());
        booking.setId(0);

        Services service = serviceRepo.findById(bookingDto.getServiceId())
                .orElseThrow(() -> new ServiceNotFoundEception("Service not found"));
        booking.setService(service);


        Booking savedBooking = bookingRepo.save(booking);
        if (savedBooking == null) {
            throw new BookingNotFoundException("Booking not found");
        }

        try{
            EMailService.sendBookingActionEmail(service.getProvider().getEmail(),
                    service.getProvider().getName()
                    , String.valueOf(savedBooking.getId()) , user.get());
        } catch (MessagingException e) {
            throw new MailSendException("Mail sent error");
        }
    }


}
