package lk.ijse.aad.backend.Dto;


import lk.ijse.aad.backend.Entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingResponseDto {
    private int id;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private Status status;
    private int serviceId;
    private int userId;
}
