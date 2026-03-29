package lk.ijse.aad.backend.Service.Impl;

import lk.ijse.aad.backend.Dto.CustomerBookingResponse;
import lk.ijse.aad.backend.Dto.ServiceDto;
import lk.ijse.aad.backend.Dto.ServiceResponseDto;
import lk.ijse.aad.backend.Entity.*;
import lk.ijse.aad.backend.Exception.Custom.DuplicateServiceException;
import lk.ijse.aad.backend.Exception.Custom.ServiceNotFoundEception;
import lk.ijse.aad.backend.Repo.AvailabilityRepo;
import lk.ijse.aad.backend.Repo.ServiceRepo;
import lk.ijse.aad.backend.Repo.UserRepo;
import lk.ijse.aad.backend.Service.ServicesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.management.ServiceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServicesServiceImpl implements ServicesService {
    @Autowired
    ServiceRepo serviceRepo;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AvailabilityRepo availabilityRepo;

    @Override
    public int conuntAllServices() {
        int serviceCount = (int) serviceRepo.count();
        return serviceCount;
    }

    @Override
    public List<ServiceResponseDto> getAllServices() {
        return serviceRepo.findAll()
                .stream()
                .map(service -> {
                    ServiceResponseDto dto = modelMapper.map(service, ServiceResponseDto.class);
                    dto.setUserId(service.getProvider().getId());
                    dto.setCategory(service.getCategory().name());
                    return dto;
                })
                .toList();
    }

    @Override
    public ServiceResponseDto getServiceByID(int id) {
        Optional<Services> service = serviceRepo.findById(id);
        if (!service.isPresent()) {
            throw new ServiceNotFoundEception("Service Not Found");
        }
        return modelMapper.map(service.get(), ServiceResponseDto.class);
    }

    @Override
    public void deleteServiceByID(int id) {
        Optional<Services> service = serviceRepo.findById(id);
        if (service.isPresent()) {
            serviceRepo.deleteById(id);
        }else{
            throw new ServiceNotFoundEception("Service Not Found");
        }
    }

    @Override
    public int countServicesByProviderId(int providerId) {

        User provider = userRepo.findById(providerId).orElseThrow(
                () -> new ServiceNotFoundEception("Provider Not Found")
        );

        long count = serviceRepo.countByProvider(provider);
        return (int) count;
    }


    @Override
    public List<ServiceResponseDto> getAllServicesByProviderId(int providerId) {
        return serviceRepo.findAllByProvider_Id(providerId)
                .stream()
                .map(service -> {
                    ServiceResponseDto dto = modelMapper.map(service, ServiceResponseDto.class);
                    dto.setUserId(service.getProvider().getId());
                    dto.setCategory(service.getCategory().name());
                    return dto;
                })
                .toList();
    }

    @Override
    public ServiceResponseDto saveService(ServiceDto serviceDto) {
        List<Services> services = serviceRepo.findAllByTitleAndDescription(
                serviceDto.getTitle(),serviceDto.getDescription());
        System.out.println(serviceDto.getAvailability());

        Services service = modelMapper.map(serviceDto, Services.class);
        service.setAvailability(Availability.valueOf(
                serviceDto.getAvailability().toUpperCase()));
        service.setId(0);

        if (services.isEmpty()) {
            return modelMapper.map(serviceRepo.save(service), ServiceResponseDto.class);
        }else {
            throw new DuplicateServiceException("Duplicate Service");
        }
    }
    @Override
    public ServiceResponseDto updateService(ServiceResponseDto serviceDto) {
        Services exsit = serviceRepo.findById(serviceDto.getId())
                .orElseThrow(() -> new ServiceNotFoundEception("Service Not Found"));

        exsit.setTitle(serviceDto.getTitle());
        exsit.setDescription(serviceDto.getDescription());
        exsit.setPrice(serviceDto.getPrice());
        exsit.setProvider(userRepo.findById(serviceDto.getUserId())
                .orElseThrow(() -> new ServiceNotFoundEception("Provider Not Found")));

        exsit.setAvailability(Availability.valueOf(serviceDto.getAvailability().toUpperCase()));
        exsit.setCategory(Category.valueOf(serviceDto.getCategory()));

        return modelMapper.map(serviceRepo.save(exsit), ServiceResponseDto.class);
    }

    @Override
    public CustomerBookingResponse getServiceAndProviderByID(int id) {
        Optional<Services> services = serviceRepo.findById(id);
        if (!services.isPresent()) {
            throw new ServiceNotFoundEception("Service Not Found");
        }
        CustomerBookingResponse customerBookingResponse = new CustomerBookingResponse();
        customerBookingResponse.setId(services.get().getId());
        customerBookingResponse.setTitle(services.get().getTitle());
        customerBookingResponse.setPrice(services.get().getPrice());
        customerBookingResponse.setStatus(services.get().getAvailability().name());


        Optional<User> user = userRepo.findById(services.get().getProvider().getId());

        if (!user.isPresent()) {
            throw new ServiceNotFoundEception("User Not Found");
        }

        customerBookingResponse.setProviderName(user.get().getName());
        return customerBookingResponse;
    }

    @Override
    public List<CustomerBookingResponse> getSuggestedServicesByCity(String email) {

        User customer = userRepo.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Provider Not Found")
        );

        String city = customer.getCity();


        List<Services> services = serviceRepo.findByProvider_CityAndAvailability(city, Availability.AVAILABLE);
        List<CustomerBookingResponse> customerBookingResponses = new ArrayList<>();


        for (Services service : services) {
            CustomerBookingResponse response = new CustomerBookingResponse();

            response.setId(service.getId());
            response.setTitle(service.getTitle());
            response.setPrice(service.getPrice());
            response.setCategory(service.getCategory().name());
            response.setDescription(service.getDescription());
            response.setStatus(service.getAvailability().name());

            Optional<User> provider = userRepo.findById(service.getProvider().getId());
            if (provider.isPresent()) {
                response.setProviderName(provider.get().getName());
                response.setProviderPhone(provider.get().getPhone());
                response.setProviderEmail(provider.get().getEmail());
                response.setProviderVillage(provider.get().getVillage());
            }

            Optional<ProviderAvailability> providerAvailability = availabilityRepo.findByProvider(service.getProvider());
            providerAvailability.ifPresent(
                    availability -> response.setProviderAvailability
                            (availability.getAvailability().name()));
            customerBookingResponses.add(response);

        }
        return customerBookingResponses;
    }


}
