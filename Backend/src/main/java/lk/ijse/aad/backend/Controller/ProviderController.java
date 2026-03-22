package lk.ijse.aad.backend.Controller;

import lk.ijse.aad.backend.Dto.BookingResponseDto;
import lk.ijse.aad.backend.Dto.ProviderResponseDto;
import lk.ijse.aad.backend.Dto.UserDto;
import lk.ijse.aad.backend.Entity.Status;
import lk.ijse.aad.backend.Service.BookingService;
import lk.ijse.aad.backend.Service.UserService;
import lk.ijse.aad.backend.Util.APIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/provider")
public class ProviderController {
    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

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
}
