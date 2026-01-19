package org.sopt.snappinserver.domain.curation.service.usecase;

import org.sopt.snappinserver.domain.curation.service.dto.response.GetAllCurationQuestionResult;

public interface GetAllCurationQuestionUseCase {

    GetAllCurationQuestionResult getAllCurationQuestions(Long userId);

}
