package org.sopt.snappinserver.domain.photographer.service.usecase;

import java.util.List;
import org.sopt.snappinserver.domain.photographer.service.dto.response.GetRandomPhotographersResult;

public interface GetRandomPhotographersUseCase {

    List<GetRandomPhotographersResult> getRandomPhotographersResult();
}
