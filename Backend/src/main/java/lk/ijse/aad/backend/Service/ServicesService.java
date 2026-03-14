package lk.ijse.aad.backend.Service;

import lk.ijse.aad.backend.Dto.ServiceDto;
import lk.ijse.aad.backend.Dto.ServiceResponseDto;

import java.util.List;

public interface ServicesService {
    int conuntAllServices();
    List<ServiceResponseDto>getAllServices();
    ServiceResponseDto getServiceByID(int id);
    void deleteServiceByID(int id);
}
