package lk.ijse.aad.backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponseDto {
    private int rating;
    private String description;
    private int serviceId;
    private String customerName;
}
