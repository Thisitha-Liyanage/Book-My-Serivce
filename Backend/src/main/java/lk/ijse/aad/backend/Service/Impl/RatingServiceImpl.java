package lk.ijse.aad.backend.Service.Impl;

import lk.ijse.aad.backend.Dto.RatingDto;
import lk.ijse.aad.backend.Dto.RatingResponseDto;
import lk.ijse.aad.backend.Dto.TopRatedServiceDto;
import lk.ijse.aad.backend.Entity.Ratings;
import lk.ijse.aad.backend.Entity.Services;
import lk.ijse.aad.backend.Repo.RatingRepo;
import lk.ijse.aad.backend.Repo.ServiceRepo;
import lk.ijse.aad.backend.Repo.UserRepo;
import lk.ijse.aad.backend.Service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private RatingRepo ratingRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public void saveRating(RatingDto ratingDto , String email) {
        Ratings ratings = new Ratings();
        ratings.setId(0);
        ratings.setRating(ratingDto.getRating());
        ratings.setDescription(ratingDto.getDescription());
        ratings.setCustomer(userRepo.findByEmail(email).get());
        ratings.setService(serviceRepo.findById(ratingDto.getServiceId()).get());
        ratingRepo.save(ratings);
    }

    @Override
    public List<RatingResponseDto> getRatingByService(int serviceId, String email) {
        List<Ratings> ratings = ratingRepo.findByService_Id(serviceId);

        List<RatingResponseDto> ratingResponseDtos = new ArrayList<>();

        for (Ratings r : ratings) {
            RatingResponseDto dto = new RatingResponseDto();
            dto.setRating((int) r.getRating());
            dto.setDescription(r.getDescription());
            dto.setCustomerName(r.getCustomer().getName());
            ratingResponseDtos.add(dto);
        }

        return ratingResponseDtos;
    }

    @Override
    public List<TopRatedServiceDto> getTop3Services() {
        List<Services> services = serviceRepo.findTopRatedServices(PageRequest.of(0, 3));

        return services.stream().map(s -> {
            double avgRating = 0;

            if (s.getRatings() != null && !s.getRatings().isEmpty()) {
                avgRating = s.getRatings().stream()
                        .mapToDouble(Ratings::getRating)
                        .average()
                        .orElse(0);
            }

            return new TopRatedServiceDto(
                    s.getId(),
                    s.getTitle(),
                    s.getProvider().getName(),
                    s.getProvider().getVillage(),
                    s.getDescription(),
                    avgRating,
                    s.getPrice()
            );
        }).collect(Collectors.toList());
    }
}


// collector - get data from data stream , like a get water from river using bucket ====== = = = = = = = = = = = = = = = =
//pageable - use to get selected amount of data