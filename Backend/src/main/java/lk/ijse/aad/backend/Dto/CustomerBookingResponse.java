package lk.ijse.aad.backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerBookingResponse {
    private int id;
    private String title;
    private String category;
    private Double price;
    private String providerName;
    private String providerAvailability;
    private String providerPhone;
    private String providerEmail;
    private String providerVillage;
    private String description;
    private String status;
    private String city;
    private int providerId;


}
