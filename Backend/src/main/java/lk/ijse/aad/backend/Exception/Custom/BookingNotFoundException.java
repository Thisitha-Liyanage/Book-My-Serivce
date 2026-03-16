package lk.ijse.aad.backend.Exception.Custom;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String message) {
        super(message);
    }
}
