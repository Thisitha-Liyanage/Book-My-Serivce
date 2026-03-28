package lk.ijse.aad.backend.Service.Impl;

import lk.ijse.aad.backend.Dto.ProviderResponseDto;
import lk.ijse.aad.backend.Dto.UserDto;
import lk.ijse.aad.backend.Entity.Availability;
import lk.ijse.aad.backend.Entity.ProviderAvailability;
import lk.ijse.aad.backend.Entity.User;
import lk.ijse.aad.backend.Repo.AvailabilityRepo;
import lk.ijse.aad.backend.Repo.UserRepo;
import lk.ijse.aad.backend.Service.AvailabilityService;
import lk.ijse.aad.backend.Service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    @Autowired
    private AvailabilityRepo availabilityRepo;

    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Override
    public void updateAvailability(String email, String status) {
        // Get managed User entity
        Optional<User> provider = Optional.ofNullable(userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Provider not found")));


        Optional<ProviderAvailability> existing = availabilityRepo.findByProvider(provider.get());

        ProviderAvailability availability = existing.orElseGet(ProviderAvailability::new);
        availability.setProvider(provider.get());
        availability.setAvailability(Availability.valueOf(status.toUpperCase()));

        availabilityRepo.save(availability);
    }
}