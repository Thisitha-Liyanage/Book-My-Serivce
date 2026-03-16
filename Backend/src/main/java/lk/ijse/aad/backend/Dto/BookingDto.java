package lk.ijse.aad.backend.Dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class BookingDto {

    @NotNull(message = "Booking date is required")
    @FutureOrPresent(message = "Booking date must be today or in the future")
    private LocalDate bookingDate;

    @NotNull(message = "Booking time is required")
    private LocalTime bookingTime;

    @NotNull(message = "Booking status is required")
    private Status status;

    @NotNull(message = "Service ID is required")
    @Positive(message = "Service ID must be greater than 0")
    private int serviceId;

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be greater than 0")
    private int userId;
}