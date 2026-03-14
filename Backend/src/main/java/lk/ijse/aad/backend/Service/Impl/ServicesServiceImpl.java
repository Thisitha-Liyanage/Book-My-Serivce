package lk.ijse.aad.backend.Service.Impl;

import lk.ijse.aad.backend.Dto.ServiceDto;
import lk.ijse.aad.backend.Dto.ServiceResponseDto;
import lk.ijse.aad.backend.Entity.Services;
import lk.ijse.aad.backend.Entity.User;
import lk.ijse.aad.backend.Exception.Custom.ServiceNotFoundEception;
import lk.ijse.aad.backend.Repo.ServiceRepo;
import lk.ijse.aad.backend.Repo.UserRepo;
import lk.ijse.aad.backend.Service.ServicesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
}
