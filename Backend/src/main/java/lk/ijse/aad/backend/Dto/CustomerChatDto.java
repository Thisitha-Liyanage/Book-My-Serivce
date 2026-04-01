package lk.ijse.aad.backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerChatDto {
    private String massage;
    private int receiverId;
    private int bookingId;
}

