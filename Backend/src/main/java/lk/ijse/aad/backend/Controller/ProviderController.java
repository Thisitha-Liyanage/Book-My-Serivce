package lk.ijse.aad.backend.Controller;

import jakarta.validation.Valid;
import lk.ijse.aad.backend.Dto.*;
import lk.ijse.aad.backend.Entity.*;
import lk.ijse.aad.backend.Service.*;
import lk.ijse.aad.backend.Util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/provider")
public class ProviderController {
    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    private ChatService chatService;

    @GetMapping("findProvider")
    public ResponseEntity<APIResponse> findProvider(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(new APIResponse(200 , "provider found",
                userService.findProviderByEmail(email)));
    }

    @GetMapping("getAllBookings")
    public ResponseEntity<APIResponse> getAllBookings(Authentication authentication) {
        String email = authentication.getName();
        ProviderResponseDto userDto = userService.findProviderByEmail(email);
        return ResponseEntity.ok(new APIResponse(200 , "bookings found" ,
                bookingService.getAllBookingsByProvider(userDto.getId())));
    }

    @GetMapping("getBookingById/{id}")
    public ResponseEntity<APIResponse> getBookingById(
            @PathVariable int id ,
            Authentication authentication) {
        String email = authentication.getName();
        userService.findProviderByEmail(email);
        return ResponseEntity.ok(new APIResponse(200 , "booking found" , bookingService.getBookingById(id)));
    }

    @PutMapping("updateStatus/{id}")
    public ResponseEntity<APIResponse> updateStatus(
            Authentication authentication
            , @PathVariable int id,
            @RequestBody Map<String, String> body
    ) {
        String email = authentication.getName();
        userService.findProviderByEmail(email);

        System.out.println(body.get("status"));
        System.out.println(id);

        String statusStr = body.get("status");
        if (statusStr == null) {
            return ResponseEntity.badRequest()
                    .body(new APIResponse(400, "Status is required", null));
        }
        Status newStatus = Status.valueOf(statusStr.toUpperCase());
        bookingService.updateStatus(id, newStatus);
        return ResponseEntity.ok(new APIResponse(200, "Status updated successfully", null));
    }

    @GetMapping("bookingOverview/{id}")
    public ResponseEntity<APIResponse> getBookingOverview(
            Authentication authentication,
            @PathVariable int id) {
        String email = authentication.getName();
        userService.findProviderByEmail(email);

        int pending = bookingService.countByStatusAndServiceProviderId(Status.PENDING , id);
        int accepted = bookingService.countByStatusAndServiceProviderId(Status.ACCEPTED , id);
        int completed = bookingService.countByStatusAndServiceProviderId(Status.COMPLETED , id);
        int cancelled = bookingService.countByStatusAndServiceProviderId(Status.REJECTED , id);

        Map<String, Integer> statusCounts = new HashMap<>();
        statusCounts.put("pending", pending);
        statusCounts.put("accepted", accepted);
        statusCounts.put("completed", completed);
        statusCounts.put("cancelled", cancelled);

        return ResponseEntity.ok(new APIResponse(200 , "provider overview", statusCounts));
    }


    @GetMapping("serviceCount/{id}")
    public ResponseEntity<APIResponse> getServiceCount(Authentication authentication
    , @PathVariable int id
    ) {
        String email = authentication.getName();
        userService.findProviderByEmail(email);
        return ResponseEntity.ok(new APIResponse(200 , "provider found" ,
                servicesService.countServicesByProviderId(id)));
    }


    @GetMapping("bookingCount/{id}")
    public ResponseEntity<APIResponse> getBookingCount(
            @PathVariable int id ,
            Authentication authentication
    ){
        String email = authentication.getName();
        userService.findProviderByEmail(email);

        return ResponseEntity.ok(new APIResponse(200 , "provider found" ,
                bookingService.countByProviderID(id)));
    }

    @GetMapping("getServices/{id}")
    public ResponseEntity<APIResponse> getServices(
            Authentication authentication,
            @PathVariable int id
    ){
        String email = authentication.getName();
        userService.findUserByEmail(email);

        List<ServiceResponseDto> services = servicesService.getAllServicesByProviderId(id);

        return ResponseEntity.ok(
                new APIResponse(200, "Services Found", services)
        );
    }


    @GetMapping("findServiceByID/{id}")
    public ResponseEntity<APIResponse> findServiceByID(
            @PathVariable int id,
            Authentication authentication
    ){
        String email = authentication.getName();
        userService.findProviderByEmail(email);

        return ResponseEntity.ok(new APIResponse(200 , "service found" ,
                servicesService.getServiceByID(id)));
    }

    @PostMapping("saveService")
    public ResponseEntity<APIResponse> saveService(
            Authentication authentication ,
            @Valid @RequestBody ServiceDto dto
    ){
        String email = authentication.getName();
        userService.findProviderByEmail(email);

        System.out.println(dto.getProviderId());

        ServiceResponseDto serviceResponseDto = servicesService.saveService(dto);
        return ResponseEntity.ok(new APIResponse(200 ,
                "service saved successfully", serviceResponseDto ));
    }

    @PutMapping("updateService")
    public ResponseEntity<APIResponse> updateService(
            Authentication authentication
            , @Valid @RequestBody ServiceResponseDto dto
    ){
        String email = authentication.getName();
        userService.findProviderByEmail(email);

        ServiceResponseDto serviceResponseDto = servicesService.updateService(dto);
        return ResponseEntity.ok(new APIResponse(200 ,
                "service updated successfully", serviceResponseDto ));
    }

    @DeleteMapping("deleteService/{id}")
    public ResponseEntity<APIResponse> deleteService(@PathVariable int id  , Authentication authentication) {
        String email = authentication.getName();
        userService.findProviderByEmail(email);
        servicesService.deleteServiceByID(id);
        return ResponseEntity.ok(new APIResponse(200 , "Service Found" , "Service deleted successfully"));

    }


    @PutMapping("/availability/{providerId}")
    public ResponseEntity<?> updateAvailability(
            @PathVariable int providerId,
            Authentication authentication,
            @RequestBody Map<String, String> body) {

        String status = body.get("availability");

        availabilityService.updateAvailability(authentication.getName(), status);

        return ResponseEntity.ok("Availability updated successfully");
    }

    @PostMapping("sendMassage")
    public ResponseEntity<APIResponse> sendMassage(
            Authentication authentication,
            @RequestBody CustomerChatDto customerChatDto
    ){
        String username = authentication.getName();
        userService.findUserByEmail(username);

        chatService.sendMassageToProvider(username , customerChatDto);
        return ResponseEntity.ok(new APIResponse(200 , "provider found" ,
                "massage sent"));
    }

    @GetMapping("getMassages")
    public ResponseEntity<APIResponse> getMassages(
            Authentication authentication
    ){
        String username = authentication.getName();
        userService.findUserByEmail(username);
        return ResponseEntity.ok(new APIResponse(200 , "chat found"
                , chatService.getAllProviderChats(username)));
    }
}
