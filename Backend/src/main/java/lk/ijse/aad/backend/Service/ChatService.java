package lk.ijse.aad.backend.Service;

import lk.ijse.aad.backend.Dto.CustomerChatDto;

import lk.ijse.aad.backend.Dto.CustomerChatResponseDto;
import lk.ijse.aad.backend.Dto.ProviderChatResponseDto;

import java.util.List;

public interface ChatService {
    void sendMassageToProvider(String email , CustomerChatDto customerChatDto);
    List<CustomerChatResponseDto> getAllChats(int id);
    List<ProviderChatResponseDto> getAllProviderChats(String email);
}
