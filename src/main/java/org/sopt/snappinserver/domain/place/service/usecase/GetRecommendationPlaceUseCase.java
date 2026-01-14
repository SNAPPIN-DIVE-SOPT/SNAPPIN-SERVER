package org.sopt.snappinserver.domain.place.service.usecase;

import java.util.List;
import org.sopt.snappinserver.domain.place.service.dto.response.GetRecommendationPlaceResult;

public interface GetRecommendationPlaceUseCase {

    List<GetRecommendationPlaceResult> getPlaceRecommendation();
}
