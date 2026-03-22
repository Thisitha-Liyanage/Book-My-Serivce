package lk.ijse.aad.backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProviderResponseDto {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String province;
    private String city;
    private String village;
    private String role;

}
