package lk.ijse.aad.backend.Dto;

import lk.ijse.aad.backend.Entity.Availability;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponseDto {
    private int id;
    private String category;
    private String title;
    private String description;
    private double price;
    private String availability;
    private int userId;
}
