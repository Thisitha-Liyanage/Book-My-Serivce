package lk.ijse.aad.backend.Service;

import lk.ijse.aad.backend.Dto.RatingDto;
import lk.ijse.aad.backend.Dto.RatingResponseDto;
import lk.ijse.aad.backend.Dto.TopRatedServiceDto;

import java.util.List;

public interface RatingService {
    void saveRating(RatingDto ratingDto , String email);
    List<RatingResponseDto> getRatingByService(int id , String email);
    List<TopRatedServiceDto> getTop3Services();
}
