package org.sopt.snappinserver.domain.curation.service.usecase;

import org.sopt.snappinserver.domain.curation.service.dto.response.GetCurationQuestionResult;

public interface GetCurationQuestionUseCase {

    GetCurationQuestionResult getCurationQuestionPhotos(Long userId, Integer step);
}
