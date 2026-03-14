package lk.ijse.aad.backend.Controller;

import lk.ijse.aad.backend.Dto.UserDto;
import lk.ijse.aad.backend.Entity.Role;
import lk.ijse.aad.backend.Service.ServicesService;
import lk.ijse.aad.backend.Service.UserService;
import lk.ijse.aad.backend.Util.APIResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ServicesService servicesService;

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

    @GetMapping("getServicesCount")
    public ResponseEntity<APIResponse> getAllServicesCount(Authentication authentication) {
        String email = authentication.getName();
        userService.findUserByEmail(email);
        return ResponseEntity.ok(new APIResponse(200 ,"Admin Found",servicesService.
                conuntAllServices()));
    }


    /// //////////////////////////// admin customer /////////////////////////////////////

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

    /// ////////////////////////////// admin provider ////////////////////////////////////////
    @GetMapping("getAllProviders")
    public ResponseEntity<APIResponse> getAllProviders(Authentication authentication) {
        String email = authentication.getName();
        userService.findUserByEmail(email);
        return ResponseEntity.ok(new APIResponse(200 , "admin found" ,
                userService.findAllUsersbyRole(Role.PROVIDER)));
    }

    @DeleteMapping("/deleteProvider/{email}")
    public ResponseEntity<APIResponse> deleteProvider(
            @PathVariable String email
            ,Authentication authentication
    ){
        String adminEmail = authentication.getName();
        userService.findUserByEmail(adminEmail);
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok(
                new APIResponse(200 , "admin Found", "Provider Deleted")
        );
    }

    @GetMapping("/getProviderByEmail/{email}")
    public ResponseEntity<APIResponse> getProviderByEmail(
            @PathVariable String email
            ,Authentication authentication
    ){
        userService.findUserByEmail(authentication.getName());
        UserDto userDto = userService.findUserByEmail(email);
        return ResponseEntity.ok(new APIResponse(200 ,"admin found", userDto));
    }


    /// /////////////////////// admin service ///////////////////////////////////
    @GetMapping("getAllServices")
    public ResponseEntity<APIResponse> getAllServices(Authentication authentication) {
        String email = authentication.getName();
        userService.findUserByEmail(email);
        return ResponseEntity.ok(new APIResponse(200 , "admin found"
                , servicesService.getAllServices()));
    }

    @GetMapping("getServiceByID/{id}")
    public ResponseEntity<APIResponse> getServiceByID(
            @PathVariable int id,
            Authentication authentication) {
        String adminEmail = authentication.getName();
        userService.findUserByEmail(adminEmail);
        return ResponseEntity.ok(new APIResponse(200 , "service found" , servicesService.getServiceByID(id)));
    }

    @DeleteMapping("deleteService/{id}")
    public ResponseEntity<APIResponse> deleteService(
            @PathVariable int id,
            Authentication authentication
    ) {
        String adminEmail = authentication.getName();
        userService.findUserByEmail(adminEmail);
        servicesService.deleteServiceByID(id);
        return ResponseEntity.ok(new APIResponse(200, "Success", "Service Deleted"));
    }

}
