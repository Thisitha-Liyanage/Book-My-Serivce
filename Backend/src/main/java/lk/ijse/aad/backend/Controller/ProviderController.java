package lk.ijse.aad.backend.Controller;

import lk.ijse.aad.backend.Dto.BookingResponseDto;
import lk.ijse.aad.backend.Dto.UserDto;
import lk.ijse.aad.backend.Service.BookingService;
import lk.ijse.aad.backend.Service.UserService;
import lk.ijse.aad.backend.Util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/provider")
public class ProviderController {
    @Autowired
    private UserService userService;

    @GetMapping("findProvider")
    public ResponseEntity<APIResponse> findProvider(Authentication authentication) {
        String email = authentication.getName();
        UserDto userdto = userService.findUserByEmail(email);
        return ResponseEntity.ok(new APIResponse(200 , "provider found", userdto));
    }
}
