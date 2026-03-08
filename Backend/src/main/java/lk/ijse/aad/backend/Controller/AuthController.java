package lk.ijse.aad.backend.Controller;

import jakarta.validation.Valid;
import lk.ijse.aad.backend.Dto.AuthResponseDto;
import lk.ijse.aad.backend.Dto.LoginDto;
import lk.ijse.aad.backend.Dto.UserDto;
import lk.ijse.aad.backend.Service.UserService;
import lk.ijse.aad.backend.Util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<APIResponse> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(new APIResponse(
                200 , "Login Success" , userService.login(loginDto)
        ));
    }

    @PostMapping("signup")
    public ResponseEntity<APIResponse> signup(@RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(
                new APIResponse(
                        200 , "User Saved" , userService.createAccount(userDto)
                )
        );
    }
}
