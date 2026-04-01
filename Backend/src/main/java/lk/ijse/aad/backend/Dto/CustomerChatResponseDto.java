package lk.ijse.aad.backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerChatResponseDto {
    private String senderName;
    private String massage;
    private String receiverName;
    private String receiverRole;
    private String city;
    private int providerId;
    private String providerRole;
    private String senderRole;
    private int senderId;
    private int receiverId;
}
