package org.sopt.snappinserver.domain.place.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.snappinserver.domain.place.domain.entity.Place;
import org.sopt.snappinserver.domain.place.repository.PlaceRepository;
import org.sopt.snappinserver.domain.place.service.dto.response.GetPlaceListResult;
import org.sopt.snappinserver.domain.place.service.usecase.GetPlaceListUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GetPlaceListService implements GetPlaceListUseCase {

    private final PlaceRepository placeRepository;

    public GetPlaceListResult getPlaceList(String keyword) {
        List<Place> places = placeRepository.findTop4ByNameStartingWithOrderByNameAsc(keyword);

        return GetPlaceListResult.from(places);
    }
}
