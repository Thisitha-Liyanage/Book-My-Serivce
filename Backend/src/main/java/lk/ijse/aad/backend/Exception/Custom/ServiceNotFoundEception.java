package lk.ijse.aad.backend.Exception.Custom;

public class ServiceNotFoundEception extends RuntimeException {
    public ServiceNotFoundEception(String message) {
        super(message);
    }
}
