package lk.ijse.aad.backend.Exception;

import io.jsonwebtoken.ExpiredJwtException;
import jdk.jshell.spi.ExecutionControl;
import lk.ijse.aad.backend.Dto.AuthResponseDto;
import lk.ijse.aad.backend.Exception.Custom.ServiceNotFoundEception;
import lk.ijse.aad.backend.Util.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ott.InvalidOneTimeTokenException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import javax.management.relation.RoleInfoNotFoundException;
import javax.management.relation.RoleNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse usernameNotFound(UsernameNotFoundException e) {
        return new APIResponse(
                404,
                "Email Not Found",
                e.getMessage()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse handleRuntimeException(RuntimeException e){
        return new APIResponse(
                500,
                "Internal Server Error",
                e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponse handleExpiredJwtException(ExpiredJwtException e){
        return new APIResponse(401,"Expired Token",e.getMessage());
    }

    @ExceptionHandler(InvalidOneTimeTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponse handleInvalidOneTimeTokenException(InvalidOneTimeTokenException e){
        return new APIResponse(401,"Invalid Token",e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public APIResponse handleBadCredentialsException(BadCredentialsException e){
        return new APIResponse(401,"Bad Credentials",
                "Invalid Email or Password");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        e.getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        APIResponse response = new APIResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse handleRoleNotFoundException(RoleNotFoundException e){
        return new APIResponse(404 ,
                "Role not found" ,
                "User role Not found");
    }

    @ExceptionHandler(ServiceNotFoundEception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse handleServiceNotFoundException(ServiceNotFoundEception e){
        return new APIResponse(404 ,
                "Service not found" ,
                "Service Not found");
    }
}

