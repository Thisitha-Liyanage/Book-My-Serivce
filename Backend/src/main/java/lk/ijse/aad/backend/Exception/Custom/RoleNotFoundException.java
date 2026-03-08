package lk.ijse.aad.backend.Exception.Custom;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
