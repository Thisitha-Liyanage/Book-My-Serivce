package lk.ijse.aad.backend.Service.Impl;

import lk.ijse.aad.backend.Dto.AuthResponseDto;
import lk.ijse.aad.backend.Dto.LoginDto;
import lk.ijse.aad.backend.Dto.UserDto;
import lk.ijse.aad.backend.Entity.Role;
import lk.ijse.aad.backend.Entity.User;
import lk.ijse.aad.backend.Exception.Custom.RoleNotFoundException;
import lk.ijse.aad.backend.Repo.UserRepo;
import lk.ijse.aad.backend.Service.UserService;
import lk.ijse.aad.backend.Util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AuthResponseDto login(LoginDto loginDto) {
        User user = userRepo.findByEmail(loginDto.getEmail()).orElseThrow(
                () -> new RuntimeException("Email not found")
        );
        if(!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        String token = jwtUtil.generateToken(loginDto.getEmail());
        return new AuthResponseDto(token , user.getRole().name());
    }

    @Override
    public UserDto createAccount(UserDto UserDto) {
        if(userRepo.findByEmail(UserDto.getEmail()).isPresent()) {
            throw new BadCredentialsException("Email already in use");
        }
        User user = modelMapper.map(UserDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.valueOf(UserDto.getRole()));
        User savedUser = userRepo.save(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        if (user == null) {
            throw new UsernameNotFoundException("Email not found");
        }
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public long countByRole(String role) {
        long count = userRepo.countByRole(Role.valueOf(role));
        if(count == 0) {
            throw new RoleNotFoundException("Role Not Found");
        }
        return count;
    }

    @Override
    public List<UserDto> findAllUsersbyRole(Role role) {
        List<User> userList = userRepo.findAllByRole(role);

        return userList.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public void deleteUserByEmail(String email) {

        Optional<User> userOpt = userRepo.findByEmail(email);

        if(userOpt.isPresent()){
            userRepo.delete(userOpt.get());
        } else {
            throw new UsernameNotFoundException("Email not found: " + email);
        }
    }

}

