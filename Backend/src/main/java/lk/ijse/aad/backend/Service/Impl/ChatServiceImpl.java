package lk.ijse.aad.backend.Service.Impl;

import lk.ijse.aad.backend.Dto.CustomerChatDto;

import lk.ijse.aad.backend.Dto.ProviderChatResponseDto;
import lk.ijse.aad.backend.Entity.Booking;
import lk.ijse.aad.backend.Entity.Chat;
import lk.ijse.aad.backend.Entity.Role;
import lk.ijse.aad.backend.Entity.User;
import lk.ijse.aad.backend.Repo.BookingRepo;
import lk.ijse.aad.backend.Repo.ChatRepo;
import lk.ijse.aad.backend.Repo.UserRepo;
import lk.ijse.aad.backend.Service.ChatService;
import lk.ijse.aad.backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lk.ijse.aad.backend.Dto.CustomerChatResponseDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private ChatRepo chatRepo;


    @Override
    public void sendMassageToProvider(String  email , CustomerChatDto customerChatDto) {
        Optional<User> sender = userRepo.findByEmail(email);
        User reciver = userRepo.findById(customerChatDto.getReceiverId()).get();
        Optional<Booking> booking = bookingRepo.findById(customerChatDto.getBookingId());

        Chat chat = new Chat();
        chat.setReceiver(reciver);
        chat.setSender(sender.get());
        chat.setBooking(booking.get());
        chat.setRead(false);
        chat.setMessage(customerChatDto.getMassage());
        chatRepo.save(chat);
    }


    @Override
    public List<CustomerChatResponseDto> getAllChats(int id) {
        List<Chat> chats = chatRepo.findAllByBooking_Id(id);
        List<CustomerChatResponseDto> dtos = new ArrayList<>();
        for (Chat chat : chats) {
            CustomerChatResponseDto customerChatResponseDto = new CustomerChatResponseDto();
            customerChatResponseDto.setMassage(chat.getMessage());
            customerChatResponseDto.setSenderName(chat.getReceiver().getName());
            customerChatResponseDto.setSenderName(chat.getSender().getName());
            customerChatResponseDto.setReceiverName(chat.getReceiver().getName());
            customerChatResponseDto.setReceiverId(chat.getReceiver().getId());
            customerChatResponseDto.setSenderId(chat.getSender().getId());
            customerChatResponseDto.setReceiverRole(chat.getReceiver().getRole().toString());
            customerChatResponseDto.setSenderRole(chat.getSender().getRole().toString());
            dtos.add(customerChatResponseDto);
        }
        return dtos;
    }

    @Override
    public List<ProviderChatResponseDto> getAllProviderChats(String email) {
        Optional<User> providerOpt = userRepo.findByEmail(email);
        if (providerOpt.isEmpty()) return Collections.emptyList();

        User provider = providerOpt.get();

        List<Chat> chats = chatRepo.findAllByReceiver_IdOrSender_Id(provider.getId(), provider.getId());
        List<ProviderChatResponseDto> dtos = new ArrayList<>();

        for (Chat chat : chats) {
            ProviderChatResponseDto dto = new ProviderChatResponseDto();
            User sender = chat.getSender();
            User receiver = chat.getReceiver();

            User customer = sender.getRole() == Role.CUSTOMER ? sender : receiver;
            dto.setCustomerId(customer.getId());
            dto.setBookingId(chat.getBooking().getId());
            dto.setCustomerName(customer.getName());
            dto.setSenderRole(chat.getSender().getRole().toString());
            dto.setReciverRole(chat.getReceiver().getRole().toString());
            dto.setCity(customer.getCity());
            dto.setPhoneNumber(customer.getPhone());
            dto.setMessage(chat.getMessage());
            dto.setProviderId(provider.getId());


            if (chat.getBooking() != null) {
                dto.setBookingDate(chat.getBooking().getBookingDate());
                dto.setServiceName(chat.getBooking().getService().getTitle());
            }

            dtos.add(dto);
        }

        return dtos;
    }


}
