package org.sopt.snappinserver.domain.place.service.usecase;

import org.sopt.snappinserver.domain.place.service.dto.response.GetPlaceListResult;

public interface GetPlaceListUseCase {

    GetPlaceListResult getPlaceList(String keyword);
}
