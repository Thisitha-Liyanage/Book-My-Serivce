package lk.ijse.aad.backend.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(?:0|\\+94)?[0-9]{9,10}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Province is required")
    private String province;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Village is required")
    private String village;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "ADMIN|CUSTOMER|PROVIDER", message = "Role must be ADMIN, CUSTOMER or PROVIDER")
    private String role;
}