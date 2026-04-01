package lk.ijse.aad.backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProviderChatResponseDto {
    private int customerId;
    private int bookingId;
    private LocalDate bookingDate;
    private String message;
    private String customerName;
    private String serviceName;
    private String city;
    private String phoneNumber;
    private int providerId;
    private String senderRole;
    private String reciverRole;
}
