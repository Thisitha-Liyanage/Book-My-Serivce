package lk.ijse.aad.backend.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TopRatedServiceDto {
    private int serviceId;
    private String title;
    private String providerName;
    private String providerVillage;
    private String description;
    private double avgRating;
    private double price;
}
