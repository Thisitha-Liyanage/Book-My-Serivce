package lk.ijse.aad.backend.Controller;

import jakarta.validation.Valid;
import lk.ijse.aad.backend.Dto.*;
import lk.ijse.aad.backend.Dto.CustomerChatResponseDto;
import lk.ijse.aad.backend.Entity.Booking;
import lk.ijse.aad.backend.Entity.Status;
import lk.ijse.aad.backend.Service.*;
import lk.ijse.aad.backend.Util.APIResponse;
import lk.ijse.aad.backend.Util.EMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private EMailService eMailService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private ChatService chatService;

    @GetMapping("foundCustomer")
    public ResponseEntity<APIResponse> getCustomer(
            Authentication authentication
    ) {
        String username = authentication.getName();
        UserDto userDto = userService.findUserByEmail(username);
        return ResponseEntity.ok(new APIResponse(200 , "customer found" , userDto));
    }

    @GetMapping("getNotCompletedBookings")
    public ResponseEntity<APIResponse> getNotCompletedBookings(
            Authentication authentication
    ){
        String username = authentication.getName();
        userService.findUserByEmail(username);

        return ResponseEntity.ok(new APIResponse(200 , "customer found" ,
                bookingService.getBookingsByStatus(username , List.of(Status.ACCEPTED, Status.PENDING) )));
    }

    @GetMapping("getServiceAndProviderDetails/{id}")
    public ResponseEntity<APIResponse> getServiceDetails(
            Authentication authentication,
            @PathVariable int id
    ){
        String username = authentication.getName();
        userService.findUserByEmail(username);

        return ResponseEntity.ok(new APIResponse(200 , "customer found" ,
                servicesService.getServiceAndProviderByID(id)));
    }

    @GetMapping("suggestedServices")
    public ResponseEntity<APIResponse> getSuggestedServices(
            Authentication authentication
    ){
        String username = authentication.getName();
        userService.findUserByEmail(username);

        return ResponseEntity.ok(new APIResponse(200 , "customer found" ,
                servicesService.getSuggestedServicesByCity(username)));
    }

    @GetMapping("bookingHistory")
    public ResponseEntity<APIResponse> getBookingHistory(
            Authentication authentication
    ){
        String username = authentication.getName();
        userService.findUserByEmail(username);

        return ResponseEntity.ok(new APIResponse(200 , "customer found" ,
                bookingService.getBookingsByStatus(username ,
                        List.of(Status.REJECTED, Status.COMPLETED))));
    }

    @PostMapping("bookService")
    public ResponseEntity<APIResponse> bookService(
            Authentication authentication,
            @Valid @RequestBody BookingDto bookingDto
    ){
        String username = authentication.getName();
        UserDto userDto = userService.findUserByEmail(username);

        bookingService.AddBooking(username, bookingDto);

        return ResponseEntity.ok(new APIResponse(200 , "booking Request Send" , "Wait for Reply"));
    }

    @GetMapping("/accept/{bookingId}")
    public ResponseEntity<APIResponse> acceptBooking(@PathVariable int bookingId) {
        bookingService.updateStatus(bookingId, Status.ACCEPTED);

        return ResponseEntity.ok(new APIResponse(200 , " booking accepted" ,
                "Provider Accept Booking"));
    }

    @GetMapping("/reject/{bookingId}")
    public ResponseEntity<?> rejectBooking(@PathVariable int bookingId) {
        bookingService.updateStatus(bookingId, Status.REJECTED);
        return ResponseEntity.ok(200 + "Booking Rejected");
    }

    @PostMapping("addRating")
    public ResponseEntity<APIResponse> addRating(
            Authentication authentication,
            @RequestBody RatingDto ratingDto
    ){
        String username = authentication.getName();
        userService.findUserByEmail(username);
        ratingService.saveRating(ratingDto , username);
        return ResponseEntity.ok(new APIResponse(200 , "customer found" , "rated successfully"));
    }

    @GetMapping("getRatingsByService/{id}")
    public ResponseEntity<APIResponse> getRatingsByService(
            Authentication authentication,
            @PathVariable int id
    ){
        String username = authentication.getName();
        userService.findUserByEmail(username);

        return ResponseEntity.ok(new APIResponse(200 , "service found" ,
                ratingService.getRatingByService(id , username)));
    }

    @GetMapping("getTopRatedServices")
    public ResponseEntity<APIResponse> getTopRatedServices(
            Authentication authentication
    ){
        String username = authentication.getName();
        userService.findUserByEmail(username);

        return ResponseEntity.ok(new APIResponse(200 , "services found" ,
                ratingService.getTop3Services()));
    }

    @GetMapping("getServiceByCategory/{selectedCategory}")
    public ResponseEntity<APIResponse> getServiceByCategory(
            Authentication authentication,
            @PathVariable String selectedCategory
    ){
        String email = authentication.getName();
        userService.findUserByEmail(email);

        return ResponseEntity.ok(new APIResponse(200 , "services found" ,
                servicesService.getServiceByCategory(email , selectedCategory)));
    }

    @PostMapping("sendMassage")
    public ResponseEntity<APIResponse> sendMassage(
            Authentication authentication,
            @RequestBody CustomerChatDto customerChatDto
            ){
        String username = authentication.getName();
        userService.findUserByEmail(username);

        chatService.sendMassageToProvider(username , customerChatDto);
        return ResponseEntity.ok(new APIResponse(200 , "customer found" ,
                "massage sent"));
    }

    @GetMapping("getFullChat/{id}")
    public ResponseEntity<APIResponse> getFullChat(
            Authentication authentication,
            @PathVariable int id
    ){
        String username = authentication.getName();
        userService.findUserByEmail(username);
        return ResponseEntity.ok(new APIResponse(200 , "customer found" , chatService.getAllChats(id)));
    }

}
