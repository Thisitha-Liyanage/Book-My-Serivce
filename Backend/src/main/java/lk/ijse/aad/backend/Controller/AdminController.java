package lk.ijse.aad.backend.Controller;

import lk.ijse.aad.backend.Dto.UserDto;
import lk.ijse.aad.backend.Entity.Role;
import lk.ijse.aad.backend.Service.UserService;
import lk.ijse.aad.backend.Util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/foundAdmin")
    public ResponseEntity<APIResponse> foundAdmin(Authentication authentication) {
        String email = authentication.getName();
        UserDto userDto = userService.findUserByEmail(email);
        return ResponseEntity.ok(
                new APIResponse(200 ,"Admin Found", userDto)
        );
    }

    @GetMapping("getCustomerCount")
    public ResponseEntity<APIResponse> getAllCustomersCount(Authentication authentication) {
        String email = authentication.getName();
        userService.findUserByEmail(email);
        return ResponseEntity.ok(
                new APIResponse(200 ,"Admin Found", userService.countByRole("CUSTOMER"))
        );
    }

    @GetMapping("getProviderCount")
    public ResponseEntity<APIResponse> getAllProvidersCount(Authentication authentication) {
        String email = authentication.getName();
        userService.findUserByEmail(email);
        return ResponseEntity.ok(
                new APIResponse(200 ,"Admin Found", userService.countByRole("PROVIDER"))
        );
    }

    @GetMapping("getAllCustomers")
    public ResponseEntity<APIResponse> getAllCustomers(Authentication authentication) {
        String email = authentication.getName();
        userService.findUserByEmail(email);
        return ResponseEntity.ok(new APIResponse(200 ,"Admin Found", userService.findAllUsersbyRole(Role.CUSTOMER))
        );
    }

    @DeleteMapping("/deleteCustomer/{email}")
    public ResponseEntity<APIResponse> deleteCustomer(
            @PathVariable String email,
            Authentication authentication) {

        String adminEmail = authentication.getName();
        userService.findUserByEmail(adminEmail);

        userService.deleteUserByEmail(email);

        return ResponseEntity.ok(
                new APIResponse(200, "Admin Found", "Customer Deleted")
        );
    }

    @GetMapping("/getCustomerByEmail/{email}")
    public ResponseEntity<APIResponse> getCustomerByEmail(
            @PathVariable String email,
            Authentication authentication
    ) {
        userService.findUserByEmail(authentication.getName());

        UserDto userDto = userService.findUserByEmail(email);
        return ResponseEntity.ok(new APIResponse(200 ,"customer Found", userDto));
    }
}