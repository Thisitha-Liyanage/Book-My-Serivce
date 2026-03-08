package lk.ijse.aad.backend.Service;

import lk.ijse.aad.backend.Dto.AuthResponseDto;
import lk.ijse.aad.backend.Dto.LoginDto;
import lk.ijse.aad.backend.Dto.UserDto;
import lk.ijse.aad.backend.Entity.Role;
import lk.ijse.aad.backend.Entity.User;

public interface UserService {
    AuthResponseDto login(LoginDto LoginDto);
    UserDto createAccount(UserDto UserDto);
    UserDto findUserByEmail(String email);
    long countByRole(String role);
}
