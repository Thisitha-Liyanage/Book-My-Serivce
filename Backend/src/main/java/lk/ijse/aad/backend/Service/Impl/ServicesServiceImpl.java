package lk.ijse.aad.backend.Service.Impl;

import lk.ijse.aad.backend.Dto.ServiceDto;
import lk.ijse.aad.backend.Dto.ServiceResponseDto;
import lk.ijse.aad.backend.Entity.Availability;
import lk.ijse.aad.backend.Entity.Category;
import lk.ijse.aad.backend.Entity.Services;
import lk.ijse.aad.backend.Entity.User;
import lk.ijse.aad.backend.Exception.Custom.DuplicateServiceException;
import lk.ijse.aad.backend.Exception.Custom.ServiceNotFoundEception;
import lk.ijse.aad.backend.Repo.ServiceRepo;
import lk.ijse.aad.backend.Repo.UserRepo;
import lk.ijse.aad.backend.Service.ServicesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.management.ServiceNotFoundException;
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
    public void deleteServiceById(int id) {
        Services service = serviceRepo.findById(id)
                .orElseThrow(() -> new ServiceNotFoundEception("Service with ID " + id + " not found"));
        serviceRepo.delete(service);
    }


}
